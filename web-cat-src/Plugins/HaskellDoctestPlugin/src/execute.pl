#!/usr/bin/perl -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.2 2011/10/25 05:07:52 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Grader: plug-in for Python submissions
#=============================================================================
# Installation Notes:
# In the neighborhood of line 165, there are several python system-specific
# path settings that must be set to have the plugin work.

use strict;
use Config::Properties::Simple;
use File::Basename;
use File::Copy;
use File::Spec;
use File::stat;
use Proc::Background;
use Web_CAT::Beautifier;    ## Soon, I hope. -sb
use Web_CAT::FeedbackGenerator;
use Web_CAT::Utilities
    qw(confirmExists filePattern copyHere htmlEscape addReportFile scanTo
       scanThrough linesFromFile addReportFileWithStyle);
use XML::Smart;

my @beautifierIgnoreFiles = ('.o', '.hi', '.tix', '.mix', '.hpc');


#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
#  Notes:
#  -- $scriptData is root directory for instructor assignment files.
#     E.g., "/var/WebCAT.data/UserScriptsData". Assignment-specific file
#     information (instructor scripts, input files) and at locations specified
#     as relative paths in the assignment options. The assignment options are
#     appended to $scriptData to obtain the specific directory/file values.
#  -- $instructorUnitTest is relative path to instructor test[s].
#  -- $localFiles is relative path to files to be copied over to the
#     $scriptData. I.e., "$scriptData/$localFile".
#     E.g., "UOM/sbrandle/CSE101/Python_ReadFile
#  -- $log_dir is where results files get placed
#     E.g., "WebCAT.data/?/SchoolName/..."
#  -- $script_home is where plugin files are.
#     E.g., config.plist, execute.pl
#  -- $working_dir is Tomcat temporary working dir
#     E.g., "/usr/local/tomcat5.5/temp/UOM/submitterWeb-CATName"

our $propfile     = $ARGV[0];   # property file name
our $cfg          = Config::Properties::Simple->new( file => $propfile );

our $localFiles   = $cfg->getProperty( 'localFiles', '' );
our $log_dir      = $cfg->getProperty( 'resultDir'      );
our $script_home  = $cfg->getProperty( 'scriptHome'     );
our $working_dir  = $cfg->getProperty( 'workingDir'     );

our $timeout      = $cfg->getProperty( 'timeout', 30    );
# The values coming through don't match up with assignment settings.
# E.g., "15" comes through as "430". So this is a 'temporary' patch.
# And I can't access the timeoutInternalPadding, etc. from config.plist, so
# have to guess as to the adjustment to undo the padding and multiplying done
# by the subsystem..
if ( $timeout >  100 ) { $timeout = ($timeout - 400) / 2; }
if ( $timeout <  2 ) { $timeout = 15; }

#-------------------------------------------------------
# Scoring Settings
#-------------------------------------------------------
#   Notes:
#   -- coverageMetric is Boolean for now. May mess with degree of coverage later.
#   -- allStudentTestsMustPass has apparently had many types of input. Swiped
#      the input tests from C++ tester.
our $maxCorrectnessScore     = $cfg->getProperty( 'max.score.correctness', 0 );
our $enableStudentTests      = $cfg->getProperty( 'enableStudentTests', 0 );
our $doStudentTests          =
    ( $enableStudentTests      =~ m/^(true|on|yes|y|1)$/i );
our $measureCodeCoverage     = $cfg->getProperty( 'coverageMetric', 0 );
our $doMeasureCodeCoverage   =
    ( $measureCodeCoverage     =~ m/^(true|on|yes|y|1)$/i );
our $allStudentTestsMustPass = $cfg->getProperty( 'allStudentTestsMustPass', 0 );
    $allStudentTestsMustPass =
    ( $allStudentTestsMustPass =~ m/^(true|on|yes|y|1)$/i );
#   Keep directives in agreement.
    $doStudentTests = 1 if $doMeasureCodeCoverage || $allStudentTestsMustPass;

#-------------------------------------------------------
#   Feedback Settings
#-------------------------------------------------------
our $hintsLimit              = $cfg->getProperty( 'hintsLimit', 3 );
our $showHints               = $cfg->getProperty( 'showHints', 0 );
    $showHints               =
    ( $showHints             =~ m/^(true|on|yes|y|1)$/i );
# Remove the 4 "$showExtraFeedback" lines below after May, 2009
# The $showExtraFeedback is temporarily a synonym for $showHints until
# after the semester ends so as not to break deployed assignments.
our $showExtraFeedback       = $cfg->getProperty( 'showExtraFeedback', 0 );
    $showExtraFeedback       =
    ( $showExtraFeedback     =~ m/^(true|on|yes|y|1)$/i );
    $showHints = $showHints || $showExtraFeedback;

our $hideHintsWithin         = $cfg->getProperty( 'hideHintsWithin', 0 );
our $dueDateTimestamp        = $cfg->getProperty( 'dueDateTimestamp', 0 );
our $submissionTimestamp     = $cfg->getProperty( 'submissionTimestamp', 0 );
#   Adjust time from milliseconds to seconds
    $dueDateTimestamp    /= 1000;
    $submissionTimestamp /= 1000;
#   Within extra feedback blackout period if
#   (1) hideHintsWithin != 0 (covered by multiplication below), and
#   (2) submission time >= (due date - hideHintsWithin time)
#   else will provide extra feedback if requested.
#   The reason for the extra variable is to be able to generate a message
#   to the effect that extra help would have been available if the student
#   had submitted earlier.
our $hintsBlackout   =
    int ($submissionTimestamp + $hideHintsWithin * 3600 * 24 >= $dueDateTimestamp);
#   Turn extra feedback off within extra feedback blackout period.
    $showHints       = $showHints && !$hintsBlackout;

#-------------------------------------------------------
#   Language (Python) Settings
#-------------------------------------------------------
#   -- None at present
#   Script Developer Settings
our $debug                   = $cfg->getProperty( 'debug', 0 );

our $NTprojdir               = $working_dir . "/";

#   Considering borrowing this from C++ grader, but not currently active.
#our %status = (
#    'studentHasSrcs'     => 0,
#    'studentTestResults' => undef,
#    'instrTestResults'   => undef,
#    'toolDeductions'     => 0,
#    'compileMsgs'        => "",
#    'compileErrs'        => 0,
#    'feedback'           => undef,
#        #new Web_CAT::FeedbackGenerator( $log_dir, 'feedback.html' ),
#    'instrFeedback'      => undef,
#        #new Web_CAT::FeedbackGenerator( $log_dir, 'staffFeedback.html' )
#);

#-------------------------------------------------------
#   Local file location definitions within this script
#-------------------------------------------------------
if ( ! defined $log_dir )    { print "log_dir undefined"; }
our $script_log_relative      = "script.log";
our $script_log               = File::Spec->join($log_dir, $script_log_relative);
if ( ! defined $script_log ) { print "script_log undefined"; }
our $student_code_relative    = "student-code.txt";
our $student_code             = File::Spec->join($log_dir, $student_code_relative);

our $instr_output_relative    = "instructor-unittest-out.txt";
our $instr_output             = File::Spec->join($log_dir, $instr_output_relative);
our $instr_rpt_relative       = "instr-unittest-report.html";
our $instr_rpt                = File::Spec->join($log_dir, $instr_rpt_relative);

our $student_output_relative  = "student-unittest-out.txt";
our $student_output           = File::Spec->join($log_dir, $student_output_relative);
our $student_rpt_relative     = "student-unittest-report.html";
our $student_rpt              = File::Spec->join($log_dir, $student_rpt_relative);

# Will append a "-moduleName.txt" to each file. Doesn't exactly
# follow approach of other report files, but seemed better this way.
our $coverage_output_relative = "student-coverage";
our $coverage_output          = File::Spec->join($log_dir, $coverage_output_relative);
our $coverage_rpt_relative    = "student-coverage-report.html";
our $coverage_rpt             = File::Spec->join($log_dir, $coverage_rpt_relative);
our $covfileRelative          = "test.cov";

our $timeout_log_relative     = "timeout_log.txt";
our $timeout_log              = File::Spec->join($log_dir, 'timeout_log');
our $debug_log                = File::Spec->join($log_dir, 'debug.txt');
our $testLogRelative          = "justTesting.log";
our $testLog                  = File::Spec->join($log_dir, $testLogRelative);

sub platformPath
{
    my $path = shift;
    my $sep = $Web_CAT::Utilities::FILE_SEPARATOR;
    if ($sep eq '/')
    {
        $path =~ s,\\,$sep,g;
    }
    else
    {
        $path =~ s,/,$sep,g;
    }
    return $path;
}

my $antLog              = platformPath("$log_dir/ant.log");

die "ANT_HOME environment variable is not set! (Should come from ANTForPlugins)"
    if !defined($ENV{ANT_HOME});
$ENV{PATH} =
    "$ENV{ANT_HOME}" . $Web_CAT::Utilities::FILE_SEPARATOR . "bin"
    . $Web_CAT::Utilities::PATH_SEPARATOR . $ENV{PATH};

my $ANT = "ant";


our $explain_rpt_relative     = "explanation_report.html";
our $explain_rpt              = File::Spec->join($log_dir, $explain_rpt_relative);

our $stdinInput               = File::Spec->devnull();

#-------------------------------------------------------
#   Other local variables within this script
#-------------------------------------------------------
our $student_src      = "";
our $can_proceed      = 1;
our $timeout_occurred = 0;
our $version          = "0.6";
our $delete_temps     = 0;    # Change to 0 to preserve temp files
our $studentTestMsgs  = "";
our $expSectionId     = 0;    # For the expandable sections

my $haskellExtraPath  = $cfg->getProperty('haskellPath');
if (defined $haskellExtraPath && $haskellExtraPath ne "")
{
    $ENV{'PATH'} =
        $haskellExtraPath . $Web_CAT::Utilities::PATH_SEPARATOR . $ENV{'PATH'};
}


#=============================================================================
# Locate instructor unit test implementation
#=============================================================================
my $scriptData = $cfg->getProperty( 'scriptData', '.' );

$scriptData =~ s,/$,,;

## Should be renamed to "generateFullScriptPath"?
##
sub findScriptPath
{
    my $subpath = shift;
    my $target = File::Spec->join($scriptData, $subpath);
    if (-e $target)
    {
        return $target;
    }
    die "cannot find user script data file $subpath in $scriptData";
}


# Instructor reference solution
my $instrSrcPath;
{
    my $instructorSolution = $cfg->getProperty('instructorSolution');
    if (defined $instructorSolution && $instructorSolution ne "")
    {
        my $instrSrc = findScriptPath($instructorSolution);
        if (-d $instrSrc)
        {
            $instrSrcPath = $instrSrc;
        }
        else
        {
            $instrSrcPath = dirname($instrSrc);
            $cfg->setProperty(
                'instructorSolutionPattern', basename($instrSrc));
        }
        $cfg->setProperty('instructorSolutionPath', $instrSrcPath);
    }
    else
    {
        die "Instructor reference solution not set.";
    }
}


#=============================================================================
#   Script Startup
#=============================================================================
#   Change to specified working directory and set up log directory
chdir( $working_dir );

## PATCH FOR INTERNET EXPLORER PATH NAME PROBLEM.
## Go through all files in working dir and rename if necessary.
while (<*>) {
    my $file = $_;
    if ( $file =~ /^[A-Z]:\\.*\.hs$/ ) {
        my $newName = $file;
        $newName =~ tr/\\/\//;
        $newName = basename( $newName );
        print "rename MS-Windows file upload file name $file to $newName\n";
        rename $file, $newName || die "Error renaming $file to $newName\n";
    }
}

#   Try to deduce whether or not there is an extra level of subdirs
#   around this assignment.
{
    # Get a listing of all file/dir names, including those starting with
    # dot, then strip out . and ..
    my @dirContents = grep(!/^(\.{1,2}|META-INF)$/, <* .*> );

    # if this list contains only one entry that is a dir name != src, then
    # assume that the submission has been "wrapped" with an outter
    # dir that isn't actually part of the project structure.
    if ( $#dirContents == 0 && -d $dirContents[0] && $dirContents[0] ne "src" )
    {
        # Strip non-alphanumeric symbols from dir name
        my $dir = $dirContents[0];
        if ( $dir =~ s/[^a-zA-Z0-9_]//g )
        {
            if ( $dir eq "" )
            {
                $dir = "dir";
            }
            rename( $dirContents[0], $dir );
        }
        $working_dir .= "/$dir";
        chdir( $working_dir );
    }
}

if ($debug)
{
    print "working dir set to $working_dir\n";
}


#-----------------------------------------------
# Copy over input/output data files as necessary
# localFiles
{
    my $localFiles = $cfg->getProperty( 'localFiles' );
    if ( defined $localFiles && $localFiles ne "" )
    {
        my $lf = confirmExists( $scriptData, $localFiles );
        print "localFiles = $lf\n" if $debug;
        if ( -d $lf )
        {
            print "localFiles is a directory\n" if $debug;
            copyHere( $lf, $lf, \@beautifierIgnoreFiles );
        }
        else
        {
            print "localFiles is a single file\n" if $debug;
            my $base = $lf;
            $base =~ s,/[^/]*$,,;
            copyHere( $lf, $base, \@beautifierIgnoreFiles );
        }
    }
}


#-----------------------------------------------
# Generate a script warning
sub adminLog {
    print "script_log undefined" if ! defined $script_log;
    open( SCRIPTLOG, ">>$script_log" ) ||
        die "Cannot open file for output '$script_log': $!";
    print SCRIPTLOG join( "\n", @_ ), "\n";
    close( SCRIPTLOG );
}

sub studentLog
{
    open( SCRIPT_LOG, ">>$script_log" ) ||
    die "cannot open $script_log: $!";
    print SCRIPT_LOG @_;
    close( SCRIPT_LOG );
}

#-----------------------------------------------
# Prints out an official error report to screen where it might be
# more helpful than saying "There has been an internal error", etc.
sub reportError {
    my $rpt_absolute_path = shift;
    my $rpt_relative_path = shift;
    my $rpt_title         = shift;
    my $rpt_message       = shift;

    my $errorFeedbackGenerator =
        new Web_CAT::FeedbackGenerator( $rpt_absolute_path );
    $errorFeedbackGenerator->startFeedbackSection(
             $rpt_title,
             ++$expSectionId,
             0 );
    if( ! defined $rpt_message || $rpt_message eq "" )
    {
        # No message passed in, so grab the script log.
        # Don't know if this is good programming style, but about to 'slurp'
        # entire file.
        # Something really blew up, so printint everything in $script_log
        # to screen report.
        open( SCRIPT_LOG, "$script_log" ) ||
            die "cannot open $script_log: $!";
        my $holdTerminator = $/;
        undef $/;
        $rpt_message = <SCRIPT_LOG>;
        close( SCRIPT_LOG );
        $/ = $holdTerminator;
    }
    $errorFeedbackGenerator->print( $rpt_message );
    $errorFeedbackGenerator->endFeedbackSection;

    # Close down this report
    $errorFeedbackGenerator->close;
    addReportFileWithStyle($cfg, $rpt_relative_path, 'text/html', 1);
}


#=============================================================================
# Try doctest
#=============================================================================

$cfg->save();
if ( $debug > 2 ) { $ANT .= " -d -v"; }
my $cmdline = $Web_CAT::Utilities::SHELL
    . "$ANT -f \"$script_home/build.xml\" -l \"$antLog\" "
    . "-propertyfile \"$propfile\" \"-Dbasedir=$working_dir\" "
    . "> " . File::Spec->devnull . " 2>&1";

print $cmdline, "\n" if ($debug);
my ($exitcode, $timeout_status) = Proc::Background::timeout_system(
    $timeout, $cmdline);
if ($timeout_status)
{
    die "timeout!\n";
}

sub filePat
{
    my $path = shift || die "filePat: path required";
    $path =~ s,\\+,/,go;
    $path =~ s,//+,/,go;
    my $dosPath = $path;
    $dosPath =~ s,/,\\,go;
    my $dDosPath = $dosPath;
    $dDosPath =~ s,\\,\\\\,go;
    return qr/\Q$path\E|\Q$dosPath\E|\Q$dDosPath\E/;
}


my $workingDirPat = filePat($working_dir . "/");
my $testDirPat = filePat($instrSrcPath . "/");
my $studentDirPat = filePat($log_dir . "/sbin/");
my $instructorDirPat = filePat($log_dir . "/ibin/");
my $resultDirPat = filePat($log_dir . "/");
my $reportCount = $cfg->getProperty('numReports', 0);


sub addReport
{
    my $infile  = shift;
    my $outfile = shift;
    my $title   = shift;
    my $hide    = shift || 0;

    my @result = (0, 0, 0, 0);

    if (-f $infile && open(LOG, $infile))
    {
        my @logLines = <LOG>;
        close(LOG);
        if ($#logLines >= 0)
        {
            my $feedbackGenerator = new Web_CAT::FeedbackGenerator("$log_dir/$outfile");
            $feedbackGenerator->startFeedbackSection($title, 1);
            $feedbackGenerator->print("<pre>\n");
            foreach my $line (@logLines)
            {
                if ($line =~ m/Cases:\s*([0-9]+)\s+Tried:\s*([0-9]+)\s+Errors:\s*([0-9]+)\s+Failures:\s*([0-9]+)/o)
                {
                    @result = ($1, $2, $3, $4);
                }

                if ($hide)
                {
                    $line =~ s/but got: \[".*"\]/but got: <<Something different!>>/o;
                }

                $line =~ s/$workingDirPat//g;
                $line =~ s/$testDirPat//g;
                $line =~ s/$studentDirPat//g;
                $line =~ s/$instructorDirPat//g;
                $line =~ s/$resultDirPat//g;
                $line =~ s/\&/\&amp;/g;
                $line =~ s/</\&lt;/g;
                $line =~ s/>/\&gt;/g;
                $feedbackGenerator->print($line);
            }
            $feedbackGenerator->print("</pre>\n");
            $feedbackGenerator->endFeedbackSection;
            $feedbackGenerator->close;
            $reportCount++;
            $cfg->setProperty("report${reportCount}.file", $outfile);
            $cfg->setProperty("report${reportCount}.mimeType", "text/html");
            $cfg->setProperty("report${reportCount}.styleVersion", 2);
        }
    }

    return @result;
}

addReport("$log_dir/sbin/compiler.log", "compiler.html", "Compiler Messages");
addReport(
    "$log_dir/sbin/bad-tests.log",
    "student-invalid-tests.html",
    "Some Test Cases Could Not Be Compiled");
my ($sCases, $sTried, $sErrors, $sFails) = addReport(
    "$log_dir/student-out.txt",
    "student-out.html",
    "Program Correctness (Results from Your Tests on Your Code)");
my $sSucceeded =  $sTried - $sErrors - $sFails;
my $sPct = $sSucceeded / (($sCases > 0) ? $sCases : 1);
$cfg->setProperty( "studentEval", "$sPct $sSucceeded $sCases");
addReport(
    "$log_dir/ibin/bad-tests.log",
    "ref-invalid-tests.html",
    "Some Test Cases Were Incompatible with the Reference Solution");
if ($sSucceeded == $sCases && $sCases > 0)
{
    my ($iCases, $iTried, $iErrors, $iFails) = addReport(
        platformPath("$log_dir/instructor-out.txt"),
        "instructor-out.html",
        "Test Validity (Results from Your Tests on the Reference Solution)", 1);
    my $iSucceeded =  $iTried - $iErrors - $iFails;
    my $iPct = $iSucceeded / (($iCases > 0) ? $iCases : 1);
    $cfg->setProperty( "instructorEval", "$iPct $iSucceeded $iCases");
}


sub extractCvgFrom
{
    my $item     = shift;
    my $propName = shift;

    my $p = $item->{'boxes'};
    my $exceptions = 0;
    if (defined $propName)
    {
        $exceptions = $cfg->getProperty($propName, 0);
    }
    my $c = $item->{'count'};
    if (exists($item->{'true'}))
    {
        $p *= 2;
        $c *= 2;
        $c -= $item->{'true'};
        $c -= $item->{'false'};
    }
    $p -= $exceptions;
    if ($c > $p)
    {
        $c = $p;
    }
    return ($c, $p);
}

my $cvgFile = "$log_dir/instructor-cvg.xml";
if (-f $cvgFile && $sSucceeded == $sCases && $sCases > 0)
{
    my $possible = 0;
    my $covered  = 0;
    my $cvg = XML::Smart->new($cvgFile);

    # <coverage name="driver.exe.tix">
    #   ...
    #   <summary>
    #     <exprs boxes="227" count="68"/>
    #     <booleans boxes="6" true="1" false="0" count="3"/>
    #     <guards boxes="2" true="1" false="0" count="2"/>
    #     <conditionals boxes="4" true="0" false="0" count="1"/>
    #     <qualifiers boxes="0" true="0" false="0" count="0"/>
    #     <alts boxes="55" count="13"/>
    #     <local boxes="7" count="2"/>
    #     <toplevel boxes="29" count="11"/>
    #   </summary>
    # </coverage>

    # Expressions
    my ($c, $p) = extractCvgFrom($cvg->{'coverage'}->{'summary'}->{'exprs'},
        'allowUncoveredRefExprs');
    $covered  += $c;
    $possible += $p;

    # Booleans (includes guards + conditionals?)
    ($c, $p) = extractCvgFrom($cvg->{'coverage'}->{'summary'}->{'booleans'},
        'allowUncoveredRefBooleans');
    $covered  += $c;
    $possible += $p;

    # Qualifiers
    ($c, $p) = extractCvgFrom($cvg->{'coverage'}->{'summary'}->{'qualifiers'},
        'allowUncoveredRefQualifiers');
    $covered  += $c;
    $possible += $p;

    # Alternatives
    ($c, $p) = extractCvgFrom($cvg->{'coverage'}->{'summary'}->{'alts'},
        'allowUncoveredRefAlternatives');
    $covered  += $c;
    $possible += $p;

    # Locals
    ($c, $p) = extractCvgFrom($cvg->{'coverage'}->{'summary'}->{'local'},
        'allowUncoveredRefLocals');
    $covered  += $c;
    $possible += $p;

    if ($possible == 0)
    {
        $possible = 1;
    }

    my $pct = $covered / $possible;

    $cfg->setProperty("coverage", "$pct $covered $possible");
}
else
{
    $cfg->setProperty("coverage", "0 0 1");
}


#=============================================================================
# Gather coverage data from student source files
#=============================================================================

my %codeMessages = ();

sub collectMessages
{
    my $prefix = shift;
    my $path   = shift;

    if (! -e $path)
    {
        return;
    }

    if (-d $path)
    {
        for my $subpath (<$path/*>)
        {
            collectMessages($prefix, $subpath);
        }
    }
    elsif ($path !~ m,hpc_index[^\./]*\.html,io)
    {
        my $fileName = $path;
        $fileName =~ s,^\Q$prefix/\E,,;
        $fileName =~ s,\.html$,,;
        print "processing $fileName\n" if ($debug);

    if (open(MARKUP, $path))
    {
        while (<MARKUP>)
        {
            # isolate the source line number
            my $lineno = 0;
            if (m,<span class="lineno">\s*([0-9]*)\s*</span>,io)
            {
                $lineno = $1;
            }

                # now look for a highlighted expression
            if (m,<span class="(nottickedoff|tickonlytrue|tickonlyfalse)">\s*([^<\s][^<]*)\s*</span>,io)
            {
                my $matchType = $1;
                my $phrase    = $2;

                    if ($matchType eq "tickonlytrue" && $phrase eq "otherwise")
                    {
                        $matchType = undef;
                        $phrase = undef;
                    if (m,<span class="tickonlytrue">\s*otherwise\s*</span>.*<span class="(nottickedoff|tickedonlytrue|tickedonlyfalse)">\s*([^<\s][^<]*)\s*</span>,io)
                        {
                        my $matchType = $1;
                        my $phrase    = $2;
                        }
                    }

                if (defined $matchType)
                {
                    $phrase =~ s,\s+$,,o;
                    my $msg =
                        "Expression \"$phrase\" was never evaluated.";
                    my $tag = "e";
                    if ($matchType eq "tickonlytrue")
                    {
                        $msg = "Condition \"$phrase\" always evaluated "
                            . "to True.";
                        $tag = "t";
                    }
                    elsif ($matchType eq "tickonlyfalse")
                    {
                        $msg = "Condition \"$phrase\" always evaluated "
                            . "to False.";
                        $tag = "f";
                    }
                    $codeMessages{$fileName}{$lineno} = {
                            category => 'coverage',
                            coverage => $tag,
                            message  => htmlEscape($msg)
                            };
            }
            }
        }
        close(MARKUP);
    }
    else
    {
        print STDERR "error: unable to open $path for reading: $!\n";
    }
    }
}


collectMessages("$log_dir/scvghtml", "$log_dir/scvghtml");


if ( $debug > 3 )
{
    foreach my $f (keys %codeMessages)
    {
        print "$f:\n";
        foreach my $line (keys %{$codeMessages{$f}})
        {
            print "    line $line:\n";
            foreach my $k (keys %{$codeMessages{$f}{$line}})
            {
                print "        $k => ", $codeMessages{$f}{$line}{$k}, "\n";
            }
        }
    }
}


#=============================================================================
# generate HTML versions of any other source files
#=============================================================================
$cfg->setProperty("numReports", $reportCount);
my $beautifier = new Web_CAT::Beautifier;
$beautifier->beautifyCwd($cfg, \@beautifierIgnoreFiles, {}, \%codeMessages);


#=============================================================================
# Scale to be out of range of $maxCorrectnessScore
# $cfg->setProperty("score.correctness", $maxCorrectnessScore);
$cfg->save();

if ($debug)
{
    my $props = $cfg->getProperties();
    while ((my $key, my $value) = each %{$props})
    {
        print $key, " => ", $value, "\n";
    }
}

#-----------------------------------------------------------------------------
exit( 0 );
