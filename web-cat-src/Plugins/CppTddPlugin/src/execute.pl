#!c:\perl\bin\perl.exe
#=============================================================================
#   @(#)$Id: execute.pl,v 1.12 2011/10/25 05:06:43 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Curator: execute script for Java submissions
#
#   usage:
#       newGbExecute.pl <properties-file>
#=============================================================================

use strict;
use warnings;
use Carp qw( carp croak );
use Cwd qw( getcwd abs_path );
use File::Basename;
use File::Copy;
use File::Spec;
use File::stat;
use Proc::Background;
use Config::Properties::Simple;
use Web_CAT::Beautifier;
use Web_CAT::CLOC;
use Web_CAT::FeedbackGenerator;
use Web_CAT::JUnitResultsReader;
use Web_CAT::DerefereeStatsReader;
use Web_CAT::Utilities;
use Text::Tabs;
use XML::Smart;
use Data::Dump qw( dump );

die "ANT_HOME environment variable is not set"
    if !defined( $ENV{ANT_HOME} );
my $ANT = "ant";     # "G:\\ant\\bin\\ant.bat";


#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $propfile    = $ARGV[0];     # property file name
my $cfg         = Config::Properties::Simple->new( file => $propfile );

my $pid         = $cfg->getProperty( 'pid' );
my $working_dir = $cfg->getProperty( 'workingDir' );
my $script_home = $cfg->getProperty( 'scriptHome' );
my $log_dir     = $cfg->getProperty( 'resultDir' );
my $timeout     = $cfg->getProperty( 'timeout', 45 );
my $reportCount = $cfg->getProperty( 'numReports', 0 );

my $maxCorrectnessScore   = $cfg->getProperty( 'max.score.correctness' );
my $maxToolScore          = $cfg->getProperty( 'max.score.tools', 0 );
my $NTprojdir             = $working_dir . "/";

my %status = (
    'studentTestResults'    => undef,
    'instrTestResults'      => undef,
    'studentDerefereeStats' => undef,
    'instrDerefereeStats'   => undef,
);


#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
my $useSpawn           = 1;
my $postProcessingTime = 20;
my $callAnt            = 1;

my $antLogRelative     = "ant.log";
my $antLog             = "$log_dir/$antLogRelative";
my $scriptLogRelative  = "script.log";
my $scriptLog          = "$log_dir/$scriptLogRelative";
my $compileLogRelative = "compile.log";
my $compileLog         = "$log_dir/$compileLogRelative";
my $testLogRelative    = "test.log";
my $testLog            = "$log_dir/$testLogRelative";
my $instrLogRelative   = "instr.log";
my $instrLog           = "$log_dir/$instrLogRelative";
my $timeoutLogRelative = "timeout.log";
my $timeoutLog         = "$log_dir/$timeoutLogRelative";
my $markupPropFile     = "$script_home/markup.properties";
my $explanationRelative = "explanation.log";
my $explanation        = "$log_dir/$explanationRelative";
my $covfile            = "$log_dir/test.cov";
my $can_proceed        = 1;
my $runtimeScore       = 0;
my $staticScore        = 0;

my $instructorTestsRun     = 0;
my $instructorTestsFailed  = 0;
my $instructorCasesPercent = 0;
my $gradedElements         = 0;
my $gradedElementsCovered  = 0;
my $studentCasesPercent    = 0;
my $codeCoveragePercent    = 0;
my $studentTestTitle;
my $studentTestMsgs;
my $totalToolDeductions    = 0;
my $antLogOpened           = 0;


#-------------------------------------------------------
# In the future, these could be set via parameters set in Web-CAT's
# interface
#-------------------------------------------------------
my $debug                = $cfg->getProperty( 'debug',      0 );
my $hintsLimit           = $cfg->getProperty( 'hintsLimit', 3 );
my $allStudentTestsMustPass =
    $cfg->getProperty( 'allStudentTestsMustPass', 0 );
$allStudentTestsMustPass =
    ( $allStudentTestsMustPass =~ m/^(true|on|yes|y|1)$/i );
my $coverageMetric       = $cfg->getProperty( 'coverageMetric', 0 );
my $measureCodeCoverage  = $coverageMetric > 0;
if ( !$measureCodeCoverage )
{
    $cfg->setProperty( 'disableCodeCoverage', 1 );
}


my @beautifierIgnoreFiles = ();

$ENV{'COVFILE'} = $covfile;

my $bullseyeDir = $cfg->getProperty( 'bullseyeDir' );
my $pathSep = $cfg->getProperty( 'PerlForPlugins.path.separator', ':' );

if ( defined $bullseyeDir )
{
    $ENV{'PATH'} = $bullseyeDir . '/bin' . $pathSep . $ENV{'PATH'}
}


#=============================================================================
# Generate derived properties for ANT
#=============================================================================
# testCases
my $scriptData = $cfg->getProperty( 'scriptData', '.' );
#my @scriptDataDirs = < $scriptData/* >;
$scriptData =~ s,/$,,;

sub findScriptPath
{
    my $subpath = shift;
    my $target = "$scriptData/$subpath";
#    foreach my $sddir ( @scriptDataDirs )
#    {
#       my $target = $sddir ."/$subpath";
#       #print "checking $target\n";
        if ( -e $target )
        {
            return $target;
        }
#    }
    die "cannot file user script data file $subpath in $scriptData";
}

# testCases
my $testCasePath = "${script_home}/tests";
{
    my $testCaseFileOrDir = $cfg->getProperty( 'testCases' );
    if ( defined $testCaseFileOrDir && $testCaseFileOrDir ne "" )
    {
        my $target = findScriptPath( $testCaseFileOrDir );
        if ( -d $target )
        {
            $cfg->setProperty( 'testCasePath', $target );
        }
        else
        {
            $cfg->setProperty( 'testCasePath', dirname( $target ) );
            $cfg->setProperty( 'testCasePattern', basename( $target ) );
            $target = dirname( $target );
        }
        $testCasePath = $target;
    }
}
$testCasePath =~ s,/,\\,g;

# assignmentIncludes
my $assignmentIncludes;
{
    my $p = $cfg->getProperty( 'assignmentIncludes' );
    if ( defined $p && $p ne "" )
    {
        $assignmentIncludes = findScriptPath( $p );
        $cfg->setProperty( 'assignmentIncludes.abs', $assignmentIncludes );
    }
}

# assignmentLib
my $assignmentLib;
{
    my $p = $cfg->getProperty( 'assignmentLib' );
    if ( defined $p && $p ne "" )
    {
        $assignmentLib = findScriptPath( $p );
        $cfg->setProperty( 'assignmentLib.abs', $assignmentLib );
    }
}

# generalIncludes
my $generalIncludes;
{
    my $p = $cfg->getProperty( 'generalIncludes' );
    if ( defined $p && $p ne "" )
    {
        $generalIncludes = findScriptPath( $p );
        $cfg->setProperty( 'generalIncludes.abs', $generalIncludes );
    }
}

# generalLib
my $generalLib;
{
    my $p = $cfg->getProperty( 'generalLib' );
    if ( defined $p && $p ne "" )
    {
        $generalLib = findScriptPath( $p );
        $cfg->setProperty( 'generalLib.abs', $generalLib );
    }
}

# timeout
my $timeoutForOneRun = $cfg->getProperty( 'timeoutForOneRun', 30 );
$cfg->setProperty( 'exec.timeout', $timeoutForOneRun * 1000 );

$cfg->save();


#=============================================================================
# Prep for output
#=============================================================================
my $DOSStyle_log_dir = $log_dir;
$DOSStyle_log_dir =~ s,/,\\,g;
my $DOSStyle_script_home = $script_home;
$DOSStyle_script_home =~ s,/,\\,g;
my $DOSStyle_NTprojdir = $NTprojdir;
$DOSStyle_NTprojdir =~ s,/,\\,g;
my $DOSStyle_scriptData = $scriptData;
$DOSStyle_scriptData =~ s,/,\\,g;
my $testCasePattern = $cfg->getProperty( 'testCasePattern' );
my $unixTestCasePath = $testCasePath;
$unixTestCasePath =~ s,\\,/,g;

sub regexize_path
{
    # transform a path to a suffix-finding RE like this:
    # from: /a/b/c/d
    # to: ((../)+|/)((((a/)?b/)?c/)?d/)

    my $path = shift;

    $path =~ m,^/?(.*)/?$,;
    $path = $1;
    my $result = "";

    my @components = split( /\//, $path );
    foreach my $i ( 0 .. $#components )
    {
        my $comp = $components[$i];
        $result = "(" . $result . quotemeta($comp) . "/)";
        $result .= "?" if ( $i < $#components );
    }

    return $result;
}

sub sanitize_path
{
    my $path = shift;
    my $dir1;
    my $dir2;
    my $re;

    $dir1 = regexize_path( $working_dir );
    $dir2 = regexize_path( abs_path( $working_dir ) );
    $re = "((\\.\\./)+|/)(" . $dir1 . "|" . $dir2 . ")";
    $path =~ s,$re,,gi;

    return $path;
}

sub path_contains_part_of_path
{
    my $path = shift;
    my $destpath = shift;

    my $dir1;
    my $dir2;
    my $re;

    $dir1 = regexize_path( $destpath );
    $dir2 = regexize_path( abs_path( $destpath ) );
    $re = "((\\.\\./)+|/)(" . $dir1 . "|" . $dir2 . ")";

    if ( $path =~ /$re/ )
    {
        return 1;
    }
    else
    {
        return 0;
    }
}


sub prep_for_output
{
    my $result = shift;
    # print "before: $result";
    $result =~ s,([a-z]:)?\Q$log_dir\E(/bin/[^\s]*(:[0-9]+)?:)?,,gi;
    $result =~ s,([a-z]:)?\Q$DOSStyle_log_dir\E(\\bin\\[^\s]*(:[0-9]+)?:)?,,gi;

    # print "\t1: $result";
    $result =~ s,([a-z]:)?\Q$script_home\E(/[^\s]*(:[0-9]+)?:)?,,gi;
    $result =~ s,([a-z]:)?\Q$DOSStyle_script_home\E(\\[^\s]*(:[0-9]+)?:)?,,gi;

    # print "\t2: $result";
    $result =~ s,([a-z]:)?\Q$script_home\E/tests(/[^\s]*(:[0-9]+)?:)?,,gi;
    $result =~
        s,([a-z]:)?\Q$DOSStyle_script_home\E\\tests(\\[^\s]*(:[0-9]+)?:)?,,gi;

    # print "\t3: $result";
    $result =~ s,([a-z]:)?\Q$NTprojdir\E/__/([^\s]*(:[0-9]+)?:)?,,gi;
    $result =~ s,([a-z]:)?\Q$DOSStyle_NTprojdir\E\\__\\([^\s]*(:[0-9]+)?:)?,,gi;
    # print "\t4: $result";
    $result =~ s,([a-z]:)?\Q$NTprojdir\E(/)?,,gi;
    $result =~ s,([a-z]:)?\Q$DOSStyle_NTprojdir\E(/)?,,gi;
    $result = sanitize_path( $result );

    $result =~ s,([a-z]:)?\Q$testCasePath\E(/)?[^:\s]*\.h,<<reference tests>>,gi;
    $result =~ s,([a-z]:)?\Q$testCasePath\E(/)?,,gi;
    $result =~ s,([a-z]:)?\Q$unixTestCasePath\E(/)?[^:\s]*\.h,<<reference tests>>,gi;
    $result =~ s,([a-z]:)?\Q$unixTestCasePath\E(/)?,,gi;
    #print "testCasePath = $testCasePath\n";
    #print "unixTestCasePath = $unixTestCasePath\n";
    #print "scriptData = $scriptData\n";
    #print "DOSStyle_scriptData = $DOSStyle_scriptData\n";

    # print "\t4.5: $result";
    $result =~ s,([a-z]:)?\Q$scriptData\E(/)?,,gi;
    $result =~ s,([a-z]:)?\Q$DOSStyle_scriptData\E(/)?,,gi;

    # print "\t5: $result";
    if ( defined( $testCasePattern ) )
    {
        $result =~ s/([^\s]*(\/|\\))?\Q$testCasePattern\E/<<reference tests>>/gi;
    }
    $result =~ s/([^\s]*(\/|\\))?(_)*instructor(_)*test(s)?\.(h|cpp)/<<reference tests>>/gio;
    $result =~ s/([^\s]*(\/|\\))?runinstructortests\.cpp/<<reference tests>>/gio;

    # print "\t6: $result";
    $result =~ s/^[0-9]+:\s*//o;
    $result =~ s/&/&amp;/go;
    $result =~ s/</&lt;/go;
    $result =~ s/>/&gt;/go;
    # print "\t7: $result";
    return expand( $result );
}

#=============================================================================
# Script Startup
#=============================================================================
# Change to specified working directory and set up log directory
chdir( $working_dir );

print "working dir set to $working_dir\n" if $debug;

if ( $debug > 2 )
{
    print "path = ", $ENV{PATH}, "\n\n";
    if ( defined $ENV{INCLUDE} )
    {
           print "include = ", $ENV{INCLUDE}, "\n\n";
    }
    if ( defined $ENV{LIB} )
    {
        print "lib = ", $ENV{LIB}, "\n\n";
    }
}

# localFiles
sub copyHere
{
    my $file = shift;
    my $base = shift;
    my $newfile = $file;
    $newfile =~ s,^\Q$base/\E,,;
    if ( -d $file )
    {
        if ( $file ne $base )
        {
            print "mkdir( $newfile );\n" if $debug;
            mkdir( $newfile );
        }
        foreach my $f ( <$file/*> )
        {
            copyHere( $f, $base );
        }
    }
    else
    {
        print "copy( $file, $newfile );\n" if $debug;
        copy( $file, $newfile );
        push( @beautifierIgnoreFiles, $newfile );
    }
}

{
    my $localFiles = $cfg->getProperty( 'localFiles' );
    if ( defined $localFiles && $localFiles ne "" )
    {
        my $lf = findScriptPath( $localFiles );
        print "localFiles = $lf\n" if $debug;
        if ( -d $lf )
        {
            print "localFiles is a directory\n" if $debug;
            copyHere( $lf, $lf );
        }
        else
        {
            print "localFiles is a single file\n" if $debug;
            my $base = $lf;
            $base =~ s,/[^/]*$,,;
            copyHere( $lf, $base );
        }
    }
}
if ( ! -d "$script_home/obj" )
{
    mkdir( "$script_home/obj" );
}



#=============================================================================
# Run ANT script and collect the log
#=============================================================================
my $time1        = time;
my $testsRun     = 0; #0
my $testsFailed  = 0;

if ( $callAnt )
{
    if ( $debug > 2 ) { $ANT .= " -v"; }
    my $cmdline = $Web_CAT::Utilities::SHELL
        . "$ANT -f \"$script_home/build.xml\" -l \"$antLog\" "
        . "-propertyfile \"$propfile\" \"-Dbasedir=$working_dir\" "
        . "2>&1 > " . File::Spec->devnull;

    print $cmdline, "\n" if ( $debug );
    if ( $useSpawn )
    {
        # Changing the HOME directory is a hack to make Bullseye work
        # correctly on Unix.  It tries to put a .BullseyeCoverage file in
        # the user's home directory, and when running under Tomcat, this
        # home dir might be the Tomcat root, which may not be writable.
        # We change it here to the working directory while ant is running.

        my $old_home = $ENV{'HOME'};
        $ENV{'HOME'} = $working_dir;

        my ( $exitcode, $timeout_status ) = Proc::Background::timeout_system(
                $timeout - $postProcessingTime, $cmdline );

        if (defined($old_home))
        {
            $ENV{'HOME'} = $old_home;
        }

        if ( $timeout_status )
        {
            $can_proceed = 0;
            my $feedbackGenerator =
                new Web_CAT::FeedbackGenerator( $timeoutLog );
            $feedbackGenerator->startFeedbackSection(
                "Errors During Testing" );
            $feedbackGenerator->print( <<EOF );
<p><font color="#ee00bb">Testing your solution exceeded the allowable time
limit for this assignment.</font></p>
<p>Most frequently, this is the result of <b>infinite recursion</b>--when
a recursive method fails to stop calling itself--or <b>infinite
looping</b>--when a while loop or for loop fails to stop repeating.</p>
<p>As a result, no time remained for further analysis of your code.</p>
EOF
            $feedbackGenerator->endFeedbackSection;
            $feedbackGenerator->close;
            # Add to list of reports
            # -----------
            $reportCount++;
            $cfg->setProperty( "report${reportCount}.file",
                               $timeoutLogRelative );
            $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
        }
    }
    else
    {
        system( $cmdline );
    }
}

my $time2 = time;
if ( $debug )
{
    print "\n", ( $time2 - $time1 ), " seconds\n";
}
my $time3 = time;


#-----------------------------------------------
# A useful subroutine for processing the ant log
sub scanTo {
    my $pattern = shift;
    # print "scanning for $pattern\n";
    if ( defined( $_ ) && !( m/$pattern/ ) )
    {
        while ( <ANTLOG> )
        {
            last if m/$pattern/;
            # print "skipping: ";
            # print;
        }
    }
#    if ( m/$pattern/ )
#    {
#       print "found: ";
#       print;
#    }
#    else
#    {
#       print "found: end of file\n";
#    }
}

#-----------------------------------------------
# Generate a script warning
sub adminLog {
    open( SCRIPTLOG, ">>$scriptLog" ) ||
        die "Cannot open file for output '$scriptLog': $!";
    print SCRIPTLOG join( "\n", @_ ), "\n";
    close( SCRIPTLOG );
}


#=============================================================================
# check to see if student tests are really there!
#=============================================================================
if ( $can_proceed )
{
    open( ANTLOG, "$antLog" ) ||
        die "Cannot open file for input '$antLog': $!";
    $antLogOpened++;

    $_ = <ANTLOG>;
    scanTo( qr/^generateStudentMain:/ );
    $_ = <ANTLOG>;
    if ( defined( $_ ) && m/^\s*\[apply\].*(usage|no tests defined)/io )
    {
        $can_proceed = 0;
        open( TESTLOG, ">$testLog" ) ||
            die "Cannot open file for output '$testLog': $!";
        print TESTLOG<<EOF;
<div class="shadow"><table><tbody>
<tr><th>Errors During Testing</th></tr>
<tr><td><pre>
<font color=\"#ee00bb\">No tests included in submission.</font>
</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>
EOF
        close( TESTLOG );
        $reportCount++;
        $cfg->setProperty( "report${reportCount}.file",     $testLogRelative );
        $cfg->setProperty( "report${reportCount}.mimeType", "text/html"      );
    }
}
elsif ( $debug ) { print "cxxtest student generation analysis skipped\n"; }


#=============================================================================
# check for compiler errors (or warnings) on student test cases
#=============================================================================
if ( $can_proceed )
{
    scanTo( qr/^compile:/ );
    scanTo( qr/^\s*\[cc\]/ );
    my $compileMsgs     = "";
    my $compileErrs     = 0;
    my $compileWarnings = 0;
    if ( !defined( $_ )  ||  $_ !~ m/^\s*\[cc\].*files to be compiled/ )
    {
        if ( defined( $_ ) )
        {
            adminLog( "Failed to find '[cc] ... files to be compiled' "
                      . "in line:\n$_" );
        }
        $can_proceed = 0;
        $compileMsgs = "Cannot locate compiler output for analysis.\n";
        $compileErrs++;
    }
    $_ = <ANTLOG>;
    while ( defined( $_ )  &&  ( s/^\s*\[cc\] //o  ||  m/^\s*$/o ) )
    {
        if ( m/^\s*$/o ) { $_ = <ANTLOG>; next; }
        # print "msg: $_";
        if ( m/^(\s*[A-Za-z]:)?[^:]+:([0-9]*:)?\s*error:/o ||
             m/no such file/io ||
             m/ld returned 1 exit status/o )
        {
            # print "err: $_";
            $compileErrs++;
            $can_proceed = 0;
        }
        elsif ( m/^(\s*[A-Za-z]:)?[^:]+:([0-9]*:)?\s*warning:/o )
        {
            # print "warning: $_";
            $compileWarnings++;
            # $can_proceed = 0;
        }
        elsif ( m/\bBullseye(Coverage Compile| Testing)\b/o
                || m/^Starting link\s*$/o )
        {
            $_ = "";
        }
        $compileMsgs .= prep_for_output( $_ );
        $_ = <ANTLOG>;
    }
    $compileMsgs =~ s/^\s*starting link\s*$//io;
    if ( $compileMsgs ne "" )
    {
        open( COMPILELOG, ">$compileLog" ) ||
            die "Cannot open file for output '$compileLog': $!";
        print COMPILELOG<<EOF;
<div class="shadow"><table><tbody>
<tr><th>
EOF
        if ( $compileErrs )
        {
            print COMPILELOG "Compilation Produced Errors";
        }
        else
        {
            print COMPILELOG "Compilation Produced Warnings";
        }
        print COMPILELOG<<EOF;
</th></tr>
<tr><td><pre>
$compileMsgs
</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>
EOF
        close( COMPILELOG );

        # Add to list of reports
        # -----------
        $reportCount++;
        $cfg->setProperty( "report${reportCount}.file", $compileLogRelative );
        $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
    }
}
elsif ( $debug ) { print "compiler output analysis skipped\n"; }
$time3 = time;
if ( $debug )
{
    print "\n", ( $time3 - $time2 ), " seconds\n";
}


#=============================================================================
# collect testing stats for student tests
#=============================================================================
if ( $can_proceed )
{
    scanTo( qr/^run\.student\.tests:/ );
    scanTo( qr/^\s*\[exec\]/ );
    if ( !defined( $_ ) || $_ !~ m/^\s*\[exec\]\s+/ )
    {
        # adminLog( "Failed to find [junit] in line:\n$_" );
    }
    my $testMsgs    = "";
    my $timeoutOccurred = 0;
    while ( defined( $_ )  &&  ( s/^\s*\[exec\] //o || m/^$/o ) )
    {
        # print "msg: $_";
        if ( m/^running\s*([0-9]+)\s*test(s)?/io )
        {
            print "stats: $_" if ( $debug > 1 );
            $testsRun     += $1;
        }
        elsif ( m/^failed\s*([0-9]+)\s*of\s*([0-9]+)\s*test(s)?/io )
        {
            print "stats: $_" if ( $debug > 1 );
            $testsFailed  += $1;
        }
        elsif ( m/^timeout: killed/io )
        {
            $timeoutOccurred++;
        }
        $testMsgs .= prep_for_output( $_ );
        $_ = <ANTLOG>;
    }

    if ( $timeoutOccurred )
    {
        $testMsgs .= "\n<p><font color=\"#ee00bb\">Testing your solution exceeded the allowable time limit for this assignment.</font></p>
<p>This error occurred while executing <b>your tests</b> on your code.</p>
<p>Most frequently, this is the result of <b>infinite recursion</b>--when
a recursive method fails to stop calling itself--or <b>infinite
looping</b>--when a while loop or for loop fails to stop repeating.</p>
<p>As a result, no time remained for further analysis of your code.</p>
\n";
        $testsFailed++;
    }
    if ( $testsFailed && $allStudentTestsMustPass )
    {
        $testMsgs .= "\n<font color=\"#ee00bb\">All of your tests must "
            . "pass for you to get further feedback.</font>\n";
        $can_proceed = 0;
    }
    $studentTestTitle = "Results From Running Your Tests";
    if ( $testsRun == 0 || $testsFailed > 0 )
    {
        $studentTestTitle   = "Errors Running Your Tests";
    }
    if ( $testsRun == 0 )
    {
        $can_proceed = 0;
        $testMsgs .=
        "\n<font color=\"#ee00bb\">No tests included in submission.</font>";
    }

    $studentCasesPercent = $testsRun > 0 ? int(
        ( ( $testsRun - $testsFailed ) / $testsRun ) * 100.0 + 0.5 )
        : 0;
    if ( $testsFailed && $studentCasesPercent == 100 )
    {
        # Don't show 100% if some cases failed
        $studentCasesPercent--;
    }

    if ( -f "memwatch.log" )
    {
        open( MEMWATCHLOG, "memwatch.log" ) ||
            die "Cannot open file for input 'memwatch.log': $!";
        my @memLines = <MEMWATCHLOG>;
        close( MEMWATCHLOG );
        $testMsgs .= "\nDynamically allocated memory (heap usage):\n"
            . join( "", @memLines );
    }
    $studentTestMsgs = $testMsgs;
}
elsif ( $debug ) { print "student test results analysis skipped\n"; }
my $time4 = time;
if ( $debug )
{
    print "\n", ( $time4 - $time3 ), " seconds\n";
}


#=============================================================================
# check for compiler errors (or warnings) on instructor test cases
#=============================================================================
if ( $can_proceed )
{
    scanTo( qr/^compileInstructorTests:/ );
    scanTo( qr/^\s*\[cc\]/ );
    my $compileMsgs     = "";
    my $compileErrs     = 0;
    my $collectingMsgs  = 1;
    if ( !defined( $_ )  ||  $_ !~ m/^\s*\[cc\].*files to be compiled/ )
    {
        adminLog( "Failed to find instructor '[cc] ... files to be compiled' "
                  . "in line:\n" . ( defined( $_ ) ? $_ : "<null>" ) );
        $compileMsgs = "Cannot locate behavioral analysis output.\n";
        $compileErrs++;
        $can_proceed = 0;
    }
    $_ = <ANTLOG>;
    while ( defined( $_ )  &&  ( s/^\s*\[cc\] //o  ||  m/^\s*$/o ) )
    {
        if ( m/^\s*$/o ) { $_ = <ANTLOG>; next; }
        # print "msg: $_";
        if ( m/^(\s*[A-Za-z]:)?[^:]+:([0-9]*:)?\s*error:/o ||
             m/no such file/io ||
             m/ld returned 1 exit status/o )
        {
            if ( $compileErrs ) { $collectingMsgs = 0; }
            # print "err: $_";
            $compileErrs++;
            $can_proceed = 0;
        }
        if ( m/in (file|member)/io || m/\(Each/o ) {
            do
            {
                print "skipping: $_" if ( $debug > 4 );
                $_ = <ANTLOG>;
            } while ( defined( $_ ) && m/^\s*\[cc\]\s\s+/o );
            next;
        }
        if ( $collectingMsgs )
        {
            $compileMsgs .= prep_for_output( $_ );
        }
        $_ = <ANTLOG>;
    }
    $compileMsgs =~ s/^\s*starting link\s*$//io;
    if ( $compileErrs )
    {
        my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $instrLog );
        $feedbackGenerator->startFeedbackSection(
            "Estimate of Problem Coverage" );
        $feedbackGenerator->print( <<EOF );
<p><b>Problem coverage: <font color="#ee00bb">unknown</font></b></p><p>
<p>
<font color="#ee00bb">Web-CAT was unable to assess your test cases.</font></p>
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
<font color="#ee00bb">Your code failed to compile correctly against the
reference tests.</font></p>
<p>This is most likely because you have not named your class(es)
or header file(es) as required in the assignment, have failed to provide
a required method or #include directive, or have failed to use
the required name(s) or signature(s) for a method.
</p><p>Failure to follow these constraints will prevent
the proper assessment of your tests.
</p><p>The following specific error(s) were discovered while compiling
reference tests against your submission:</p>
<pre>
$compileMsgs
</pre>
EOF
        $feedbackGenerator->endFeedbackSection;
        $feedbackGenerator->close;

        # Add to list of reports
        # -----------
        $reportCount++;
        $cfg->setProperty( "report${reportCount}.file", $instrLogRelative );
        $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
    }
}
elsif ( $debug ) { print "instructor test generation analysis skipped\n"; }


#=============================================================================
# collect testing stats for instructor tests
#=============================================================================
if ( $can_proceed )
{
    scanTo( qr/^instructorTest:/ );
    scanTo( qr/^\s*\[exec\]/ );
    my %instrHints  = ();
    my $resultsSeen = 0;
    my $timeoutOccurred = 0;
    my $memwatchLog = "";
    if ( !defined( $_ ) || $_ !~ m/^\s*\[exec\]\s+/ )
    {
        adminLog( "Failed to find [exec] in line:\n"
                  . ( defined( $_ ) ? $_ : "<null>" ) );
        $can_proceed = 0;
        $instrHints{"error: Cannot locate behavioral analysis output.\n"} = 1;
        $instructorTestsFailed++;
    }
    while ( defined( $_ )  &&  ( s/^\s*\[exec\] //o || m/^\s*$/o ) )
    {
        # print "msg: $_";
        if ( m/^running\s*([0-9]+)\s*tests/io )
        {
            print "stats: $_" if ( $debug > 1 );
            $instructorTestsRun += $1;
            if ( m/^running\s*([0-9]+)\s*tests(.*)\.ok!$/io )
            {
                $resultsSeen++;
            }
        }
        elsif ( m/^failed\s*([0-9]+)\s*of\s*([0-9]+)\s*tests/io )
        {
            print "stats: $_" if ( $debug > 1 );
            $instructorTestsFailed += $1;
            $resultsSeen++;
        }
        elsif ( s/^In .*:$//o )
        {
            # Just ignore messages that point to file locations
        }
        elsif ( s/^.*(\"?)(SIG[A-Z]*):\s*/$2: /o )
        {
            if ( $1 eq "\"" ) { s/\"$//o; }
            # print "hint: $_";
            if ( $hintsLimit != 0 )
            {
                $instrHints{prep_for_output($_)} = 1;
            }
        }
        elsif ( s/^.*(\"?)\bhint:\s*//io )
        {
            if ( $1 eq "\"" ) { s/\"$//o; }
            # print "hint: $_";
            if ( $hintsLimit != 0 )
            {
                $instrHints{prep_for_output($_)} = 1;
            }
        }
        elsif ( s,^/=MEMWATCH=/:\s*,,o )
        {
            $memwatchLog .= prep_for_output($_);
        }
        elsif ( m/^timeout: killed/io )
        {
            $timeoutOccurred++;
        }
        elsif ( m/^\.+ok!$/io )
        {
            $resultsSeen++;
        }
        # $testMsgs .= prep_for_output( $_ );
        $_ = <ANTLOG>;
    }

    if ( !$resultsSeen && $instructorTestsRun > 0 )
    {
        $instructorTestsFailed = $instructorTestsRun;
        print "no results seen, failed = $instructorTestsFailed\n"
            if ( $debug > 1 );
    }
    my $instrHints = "";
    if ( %instrHints || $memwatchLog ne "" )
    {
        my $wantHints = $hintsLimit;
        if ( $wantHints )
        {
            $instrHints = "<p>The following hint(s) may help you locate "
            . "some ways in which your solution and your testing may be "
            . "improved:</p>\n<pre>\n";
            foreach my $msg ( keys %instrHints )
            {
                if ( $msg =~ m/^(assert|SIG)/o )
                {
                    $instrHints .= "hint: " . $msg;
                }
                elsif ( $hintsLimit > 0 )
                {
                    $instrHints .=
                        "hint: your code/tests do not correctly cover "
                        . $msg;
                    $hintsLimit--;
                }
            }
            my @hintKeys = keys %instrHints;
            my $hintCount = $#hintKeys;
            if ( $hintCount > $wantHints )
            {
                $instrHints .= "\n($wantHints of $hintCount hints shown)\n";
            }
        }
        if ( $memwatchLog ne "" )
        {
            if ( length( $instrHints ) )
            {
                $instrHints .= "\n\n";
            }
            else
            {
                $instrHints = "<pre>\n";
            }
            $instrHints .= $memwatchLog;
        }
        $instrHints .= "</pre>";
    }

    my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $instrLog );
    $feedbackGenerator->startFeedbackSection(
        "Estimate of Problem Coverage" );

    if ( $timeoutOccurred )
    {
        $feedbackGenerator->print( <<EOF );
<p><b>Problem coverage: <font color="#ee00bb">unknown</font></b></p><p>
<p><font color="#ee00bb">Testing your solution exceeded the allowable time
limit for this assignment.</font></p>
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
<p>
In this case, your solution exceeded the time limit while executing
the reference tests.
As a result, no time remained for further analysis of your code.
This issue prevented Web-CAT from properly assessing the thoroughness
of your solution or your test cases.
</p>
<p>Most frequently, this is the result of <b>infinite recursion</b>--when
a recursive method fails to stop calling itself--or <b>infinite
looping</b>--when a while loop or for loop fails to stop repeating.
Note that this can happen <b>even if your test cases do not case such an
error</b>.   If your test cases
do not reveal this error, then <b>your test cases are incomplete</b>.</p>
$instrHints
EOF
    }
    elsif ( $instructorTestsRun > 0 &&
         $instructorTestsFailed == $instructorTestsRun )
    {
        $feedbackGenerator->print( <<EOF );
<p><b>Problem coverage: <font color="#ee00bb">unknown</font></b></p><p>
<p><font color="#ee00bb">Your problem setup does not appear to be consistent
with the assignment.</font></p>
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
<p>
In this case, <b>none of the reference tests pass</b> on your solution,
which may mean that your solution (and your tests) make incorrect assumptions
about some aspect of the required behavior.
This discrepancy prevented Web-CAT from properly assessing the thoroughness
of your solution or your test cases.</p>
<p>Double check that you have carefully followed all initial conditions
requested in the assignment in setting up your solution.
</p>$instrHints
EOF
    }
    elsif ( $instructorTestsRun == 0 ||
            $instructorTestsFailed == 0 )
    {
        $instructorCasesPercent = 100;
        $feedbackGenerator->print( <<EOF );
<p><b>Problem coverage: 100%</b></p><p>
<p>Your solution appears to cover all required behavior for this assignment.
Make sure that your tests cover all of the behavior required.</p>
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.
</p>$instrHints
EOF
    }
    else
    {
        $instructorCasesPercent = $instructorTestsRun > 0 ?
            int(
                ( ( $instructorTestsRun - $instructorTestsFailed ) /
                  $instructorTestsRun ) * 100.0 + 0.5 )
            : 0;
        $feedbackGenerator->print( <<EOF );
<p><b>Problem coverage: <font color="#ee00bb">$instructorCasesPercent%</font></b></p>
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
<p>
Differences in test results indicate that your code still contains bugs.
Your code appears to cover
<font color="#ee00bb">only $instructorCasesPercent%</font>
of the behavior required for this assignment.</p>
<p>
Your test cases are not detecting these defects, so your testing is
incomplete--covering at most <font color="#ee00bb">only
$instructorCasesPercent%</font>
of the required behavior, possibly even less.</p>
</p>
<p>Double check that you have carefully followed all initial conditions
requested in the assignment in setting up your solution, and that you
have also met all requirements for a complete solution in the final
state of your program.
</p>$instrHints
EOF
    }
    $feedbackGenerator->endFeedbackSection();
    $feedbackGenerator->close;

    if ( $can_proceed )
    {
        scanTo( qr/^BUILD FAILED/ );
        if ( defined( $_ )  &&  m/^BUILD FAILED/ )
        {
            warn "ant BUILD FAILED unexpectedly.";
            $can_proceed = 0;
        }
    }

    #
    # Collect student and instructor results from the plist printer
    #
    $status{'studentTestResults'} =
        new Web_CAT::JUnitResultsReader( "$log_dir/student.inc" );
    $status{'instrTestResults'} =
        new Web_CAT::JUnitResultsReader( "$log_dir/instr.inc" );
    $status{'studentDerefereeStats'} =
        new Web_CAT::DerefereeStatsReader( "$log_dir/student-dereferee.inc" );
    $status{'instrDerefereeStats'} =
        new Web_CAT::DerefereeStatsReader( "$log_dir/instr-dereferee.inc" );
}
elsif ( $debug ) { print "instructor test results analysis skipped\n"; }

if ( $antLogOpened )
{
    close( ANTLOG );
    my $headerFile = "$log_dir/ant.header";
    unlink( $headerFile ) if ( -f $headerFile );
}


#=============================================================================
# generate score
#=============================================================================

# tracking covered elements is not currently supported
$runtimeScore = 0;
my $runtimeScoreWithoutCoverage = 0;

if ( $can_proceed )
{
    if ( $instructorTestsRun == 0 ) { $instructorTestsRun = 1; }
    $runtimeScoreWithoutCoverage =
        $maxCorrectnessScore
        * ( ( $instructorTestsRun - $instructorTestsFailed )
            / $instructorTestsRun );

    # No credit unless all student tests pass
    if ( $testsFailed )
    {
        if ( $allStudentTestsMustPass )
        {
            $runtimeScoreWithoutCoverage = 0;
        }
        else
        {
            $runtimeScoreWithoutCoverage *=
                ( $testsRun - $testsFailed )
                / $testsRun;
        }
    }

    # First, the static analysis, tool-based score
    my $staticScore  = $maxToolScore - $totalToolDeductions;

    # Second, the coverage/testing/correctness component
    $runtimeScore = $runtimeScoreWithoutCoverage;
}


#=============================================================================
# Enter bullseye coverage data in properties
#=============================================================================
sub htmlEscape
{
    my $text = shift;
    $text =~ s/&/&amp;/go;
    $text =~ s/</&lt;/go;
    $text =~ s/>/&gt;/go;
    $text =~ s/\"/&quot;/go;
    return $text;
}

sub extractEntity
{
    my $line     = shift;

    $line =~ s/^\s+//o;
    $line =~ s/\s+$//o;
    if ( $line =~ s/^\s*case\b\s*//o )
    {
        $line =~ s/:[^:]*$//o;
    }
    elsif ( $line =~ s/^\s*catch\b\s*//o )
    {
        $line =~ s/\s*\{.*$//o;
        $line =~ s/\)[^\)]*$/\)/o;
    }
    else
    {
        # trim anything from { to end
        #print "1: '$line'\n";
        $line =~ s/\s*\{.*$//o;
        #print "2: '$line'\n";
        my $open  = ( $line =~ tr/\(// );
        my $close = ( $line =~ tr/\)// );
        while ( $open > $close )
        {
            $line =~ s/^[^\(]*\(//o;
            $open--;
        }
        #print "3: '$line'\n";
        while ( $close > $open )
        {
            $line =~ s/\)[^\)]*$//o;
            $close--;
        }
        $line =~ s/^\s*(if|while)\s*\(/\(/o;
        if ( $line =~ s/^\s*\(/\(/o )
        {
            $line =~ s/\)[^\)]*$/\)/o;
        }
        #print "4: '$line'\n";
        $line =~ s/^[&|\s]+//o;
        $line =~ s/[&|\s]+$//o;
        my $first = "";
        my $last  = "";
        if ( length( $line ) )
        {
            $first = substr( $line, 0, 1 );
            $last  = substr( $line, -1 );
            if ( ( $first ne "'" && $first ne '"' && $first ne "(" )
                 || ( $last ne "'" && $last ne '"' && $last ne ")" ) )
            {
                $line = "'$line'";
            }
        }
        #print "5: '$line'\n";
    }
    return htmlEscape( $line );
}

# %codeMarkupIds is a map from file names to codeMarkup numbers
my %codeMarkupIds = ();

# %codeMessages is a hash like this:
# {
#   filename1 => {
#                  <line num> => {
#                                   category => coverage,
#                                   coverage => "...",
#                                   message  => "..."
#                                },
#                  <line num> => { ...
#                                },
#                },
#   filename2 => { ...
#                },
# }
my %codeMessages = ();

sub exclude_from_coverage
{
    my $fqFileName = shift;

    if ( $fqFileName =~ m,^\.\./, )
    {
        my $old_cwd = getcwd();
        chdir $working_dir;
        $fqFileName = abs_path( $fqFileName );
        chdir $old_cwd;
    }

    if ( path_contains_part_of_path( $fqFileName, $scriptData )
             || ( defined( $assignmentIncludes ) &&
                  path_contains_part_of_path( $fqFileName, $assignmentIncludes ) )
             || ( defined( $generalIncludes ) &&
                  path_contains_part_of_path( $fqFileName, $generalIncludes) ) )
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

sub strip_quotes_and_normalize
{
    my $fqFileName = shift;

    if ( length( $fqFileName ) > 0
         && substr( $fqFileName, 0, 1 ) eq '"' )
    {
        $fqFileName = substr( $fqFileName, 1 );
    }
    if ( length( $fqFileName ) > 0
         && substr( $fqFileName, -1 ) eq '"' )
    {
        $fqFileName = substr( $fqFileName, 0, -1 );
    }

    $fqFileName =~ s,\\,/,go;

    return $fqFileName;
}

if ( $testsRun && $measureCodeCoverage )
{
    my $coverageLog    = "$log_dir/covfile.out";
    my $numCodeMarkups = $cfg->getProperty( 'numCodeMarkups', 0 );
    my $topLevelGradedElements = 0;
    if ( open ( COVERAGE, $coverageLog ) )
    {
        while ( <COVERAGE> )
        {
            chomp;
            my @entry = split( /,/ );
            next if ( $#entry < 6 );
            my $normalizedFile = strip_quotes_and_normalize( $entry[0] );

            next if exclude_from_coverage( $normalizedFile );

            $topLevelGradedElements += $entry[2];
            if ( $coverageMetric == 2 )
            {
                $topLevelGradedElements += $entry[5];
            }
        }
        close( COVERAGE );
        print "topLevelGradedElements = $topLevelGradedElements\n"
            if ( $debug > 2 );

        if ( $coverageMetric == 2 )
        {
            $cfg->setProperty( "statElementsLabel",
                               "Methods/Conditions/Decisions Executed" );
        }
        else
        {
            $cfg->setProperty( "statElementsLabel", "Methods Executed" );
        }
        my $ptsPerUncovered = 0.0;
        if ( $topLevelGradedElements > 0 && $runtimeScoreWithoutCoverage > 0 )
        {
            $ptsPerUncovered = -1.0 /
                $topLevelGradedElements * $runtimeScoreWithoutCoverage;
        }

        if ( open ( COVERAGE, $coverageLog ) )
        {
            while ( <COVERAGE> )
            {
                chomp;
                my @entry = split( /,/ );
                next if ( $#entry < 6 );
                my $fqFileName = strip_quotes_and_normalize( $entry[0] );

                next if exclude_from_coverage( $fqFileName );

                $fqFileName = sanitize_path( $fqFileName );

                if ( $debug > 2 )
                {
                    print "coverage: $fqFileName: $_\n";
                }

                $numCodeMarkups++;
                $codeMarkupIds{$fqFileName} = $numCodeMarkups;
                $cfg->setProperty(
                    "codeMarkup${numCodeMarkups}.sourceFileName",
                    $fqFileName );
                $cfg->setProperty( "codeMarkup${numCodeMarkups}.methods",
                                   $entry[2] );
                $cfg->setProperty(
                    "codeMarkup${numCodeMarkups}.methodsCovered",
                    $entry[1] );
                $cfg->setProperty( "codeMarkup${numCodeMarkups}.conditionals",
                                   $entry[5] );
                $cfg->setProperty(
                    "codeMarkup${numCodeMarkups}.conditionalsCovered",
                    $entry[4] );

                my $myElements        = $entry[2];
                my $myElementsCovered = $entry[1];
                if ( $coverageMetric == 2 )
                {
                    $myElements        += $entry[5];
                    $myElementsCovered += $entry[4];
                }
                $gradedElements += $myElements;
                $gradedElementsCovered += $myElementsCovered;

                $cfg->setProperty( "codeMarkup${numCodeMarkups}.elements",
                                   $myElements );
                $cfg->setProperty(
                    "codeMarkup${numCodeMarkups}.elementsCovered",
                    $myElementsCovered );
                $cfg->setProperty( "codeMarkup${numCodeMarkups}.deductions",
                                   ( $myElements - $myElementsCovered ) *
                                   $ptsPerUncovered );
            }
            close( COVERAGE );

            $cfg->setProperty( "numCodeMarkups", $numCodeMarkups );

            if ( $gradedElements > 0 )
            {
                $codeCoveragePercent = $gradedElements > 0 ? int(
                    ( $gradedElementsCovered / $gradedElements )
                    * 100.0 + 0.5 )
                    : 0;
                if ( $gradedElementsCovered < $gradedElements
                     && $codeCoveragePercent == 100 )
                {
                    # Don't show 100% if some cases failed
                    $codeCoveragePercent--;
                }
                $runtimeScore *= $gradedElementsCovered / $gradedElements;
            }
            else
            {
                $runtimeScore = 0;
            }
        }

        # Now process the CSV file for source info
        $coverageLog = "$log_dir/covfunc.out";
        if ( open ( COVERAGE, $coverageLog ) )
        {
            while ( <COVERAGE> )
            {
                chomp;
                my @entry = split( /,/ );
                next if ( $#entry < 5 );
                $entry[0] = strip_quotes_and_normalize( $entry[0] );

                next if exclude_from_coverage( $entry[0] );

                $entry[0] = sanitize_path( $entry[0] );
                if ( $entry[3] eq "function" && $entry[4] eq "" )
                {
                    if ( !defined $codeMessages{$entry[0]} )
                    {
                        $codeMessages{$entry[0]} = {};
                    }
                    if ( !defined $codeMessages{$entry[0]}{$entry[1]} )
                    {
                        $codeMessages{$entry[0]}{$entry[1]} = {
                            category => 'coverage',
                            coverage => 'function',
                            message  => "Method "
                                . htmlEscape( join( ', ', @entry[5..$#entry] ) )
                                . " was never executed."
                            };
                    }
                }
            }
            close( COVERAGE );
        }

        # Now process the CSV file for source info
        $coverageLog = "$log_dir/covsrc.out";
        if ( open ( COVERAGE, $coverageLog ) )
        {
            my $thisFile;
            while ( <COVERAGE> )
            {
                chomp;
                if ( m/^(\S*):$/o )
                {
                    $thisFile = strip_quotes_and_normalize( $1 );

                    if ( exclude_from_coverage( $thisFile ) )
                    {
                        $thisFile = undef;
                    }
                    else
                    {
                        #$thisFile =~ s,^\Q${working_dir}\E(/)*,,o;
                        $thisFile = sanitize_path( $thisFile );
                    }
                    next;
                }
                if ( s/^\s*-->//o )
                {
                    if ( !defined( $thisFile ) )
                    {
                        #print "Warning: no file for coverage line:\n$_\n";
                        next;
                    }
                    s/^(t|T|f|F)?\s*([0-9]+)([a-z]?)\s*//o;
                    my $tag  = $1;
                    my $line = $2;
                    my $prefix = "Method ";
                    my $suffix = " was never executed.";
                    if ( !defined( $tag ) )
                    {
                        if ( m/^\s*case\b/o )
                        {
                            $prefix = "Case ";
                            $tag    = "c";
                            $suffix = " was never executed.";
                        }
                        elsif ( m/^\s*catch\b/o )
                        {
                            $prefix = "Exception handler for ";
                            $tag    = "x";
                            $suffix = " was never executed.";
                        }
                        else
                        {
                            $prefix = "Expression ";
                            $tag    = "e";
                            $suffix = " was never evaluated.";
                        }
                    }
                    elsif ( $tag eq "T" )
                    {
                        $prefix = "Decision ";
                        $suffix = " was only evaluated true.";
                    }
                    elsif ( $tag eq "F" )
                    {
                        $prefix = "Decision ";
                        $suffix = " was only evaluated false.";
                    }
                    elsif ( $tag eq "t" )
                    {
                        $prefix = "Condition ";
                        $suffix = " was only evaluated true.";
                    }
                    else # ( $tag eq "f" )
                    {
                        $prefix = "Condition ";
                        $suffix = " was only evaluated false.";
                    }

                    if ( !defined $codeMessages{$thisFile} )
                    {
                        $codeMessages{$thisFile} = {};
                    }
                    if ( !defined $codeMessages{$thisFile}{$line} )
                    {
                        $codeMessages{$thisFile}{$line} = {
                            category => 'coverage',
                            coverage => $tag,
                            message  => $prefix
                                . extractEntity( $_, $tag eq "function" )
                                . $suffix
                            };
                    }
                    elsif ( $codeMessages{$thisFile}{$line}{coverage}
                            =~ m/function/o
                            && $tag eq "e" )
                    {
                        # don't re-enter the info for the function itself
                    }
                    else
                    {
                        $codeMessages{$thisFile}{$line}{coverage} .= $tag;
                        $codeMessages{$thisFile}{$line}{message} .= "  "
                            . $prefix
                            . extractEntity( $_ )
                            . $suffix;
                    }
                }
            }
            close( COVERAGE );
        }
    }
}

if ( $debug > 3 )
{
    print "\n\ncode messages:\n--------------------\n";
    foreach my $f ( keys %codeMessages )
    {
        print "$f:\n";
        foreach my $line ( keys %{ $codeMessages{$f} } )
        {
            print "    line $line:\n";
            foreach my $k ( keys %{ $codeMessages{$f}{$line} } )
            {
                print "        $k => ", $codeMessages{$f}{$line}{$k}, "\n";
            }
        }
    }
}


#=============================================================================
# generate HTML version of student testing results
#=============================================================================
if ( defined( $studentTestMsgs ) )
{
    my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $testLog );
    $feedbackGenerator->startFeedbackSection( $studentTestTitle );
    $feedbackGenerator->print( <<EOF );
<pre>
$studentTestMsgs
</pre>
EOF
    if ( $testsRun > 0 )
    {
        $feedbackGenerator->print( "<p><b>Test Pass Rate: " );
        if ( $studentCasesPercent < 100 )
        {
            $feedbackGenerator->print(
                "<font color=\"#ee00bb\">$studentCasesPercent%</font>" );
        }
        else
        {
            $feedbackGenerator->print( "$studentCasesPercent%" );
        }
        $feedbackGenerator->print( "</b></p>\n" );
    }
    if ( $coverageMetric && $gradedElements > 0 )
    {
        $feedbackGenerator->print( "<p><b>Code Coverage: " );
        if ( $codeCoveragePercent < 100 )
        {
            $feedbackGenerator->print(
                "<font color=\"#ee00bb\">$codeCoveragePercent%</font>" );
        }
        else
        {
            $feedbackGenerator->print( "$codeCoveragePercent%" );
        }
        my $descr =
            $cfg->getProperty( "statElementsLabel", "Methods Executed" );
        $descr =~ tr/A-Z/a-z/;
        $descr =~ s/\s*executed\s*$//;
        $feedbackGenerator->print(
            "</b> (percentage of $descr exercised by your tests)</p>\n" );
        $feedbackGenerator->print( <<EOF );
<p>You can improve your testing by looking for any
<span style="background-color:#F0C8C8">lines highlighted in this color</span>
in your code listings above.  Such lines have not been sufficiently
tested--hover your mouse over them to find out why.
</p>
EOF
    }
    $feedbackGenerator->endFeedbackSection;
    $feedbackGenerator->close;

    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $testLogRelative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html"      );
}

# Instructor's test log
# -----------
if ( -f $instrLog && stat( $instrLog )->size > 0 )
{
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $instrLogRelative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html"       );
}


if ( defined $status{'studentTestResults'}
     && $status{'studentTestResults'}->hasResults )
{
    $cfg->setProperty('student.test.results',
                      $status{'studentTestResults'}->plist);
    $cfg->setProperty('student.test.executed',
                      $status{'studentTestResults'}->testsExecuted);
    $cfg->setProperty('student.test.passed',
                      $status{'studentTestResults'}->testsExecuted
                      - $status{'studentTestResults'}->testsFailed);
    $cfg->setProperty('student.test.failed',
                      $status{'studentTestResults'}->testsFailed);
    $cfg->setProperty('student.test.passRate',
                      $status{'studentTestResults'}->testPassRate);
    $cfg->setProperty('student.test.allPass',
                      $status{'studentTestResults'}->allTestsPass);
    $cfg->setProperty('student.test.allFail',
                      $status{'studentTestResults'}->allTestsFail);
}
if ( defined $status{'instrTestResults'}
     && $status{'instrTestResults'}->hasResults )
{
    $cfg->setProperty('instructor.test.results',
                      $status{'instrTestResults'}->plist);
    $cfg->setProperty('instructor.test.executed',
                      $status{'instrTestResults'}->testsExecuted);
    $cfg->setProperty('instructor.test.passed',
                      $status{'instrTestResults'}->testsExecuted
                      - $status{'instrTestResults'}->testsFailed);
    $cfg->setProperty('instructor.test.failed',
                      $status{'instrTestResults'}->testsFailed);
    $cfg->setProperty('instructor.test.passRate',
                      $status{'instrTestResults'}->testPassRate);
    $cfg->setProperty('instructor.test.allPass',
                      $status{'instrTestResults'}->allTestsPass);
    $cfg->setProperty('instructor.test.allFail',
                      $status{'instrTestResults'}->allTestsFail);
}
if ( defined $status{'studentDerefereeStats'}
     && $status{'studentDerefereeStats'}->hasResults )
{
    $cfg->setProperty('student.memory.numLeaks',
                      $status{'studentDerefereeStats'}->numLeaks);
    $cfg->setProperty('student.memory.leakRate',
                      $status{'studentDerefereeStats'}->leakRate);
    $cfg->setProperty('student.memory.totalBytesAllocated',
                      $status{'studentDerefereeStats'}->totalMemoryAllocated);
    $cfg->setProperty('student.memory.maxBytesInUse',
                      $status{'studentDerefereeStats'}->maxMemoryInUse);
    $cfg->setProperty('student.memory.numCallsToNew',
                      $status{'studentDerefereeStats'}->numCallsToNew);
    $cfg->setProperty('student.memory.numCallsToDelete',
                      $status{'studentDerefereeStats'}->numCallsToDelete);
    $cfg->setProperty('student.memory.numCallsToArrayNew',
                      $status{'studentDerefereeStats'}->numCallsToArrayNew);
    $cfg->setProperty('student.memory.numCallsToArrayDelete',
                      $status{'studentDerefereeStats'}->numCallsToArrayDelete);
    $cfg->setProperty('student.memory.numCallsToDeleteNull',
                      $status{'studentDerefereeStats'}->numCallsToDeleteNull);
}
if ( defined $status{'instrDerefereeStats'}
     && $status{'instrDerefereeStats'}->hasResults )
{
    $cfg->setProperty('instructor.memory.numLeaks',
                      $status{'instrDerefereeStats'}->numLeaks);
    $cfg->setProperty('instructor.memory.leakRate',
                      $status{'instrDerefereeStats'}->leakRate);
    $cfg->setProperty('instructor.memory.totalBytesAllocated',
                      $status{'instrDerefereeStats'}->totalMemoryAllocated);
    $cfg->setProperty('instructor.memory.maxBytesInUse',
                      $status{'instrDerefereeStats'}->maxMemoryInUse);
    $cfg->setProperty('instructor.memory.numCallsToNew',
                      $status{'instrDerefereeStats'}->numCallsToNew);
    $cfg->setProperty('instructor.memory.numCallsToDelete',
                      $status{'instrDerefereeStats'}->numCallsToDelete);
    $cfg->setProperty('instructor.memory.numCallsToArrayNew',
                      $status{'instrDerefereeStats'}->numCallsToArrayNew);
    $cfg->setProperty('instructor.memory.numCallsToArrayDelete',
                      $status{'instrDerefereeStats'}->numCallsToArrayDelete);
    $cfg->setProperty('instructor.memory.numCallsToDeleteNull',
                      $status{'instrDerefereeStats'}->numCallsToDeleteNull);
}

$cfg->setProperty('outcomeProperties',
                  '("instructor.test.results", "student.test.results")');


#=============================================================================
# generate score explanation for student
#=============================================================================
if ( $can_proceed )
{
    my $scoreToTenths = int( $runtimeScore * 10 + 0.5 ) / 10;
    if ( $instructorTestsRun == 0
         || $instructorTestsFailed == $instructorTestsRun )
    {
        $instructorCasesPercent = "<font color=\"#ee00bb\">unknown</font>";
    }
    else
    {
        $instructorCasesPercent = "$instructorCasesPercent%";
    }
    my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $explanation );
    $feedbackGenerator->startFeedbackSection( "Interpreting Your Score" );
    $feedbackGenerator->print( <<EOF );
<p>Your score is based on the following factors (shown here rounded to
the nearest percent):</p>
<table style="border:none">
<tr><td><b>Test Pass Rate:</b></td><td class="n">$studentCasesPercent%</td>
<td>(how many of your tests pass)</td></tr>
EOF
    if ( $coverageMetric )
    {
        $feedbackGenerator->print( <<EOF );
<tr><td><b>Code Coverage:</b></td><td class="n">$codeCoveragePercent%</td>
<td>(how much of your code is exercised by your tests)</td></tr>
EOF
    }
    $feedbackGenerator->print( <<EOF );
<tr><td><b>Problem Coverage:</b></td>
<td class="n">$instructorCasesPercent</td>
<td>(how much of the problem your solution/tests cover)</td></tr>
</table>
<p>Your Correctness/Testing score is calculated this way:</p>
<p>score = $maxCorrectnessScore * $studentCasesPercent%
EOF
    if ( $coverageMetric )
    {
        $feedbackGenerator->print( " * $codeCoveragePercent% " );
    }
    $feedbackGenerator->print( <<EOF );
* $instructorCasesPercent = $scoreToTenths</p>
<p>Note that full-precision (unrounded) percentages are used to calculate
your score.</p>
EOF
    $feedbackGenerator->endFeedbackSection();
    $feedbackGenerator->close;
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",    $explanationRelative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html"         );
}


#=============================================================================
# generate HTML versions of source files
#=============================================================================

my $beautifier = new Web_CAT::Beautifier;
$beautifier->beautifyCwd( $cfg,
                          [ 'runAllTests.cpp' ],
                          \%codeMarkupIds,
                          \%codeMessages );


#=============================================================================
# Use CLOC to calculate lines of code statistics
#=============================================================================

my @cloc_files = ();
my $numCodeMarkups = $cfg->getProperty( 'numCodeMarkups', 0 );

for (my $i = 1; $i <= $numCodeMarkups; $i++)
{
    my $cloc_file = $cfg->getProperty(
        "codeMarkup${i}.sourceFileName", undef);

    push @cloc_files, $cloc_file if defined $cloc_file;
}

print "Passing these files to CLOC: @cloc_files\n" if ( $debug > 2 );

if (@cloc_files)
{
    my $cloc = new Web_CAT::CLOC;
    $cloc->execute(@cloc_files);

    for (my $i = 1; $i <= $numCodeMarkups; $i++)
    {
        my $cloc_file = $cfg->getProperty(
            "codeMarkup${i}.sourceFileName", undef);

        my $cloc_metrics = $cloc->fileMetrics($cloc_file);
        next unless defined $cloc_metrics;

        $cfg->setProperty(
            "codeMarkup${i}.loc",
            $cloc_metrics->{blank} + $cloc_metrics->{comment} + $cloc_metrics->{code} );
        $cfg->setProperty(
            "codeMarkup${i}.ncloc",
            $cloc_metrics->{blank} + $cloc_metrics->{code} );
    }
}

#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================

# Script log
# ----------
if ( -f $scriptLog && stat( $scriptLog )->size > 0 )
{
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $scriptLogRelative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/plain"       );
    $cfg->setProperty( "report${reportCount}.to",       "admin"            );
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $antLogRelative    );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/plain"       );
    $cfg->setProperty( "report${reportCount}.to",       "admin"            );
}

$cfg->setProperty( "numReports",        $reportCount  );
$cfg->setProperty( "score.correctness", $runtimeScore );
$cfg->setProperty( "score.tools",       $staticScore  );
$cfg->save();

if ( $debug )
{
    my $lasttime = time;
    print "\n", ( $lasttime - $time1 ), " seconds total\n";
    print "\nFinal properties:\n-----------------\n";
    my $props = $cfg->getProperties();
    while ( ( my $key, my $value ) = each %{$props} )
    {
        print $key, " => ", $value, "\n";
    }
}


#-----------------------------------------------------------------------------
exit( 0 );
#-----------------------------------------------------------------------------
