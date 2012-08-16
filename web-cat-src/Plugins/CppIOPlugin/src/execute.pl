#!/usr/bin/perl -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.2 2011/10/25 05:05:24 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Grader: plug-in for C++ I/O testing submissions
#=============================================================================
# Installation Notes:

use strict;
use Config::Properties::Simple;
use File::Basename;
use File::Copy;
use File::stat;
use Proc::Background;
use Web_CAT::FeedbackGenerator;
use Web_CAT::Utilities
    qw( confirmExists filePattern copyHere htmlEscape addReportFile scanTo
        scanThrough linesFromFile );

my @beautifierIgnoreFiles = ( '.txt' );

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
#     working directory. The full path is obtained by appending this to
#     $scriptData. I.e., "$scriptData/$localFile".
#     E.g., "UOM/sbrandle/CSE101/Python_ReadFile
#  -- $log_dir is where results files get placed
#     E.g., "WebCAT.data/?/SchoolName/..."
#  -- $script_home is where plugin files are.
#     E.g., config.plist, execute.pl
#  -- $working_dir is Tomcat temporary working dir
#     E.g., "/usr/local/tomcat5.5/temp/UOM/submitterWeb-CATName"

our $propfile     = $ARGV[0];	# property file name
our $cfg          = Config::Properties::Simple->new( file => $propfile );

our $scriptData   = $cfg->getProperty( 'scriptData', '.' );
$scriptData =~ s,/$,,;
our $localFiles   = $cfg->getProperty( 'localFiles', '' );
our $log_dir	  = $cfg->getProperty( 'resultDir'      );
our $script_home  = $cfg->getProperty( 'scriptHome'     );
our $working_dir  = $cfg->getProperty( 'workingDir'     );
our $timeout	  = $cfg->getProperty( 'timeout', 30    );
# The values coming through don't match up with assignment settings.
# E.g., "15" comes through as "430". So this is a 'temporary' patch.
# And I can't access the timeoutInternalPadding, etc. from config.plist, so
# have to guess as to the adjustment to undo the padding and multiplying done
# by the subsystem..
if ( $timeout >  100 ) { $timeout = ($timeout - 400) / 2; }
if ( $timeout <  2 ) { $timeout = 15; }
our $reportCount  = $cfg->getProperty( 'numReports', 0  );

#-------------------------------------------------------
# Scoring Settings
#-------------------------------------------------------
#   Notes:
#   -- allStudentTestsMustPass has apparently had many types of input. Swiped
#      the input tests from C++ tester.
our $maxCorrectnessScore     = $cfg->getProperty( 'max.score.correctness', 0 );
our $maxToolScore            = $cfg->getProperty( 'max.score.tools', 0 );
our $ignoreWSDiff            = $cfg->getProperty( 'ignoreWhiteSpaceDifferences', 0 );
    $ignoreWSDiff        = ( $ignoreWSDiff =~ m/^(true|on|yes|y|1)$/i );
our $ignoreBlankLineDiff     = $cfg->getProperty( 'ignoreBlankLineDifferences', 0 );
    $ignoreBlankLineDiff = ( $ignoreBlankLineDiff =~ m/^(true|on|yes|y|1)$/i );
our $ignoreCaseDiff          = $cfg->getProperty( 'ignoreCaseDifferences', 0 );
    $ignoreCaseDiff      = ( $ignoreCaseDiff =~ m/^(true|on|yes|y|1)$/i );

#-------------------------------------------------------
#   Feedback Settings
#-------------------------------------------------------
our $showHints               = $cfg->getProperty( 'showHints', 0 );
    $showHints           =
    ( $showHints         =~ m/^(true|on|yes|y|1)$/i );

our $hintsFile               = $cfg->getProperty( 'hintsFile'     );
if( (defined $hintsFile ) && -e "$scriptData/$hintsFile" ) {
    $hintsFile = "$scriptData/$hintsFile";
}
else {
    $showHints = 0;
    print "Hints: script thinks that \$hintsFile isn't defined or doesn't exist.\n"
        . "Turning hints off.\n";;
}
#print "\$hintsFile = $hintsFile.\n" unless (! defined $hintsFile);

our $hintsLimit              = $cfg->getProperty( 'hintsLimit', 1 );
our $hideHintsWithin         = $cfg->getProperty( 'hideHintsWithin', 0 );
our $dueDateTimestamp        = $cfg->getProperty( 'dueDateTimestamp', 0 );
our $submissionTimestamp     = $cfg->getProperty( 'submissionTimestamp', 0 );
#   Adjust time from milliseconds to seconds
    $dueDateTimestamp    /= 1000;
    $submissionTimestamp /= 1000;
#   Within hints blackout period if
#   (1) hideHintsWithin != 0 (covered by multiplication below), and
#   (2) submission time >= (due date - hideHintsWithin time)
#   else will provide hints if requested.
#   The reason for the extra variable is to be able to generate a message
#   to the effect that extra help would have been available if the student
#   had submitted earlier.
our $hintsBlackout   =
    int ($submissionTimestamp + $hideHintsWithin * 3600 * 24 >= $dueDateTimestamp);
#   Turn hints off within hints blackout period.
    $showHints       = $showHints && !$hintsBlackout;


#-------------------------------------------------------
#   Language (C++) Settings
#-------------------------------------------------------
#   -- None at present
#   Script Developer Settings
our $debug                   = $cfg->getProperty( 'debug', 0 );
our $NTprojdir               = $working_dir . "/";
our $acceptCSource           = $cfg->getProperty( 'acceptCSource', 1 );
    $acceptCSource           =
    ( $acceptCSource         =~ m/^(true|on|yes|y|1)$/i );
our $acceptCPPSource           = $cfg->getProperty( 'acceptCPPSource', 1 );
    $acceptCPPSource           =
    ( $acceptCPPSource         =~ m/^(true|on|yes|y|1)$/i );
# If for some strange reason configured to accept neither, that is an error.
# So turn them both on.
if ( ! ( $acceptCSource || $acceptCPPSource )) {
    $acceptCSource = 1;
    $acceptCPPSource = 1;
}

#-------------------------------------------------------
#   Local file location definitions within this script
#-------------------------------------------------------
if ( ! defined $log_dir )    { print "log_dir undefined"; }
our $script_log_relative      = "script.log";
our $script_log               = "$log_dir/$script_log_relative";
if ( ! defined $script_log ) { print "script_log undefined"; }
our $student_code_relative    = "student-code.txt";
our $student_code             = "$log_dir/$student_code_relative";

our $instr_output_relative    = "instructor-unittest-out.txt";
our $instr_output             = "$log_dir/$instr_output_relative";
our $instr_rpt_relative       = "instr-unittest-report.html";
our $instr_rpt                = "$log_dir/$instr_rpt_relative";
our $instr_error_rpt_relative = "instr-error-report.html";
our $instr_error_rpt          = "$log_dir/$instr_error_rpt_relative";

our $student_output_relative  = "student-unittest-out.txt";
our $student_output           = "$log_dir/$student_output_relative";
our $student_rpt_relative     = "student-unittest-report.html";
our $student_rpt              = "$log_dir/$student_rpt_relative";

our $timeout_log_relative     = "timeout_log.txt";
our $timeout_log              = "$log_dir/timeout_log";
our $debug_log                = "$log_dir/debug.txt";
our $explain_rpt_relative     = "explanation_report.html";
our $explain_rpt              = "$log_dir/$explain_rpt_relative";

#-------------------------------------------------------
#   Other local variables within this script
#-------------------------------------------------------
our $student_src      = "";
our $instructor_src   = "";
our $can_proceed      = 1;
our $timeout_occurred = 0;
our $version          = "0.6";
our $delete_temps     = 0;    # Change to 0 to preserve temp files
our $studentTestMsgs  = "";
our $expSectionId     = 0;    # For the expandable sections

if(0) {
adminLog( "
localFiles              = $localFiles
log_dir                 = $log_dir
script_home             = $script_home
working_dir             = $working_dir
maxCorrectnessScore     = $maxCorrectnessScore
maxToolScore            = $maxToolScore
debug                   = $debug
" );
}

#-----------------------------
#   C/C++ compiler to use.
#   Std linux: Improve portability later. Do some minimal testing.
#   FIXME: Set a default, then check environment for C/C++ location.
#   Die if can't find an executable.
#  If CPP_COMPILER is set and meaningful, switch to use it.
#  Todo ...
#-----------------------------
my $c_compiler   = "gcc";       # "/usr/bin/gcc";
my $cpp_compiler = "g++";       # "/usr/bin/g++";
#if ( $acceptCSource && ( ! -x $c_compiler )) {
#    die "$c_compiler doesn't exist";
#}
#if ( $acceptCPPSource && ( ! -x $cpp_compiler )) {
#    die "$cpp_compiler doesn't exist";
#}

#-----------------------------
#  Diff program to use.
my $diff_prog = "diff";      # "/usr/bin/diff";
#-x $diff_prog || die "$diff_prog doesn't exist";

# Set the diff flag. Below is currently set to ignore all white space differences.
my $diff_flags = "";
if( $ignoreWSDiff ) {
    $diff_flags .= "-w ";
}
if( $ignoreBlankLineDiff ) {
    $diff_flags .= "-B ";
}
if( $ignoreCaseDiff ) {
    $diff_flags .= "-i ";
}


#=============================================================================
# Locate instructor unit test implementation
#=============================================================================

## Should be renamed to "generateFullScriptPath"?
##
sub findScriptPath
{
    my $subpath = shift;
    my $target = "$scriptData/$subpath";
    if ( -e $target )
    {
	return $target;
    }
    die "cannot file user script data file $subpath in $scriptData";
}

# Locate instructor directory with canonical C++ solution and input test files
my $instr_files;
{
    my $instructor_test_files = $cfg->getProperty( 'instructorTestFiles' );
    if ( defined $instructor_test_files && $instructor_test_files ne "" )
    {
	$instr_files = findScriptPath( $instructor_test_files );
    }
}
if ( !defined( $instr_files ) )
{
    die "Instructor test files not set.";
}
elsif ( (! -f $instr_files) && (! -d $instr_files) )
{
    die "Instructor test test '$instr_files' not found.";
}

# Build lists of the individual instructor canonical C++ file and input files
my @instr_sources;
my @instr_input_files;
# HACK: Remove all the printing once happy that plugin works properly.
#print "Looking for the instructor's files.\n\$instr_files = $instr_files\n";
while (<${instr_files}/*>) {
    my $file = $_;
    if( $file =~ /\.cpp$/i || $file=~ /\.c$/i ) {
        push (@instr_sources, $file);
#	print "CPP file = $file\n";
    }
    elsif( $file =~ /input.*\.txt$/i) {
        push (@instr_input_files, $file);
#	print "Input file = $file\n";
    }
    else {
#        print "Don't recognize $file.\n";
    }
}

if ( $#instr_sources < 0 ) {
    studentLog( "<p>Cannot identify the instructor's C++ source file ('.cpp' file).\n",
		"Was it called something like 'program.cpp?</p>\n" );
    $can_proceed = 0;
}
else {
    if ( $#instr_sources == 0 ) {
        $instructor_src = $instr_sources[0];
    }
    elsif ( $#instr_sources > 0 ) {
	$instructor_src = join(' ', @instr_sources);
	#studentLog( "<p>Multiple C++ source files present. Using ",
	#	    "$instructor_src.\nIgnoring other C++ files.",
	#	    "</p>\n" );
    }
}

if ( $debug > 0 ) {
    print "Instructor source = $instructor_src\n";
}

if ( $#instr_input_files < 0 ) {
    studentLog( "<p>Cannot find any instructor input test files.</p>\n" );
    $can_proceed = 0;
}

if ( $debug > 0 ) {
    print "Instructor test input test files = $instr_files\n";
}

if( $can_proceed == 0) {
    reportError( $instr_rpt, $instr_rpt_relative, "Test Error Report" );
}

#=============================================================================
#   Script Startup
#=============================================================================
#   Change to specified working directory and set up log directory
chdir( $working_dir );
print "working dir set to $working_dir\n" if $debug;

## PATCH FOR INTERNET EXPLORER PATH NAME PROBLEM.
## Go through all files in working dir and rename if necessary.
while (<*>) {
    my $stufile = $_;
    if ( $stufile =~ /^[A-Z]:\\.*\.py$/ ) {
        my $newName = $stufile;
        $newName =~ tr/\\/\//;
        $newName = basename( $newName );
        print "rename MS-Windows file upload file name $stufile to $newName\n";
        rename $stufile, $newName || die "Error renaming $stufile to $newName\n";
    }
}

#-----------------------------------------------
# Copy over input/output data files as necessary
# localFiles
{
    my $localFiles = $cfg->getProperty( 'localFiles' );
    if ( defined $localFiles && $localFiles ne "" ) {
        my $lf = confirmExists( $scriptData, $localFiles );
        print "localFiles = $lf\n" if $debug;
        if ( -d $lf ) {
            print "localFiles is a directory\n" if $debug;
            copyHere( $lf, $lf, \@beautifierIgnoreFiles );
        }
        else {
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

#-----------------------------------------------
sub studentLog {
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

    my $errorFeedbackGenerator = new Web_CAT::FeedbackGenerator( $rpt_absolute_path );
    $errorFeedbackGenerator->startFeedbackSection(
             $rpt_title,
             ++$expSectionId,
             0 );
    if( ! defined $rpt_message || $rpt_message eq "" ) {
	# No message passed in, so grab the script log.
	# Don't know if this is good programming style, but about to 'slurp' entire file.
	# Something really blew up, so printint everything in $script_log to screen report.
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
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $rpt_relative_path );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html"        );
}

#=============================================================================
# Find the student implementation file to use. The file should be named
# name be something like "file.cpp" (*.cpp).
#=============================================================================
# There is  probably a  better way of doing this, but I don't know how to do
# @sources = <*.cpp> in a case-insensitive way.
my $submittedCfile = 0;
my $submittedCPPfile = 0;
my $stu_compiler;

my @sources;
my $student_src_multiple = "";
my $instr_src_multiple = "";
while (<*>) {
    my $fileName = $_;
    if( $fileName =~ /\.cpp$/i) {
        push (@sources, $_);
	$submittedCPPfile = 1;
    }
    elsif( $fileName =~ /\.c$/i) {
        push (@sources, $_);
	$submittedCfile = 1;
    }
}

if ( $#sources < 0 || ! -f $sources[0] ) {
    studentLog( "<p>Cannot identify a C or C++ source file ('.c' or '.cpp' file).\n",
		"Did you call it something like 'program.cpp?</p>\n" );
    $can_proceed = 0;
}
else {
    if ( $#sources == 0 ) {
        $student_src = $sources[0];
    }
    elsif ( $#sources > 0 ) {
	# Hack: may need to be fixed, but for now, make into one happy string.
	$student_src = join(' ', @sources);
	#studentLog( "<p>Multiple C++ source files present. Using ",
	#	    "$student_src.\nIgnoring other C++ files.",
	#	    "</p>\n" );
    }
}

if ( $submittedCPPfile && $submittedCfile ) {
    studentLog( "<p>You appear to have included both C++ ('.cpp') and C ('.c') files.\n",
		"This program only supports one file and one language. Please resubmit properly.</p>\n" );
    $can_proceed = 0;
}
elsif( $submittedCPPfile ) {
    if( ! $acceptCPPSource ) {
        reportError( $instr_rpt, $instr_rpt_relative, "File Language Error Report",
	     "Your program '" . basename($student_src) . "' was not accepted.<br>\n" .
	     "<p>You appear to have submitted a C++ ('.cpp') file, " .
             "but your instructor is not accepting C++ files for this assignment.\n" .
	     "Please submit a C file instead.</p>\n" );
	$can_proceed = 0;
    }
    else {
	$stu_compiler = $cpp_compiler;
    }
}
elsif( $submittedCfile ) {
    if( ! $acceptCSource ) {
        reportError( $instr_rpt, $instr_rpt_relative, "File Language Error Report",
	     "Your program '" . basename($student_src) . "' was not accepted.<br>\n" .
	     "<p>You appear to have submitted a C ('.c') file, " .
             "but your instructor is not accepting C files for this assignment.\n" .
	     "Please submit a C++ file instead.</p>\n" );
	$can_proceed = 0;
    }
    else {
	$stu_compiler = $c_compiler;
    }
}

if ( $debug > 0 ) {
    print "Student source = $student_src\n";
}

# Potential problem if multiple source files and mix C/C++.
my $instr_compiler;
if ( $instructor_src =~ /\.cpp$/i ) {
    $instr_compiler = $cpp_compiler;
}
elsif ( $instructor_src =~ /\.c$/i ) {
    $instr_compiler = $c_compiler;
}


#=============================================================================
# A subroutine for executing a program/collecting the output
#=============================================================================
sub run_test {
    my $cmd           = shift;
    my $unitTesterDir = shift;
    my $name          = shift;
    my $title         = shift;
    my $temp_input    = shift;
    my $outfile       = shift;
    my $resultReport  = shift;      # HTML output report of testing
    my $resultReportRelative = shift;      # HTML output report of testing
    my $show_details  = shift;

    ## Local variables
    my $messages    = "";   # The collection of messages to output for a test.
    my @testScripts;        # The set of test scripts to run
    my $num_cases   = 0;    # Number of cases
    my $failures    = 0;    # count of failures
    my $errors      = 0;    # Number of runtime errors, which is 0 or 1 since
    			    # such an error crashes the program
    # Track whether there was a compiler/interpreter/execution error
    my $execution_failed = 0;
    my %hintHash = ();      # Hash to associate hints with test case files
    my $hintMessages = "";  # Hint messages to display to user

    # Open the log file for printing. Keep open. Doesn't seem to loose data with die.
    open( RESULT_REPORT, ">>$outfile" ) ||
	die "cannot open '$outfile'";

    # Build the instructor executable
    # Try to decide whether the instructor executable needs to be recompiled.
    # FIXME: reinsert the single quotes around variables below
    my $src_info = stat($instructor_src);
    my $exe_info = stat("$instr_files/instructor.exe");

    my $cmdline;
    my $exitcode;
    my $timeout_status;
    # If (1) the executable does not exist or (2) the source is more recent,
    # Problem comparing the date right now, so leaving that alone.
    # compile the instructor executable.
    # fixme: Hack: temporarily turning off date check if have multiple source files.
    #if( (! defined $exe_info) || ($src_info->mtime > $exe_info->mtime) )
    if( ! defined $exe_info )
    {
	print RESULT_REPORT "Building instructor executable: compiling $instructor_src\n";
	$cmdline = "$Web_CAT::Utilities::SHELL"
	    . "$instr_compiler -o $instr_files/instructor.exe $instructor_src 2>>$outfile";
	print "$cmdline\n";

	# Exec program and collect output
	( $exitcode, $timeout_status ) =
	     Proc::Background::timeout_system( $timeout, $cmdline );
	$exitcode = $exitcode>>8;    # Std UNIX exit code extraction.
	die "Exec died: $cmdline" if ( $exitcode != 0 );
	if( $timeout_status != 0 ) { $timeout_occurred = 1; }

	# Make sure executable got created as and where expected.
	# Currently, if something blew up, we should 'die' above. This is more
	# here as a discussion reminder to discuss which is best:
	# (1) die, thus sending email to notify the instructor (hopefully read),
	# (2) not die and issue and error message that a student will see and
	#     -- it is hoped -- complain to someone about.
	if( ! -x "$instr_files/instructor.exe" ) {
	    $can_proceed = 0;
	    reportError( $instr_rpt, $instr_rpt_relative, "Test Error Report",
		 "Your instructor's program '" . basename($instructor_src) . "' appears to have "
		 . "had compilation errors.<br>\n"
		 . "Try submitting again to see whether the problem solves itself."
		 . "If not, please notify your instructor of the problem.\n" );
	    return (0, 0, 1);
	}
    }
    # Hack: fixme: needs to be fixed to deal with multiple source files.
    #else {
    #    print "$instr_files/instructor.exe (mtime=" . scalar $exe_info->mtime
    #	    . ") is newer than $instructor_src (mtime=" . scalar $src_info->mtime
    #	    . "). No need to compile.\n";
    #}

    # Build the student executable
    print RESULT_REPORT "Building student executable: compiling $student_src\n";

    # The single quotes around variables below are because there have been
    # instances of pathnames with metacharacters that caused exec to blow up.
    $cmdline = "$Web_CAT::Utilities::SHELL"
	. "$stu_compiler -o $working_dir/student.exe $student_src 2>>'$outfile'";
print $cmdline . "\n";
    # Exec program and collect output
    ( $exitcode, $timeout_status ) =
	 Proc::Background::timeout_system( $timeout, $cmdline );
    $exitcode = $exitcode>>8;    # Std UNIX exit code extraction.

    if( $exitcode != 0 || ( ! -x "$working_dir/student.exe" ) ) {
	# FIXME: Need to generate an error message about compiler problems.
        $can_proceed = 0;
        reportError( $instr_rpt, $instr_rpt_relative, "Test Error Report",
	     "Your program '" . basename($student_src) . "' had compilation errors.<br>\n"
	     . "Please fix the errors and submit when correct.<br>\n"
	     . "Note: If it compiled for you, be aware that non-standard C++ extensions "
	     . "(e.g., MS Visual C++ additions, including headers) can cause problems. "
	     . "'g++' is used to compile your code.\n" );
	return (0, 0, 1);
    }
    if( $timeout_status != 0 ) { $timeout_occurred = 1; }
    # Make sure executable got created as and where expected.

    if ( $diff_flags eq "" ) {
	print RESULT_REPORT "Doing exact comparison, including white space differences.\n";
    }
    else {
	print RESULT_REPORT "Doing comparison excluding at least some white space differences.\n";
    }

    ## Build up hints array if in use
    if( $showHints ) {
	open HINTS, "$hintsFile" or die "Cannot open $hintsFile";

	while( <HINTS> ) {
	    chomp $_;
	    my $hint = $_;
	    # Ignore hints where /^#/.
	    if (! ($hint =~ /^#/) ) {
		my @hintArray = split /=/, $hint, 2;
		if( $#hintArray != 1 ) {
		    print "Couldn't find separator in hint line: '$hint'. Ignoring ...\n";
		} else {
		    $hintHash{$hintArray[0]} = $hintArray[1];
		}
	    }
	}

	# FIXME: Little debug routine. Zap when working.
	foreach my $file (keys(%hintHash)) {
	    print "Hint for $file = " . $hintHash{$file} . "\n";
	}
    }


    my $hintsCount = 0;   # Track hints issued so that don't exceed $hintsLimit.
    my $noMoreHints = 0;  # Used to track that doing no more hints.

    ## START OF LOOP GENERATING ALL INSTRUCTOR OUTPUT TEST FILES
    foreach( @instr_input_files ) {
	my $input_file = $_;
	my $instructor_output_file = "";
	my $student_output_file = "";
	# Changed to reduce naming convention restrictions
	#if( $_ =~ m/(\/.+\/)(input)(\d+)\.txt/i ) {
	if( $_ =~ m/(\/.+\/)(input)(.*)\.txt/i ) {
	    $instructor_output_file = "$log_dir/" . "output" . "$3" . ".txt";
	    $student_output_file = "$log_dir/stu-output" . $3 . ".txt";
	}
	else {
	    die "Instructor input file matching error. Cannot proceed. ('$_')\n";
	}

	#-----------------------------------------
	# Create the instructor output file
	print RESULT_REPORT "Creating instructor output file $instructor_output_file\n";

	$cmdline = "$Web_CAT::Utilities::SHELL"
	    . "'$instr_files'/instructor.exe < '$input_file' > '$instructor_output_file' 2>>'$outfile'";
	# Exec program and collect output
	( $exitcode, $timeout_status ) =
	     Proc::Background::timeout_system( $timeout, $cmdline );
	$exitcode = $exitcode>>8;    # Std UNIX exit code extraction.
	die "Exec died: $cmdline" if ( $exitcode != 0 );
	# Should we do this, or issue a 'die' instead.
	# Which receives more of the right type of attention?
	if( $timeout_status != 0 ) {
	    $timeout_occurred = 1;
	    $can_proceed      = 0;
	    reportError( $instr_error_rpt, $instr_error_rpt_relative,
	         "Test Error Report",
		 "Your instructor's program '" . basename($instructor_src)
		 . "' has apparently entered an infinite loop.<br>\n"
		 . "Please notify your instructor of the problem.\n" );
	    # And penalize students heavily for the problem? Another discussion point.
	    # If so, uncomment line below.
	    return (0, 0, 1);
	}

	#-----------------------------------------
	# Create the student output file
	print RESULT_REPORT "Creating student output file $student_output_file\n";
	$cmdline = "$Web_CAT::Utilities::SHELL"
	    . "$working_dir/student.exe < $input_file > $student_output_file 2>>$outfile";
	# Exec program and collect output
	( $exitcode, $timeout_status ) =
	     Proc::Background::timeout_system( $timeout, $cmdline );
	$exitcode = $exitcode>>8;    # Std UNIX exit code extraction.
	die "Exec died: $cmdline" if ( $exitcode != 0 );
	if( $timeout_status != 0 ) {
	    $timeout_occurred = 1;
	    $can_proceed = 0;
	    reportError( $instr_error_rpt, $instr_error_rpt_relative,
	         "Test Error Report",
		 "Your program '" . basename($student_src) . "' has apparently "
		 . "entered an infinite loop.<br>\n"
		 . "Please fix your program to avoid this problem with future submissions.\n" );
	    # And penalize students heavily for the problem?
	    # If so, uncomment line below.
	    return (0, 0, 1);
	}
	else {print RESULT_REPORT "... Created student output file $student_output_file\n";}

	#-----------------------------------------
	# Run a diff on the output files
	# Note: set whether to ignore white space differences
	print RESULT_REPORT "Comparing instructor and student output files ('$student_output_file')\n";
        # FIXME: reinsert the single quotes around variables below
	$cmdline = "$Web_CAT::Utilities::SHELL"
	    . "$diff_prog $diff_flags '$instructor_output_file' '$student_output_file' "
	    . ">'$student_output_file.dif' 2>>$outfile";
	print RESULT_REPORT "$cmdline\n";
	# Exec program and collect output
	( $exitcode, $timeout_status ) =
	     Proc::Background::timeout_system( $timeout, $cmdline );
	$exitcode = $exitcode>>8;    # Std UNIX exit code extraction.
	if( $timeout_status != 0 ) {
	    $timeout_occurred = 1;
	    $can_proceed = 0;
	    print "Thinks a timeout happened while running diff. Highly unlikely!\n";
	}

	# Increment the test count
	$num_cases++;

	# Deal with various test failure possibilities and count failures.
	if ( $exitcode < 0 || ! -e $student_output_file ) {
	    die "Exec died: $cmdline"
	}
	elsif ( -z "$student_output_file.dif" ) {
	    print RESULT_REPORT "\n.\nPASS: $instructor_output_file and $student_output_file match.\n";
	}
	elsif ( ! -z "$student_output_file.dif" ) {
	    $failures++;
	    if( $showHints && (! $noMoreHints )) {
		if( $hintsBlackout ) {
			$hintMessages .=
			     "<p>Your instructor has choosen to cut off extra feedback "
			     . "$hideHintsWithin day(s) before the due date. Consequently, "
			     . "no hints will be given.</p>\n";
			$noMoreHints = 1;
		}
		elsif( $hintsLimit == 0 || $hintsCount < $hintsLimit ) {
		    $hintsCount++;
		    my $aHint = $hintHash{ basename($input_file) };
		    # Not guaranteed to have a hint per test input file, so must check before ref.
		    if( defined $aHint ) {
			$hintMessages .= $aHint . "<br>\n";
		    }
		    if( $hintsCount != 0 && $hintsCount == $hintsLimit ) {
			$noMoreHints = 1;
		    }
		}
	    }
	    print RESULT_REPORT "\nF\nFAIL: $instructor_output_file and $student_output_file differ.\n";
	}
	print RESULT_REPORT "... done comparing instructor and student output files "
	    . "('$student_output_file')\n";

	if ( $timeout_occurred )
	{
	    #if ( $@ )
	    #{
		# timed out
		adminLog("Script thinks that a timeout happened.\n" .
			 "Timeout value = $timeout\n");

		$can_proceed = 0;

		my $timeOutFeedbackGenerator = new Web_CAT::FeedbackGenerator( $timeout_log );
		$timeOutFeedbackGenerator->startFeedbackSection( "Errors During Testing" );
		$timeOutFeedbackGenerator->print(
		      "<p><font color=\"#ee00bb\">Testing your solution exceeded the allowable time"
		    . "limit for this assignment.</font></p>\n"
		    . "<p>Most frequently, this is the result of <b>infinite recursion</b>--when "
		    . "a recursive method fails to stop calling itself--or <b>infinite "
		    . "looping</b>--when a while loop or for loop fails to stop repeating.</p> "
		    . "<p>As a result, no time remained for further analysis of your code.</p> "
                );
		$timeOutFeedbackGenerator->endFeedbackSection;
		$timeOutFeedbackGenerator->close;
		# Add to list of reports
		# -----------
		$reportCount++;
		$cfg->setProperty( "report${reportCount}.file",
				   $timeout_log_relative );
		$cfg->setProperty( "report${reportCount}.mimeType", "text/html" );

		return (0, 0, 1);
	    #}
	}

    } ## END OF LOOP PROCESSING ALL UNIT TESTER FILES
      ## Output from all unit testers was appended to the same file, "$outfile".
    close( RESULT_REPORT );

    #=========================================================================
    # Compare the output to test case expectations
    #=========================================================================

    if ( $can_proceed )
    {

    my $unitTesterOutput = "";
    # Compute overall values
    my $succeeded = $num_cases - $failures - $errors;
print "\$succeeded = $succeeded, \$num_cases = $num_cases, \$failures = $failures, \$errors = $errors\n";
    my $eval_score = ( $num_cases > 0 )
	? $succeeded/($num_cases*1.0)
	: 0;

    my $testsExecuted = $num_cases;
    my $allTestsPass = $succeeded == $num_cases;
    my $sectionTitle = "$title ";
    if ( $testsExecuted == 0 )
    {
	# This used to say "no tests submitted". That might be true, but
	# it is more likely that the student messed up.
	# Be vague and sort of blame them.  :-)
        $sectionTitle .=
            "<b class=\"warn\">(No Testable Files Found!)</b>";
    }
    elsif ( $allTestsPass )
    {
        $sectionTitle .= "(100%)";
    }
    else
    {
        my $studentCasesPercent =
	        $allTestsPass ?
		100 :
		sprintf( "%.1f", $eval_score * 100 );
        $sectionTitle .= "<b class=\"warn\">($studentCasesPercent%)</b>";
    }

    #--------------------------------
    # Fire up the feedback generator
    my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $resultReport );

    # startFeedbackSection( title, report number,
    #     initially collapsed (1) or expanded (0) )
    #     Expand if there are errors.
    $feedbackGenerator->startFeedbackSection(
             $sectionTitle,
             ++$expSectionId,
	     ( $failures + $errors > 0 ) ? 0 : 1 );

    my $casesPlural = ($num_cases!=1) ? "s" : "";
    my $failuresPlural = ($failures!=1) ? "s" : "";
    my $errorsPlural = ($errors!=1) ? "s" : "";
    $feedbackGenerator->print( "Summary: $num_cases case" . $casesPlural
             . " ($failures failure" . $failuresPlural
	     . ", $errors error" . $errorsPlural . ")<br>\n" );
    if( $showHints && (! $allTestsPass ) ) {
        $feedbackGenerator->print( "== Hints ==<br>\n " . $hintMessages );
    }

    # FIXME: Do we hide details if all tests pass?
    # If all is well, why report? On the other hand, students may be happy
    # to see details of passing tests, even if there are few details.
    # Currently choosing not to report if all is well.
    if( ! $allTestsPass )
    {
	$feedbackGenerator->print( "<pre>\n" );
	$feedbackGenerator->print( $unitTesterOutput );
	$feedbackGenerator->print( "</pre>\n" );
    }
    $feedbackGenerator->endFeedbackSection;

    # Close down this report
    $feedbackGenerator->close;
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $resultReportRelative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html"       );

adminLog("Return values from processing are: \n
eval_score = $eval_score,
succeeded  = $succeeded,
num_cases  = $num_cases\n");

	return ( $eval_score, $succeeded, $num_cases );
    }   # End of "if ($can_proceed ) ..."
    else
    {
	# Things blew up.
	return (0, 0, 1);
    }
}


#=============================================================================
# A subroutine to explain results score
#=============================================================================
sub explain_results
{
    my $totalScore              = shift;
    my $instructorCasesPercent  = shift;
    my $maxCorrectnessScore     = shift;

    $totalScore = int( $totalScore * 10 + 0.5 ) / 10;
    $instructorCasesPercent = int( $instructorCasesPercent * 10 + 0.5 ) / 10;
    my $possible = int( $maxCorrectnessScore * 10 + 0.5 ) / 10;
    my $feedbackGenerator =
       new Web_CAT::FeedbackGenerator( $explain_rpt );
    $feedbackGenerator->startFeedbackSection(
        "Interpreting Your Correctness/Testing Score "
        . "($totalScore/$possible)",
        ++$expSectionId,
	1 );
    my $feedbackStr = "";
    $feedbackStr .= "<table style=\"border:none\">\n";
    my $feedbackStr2 = "<tr><td colspan=\"2\">score =";

    $feedbackStr .=
    "<tr><td><b>Results from running your instructor's tests:</b></td>\n"
    . "<td class=\"n\">$instructorCasesPercent%</td></tr>\n";
    $feedbackStr2 .= " * $instructorCasesPercent%";

    $feedbackStr2 .=
        " * $maxCorrectnessScore points possible =\n"
	. "$totalScore</td></tr></table>\n"
	. "<p>Full-precision (unrounded) percentages are used to calculate "
	. "your score, not the rounded numbers shown above.</p>\n";
    $feedbackGenerator->print( $feedbackStr  );
    $feedbackGenerator->print( $feedbackStr2 );
    $feedbackGenerator->endFeedbackSection;
    $feedbackGenerator->close;
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $explain_rpt_relative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html"       );
}

#=============================================================================
# Phases II and III: Execute the program and produce results
#=============================================================================
# FIXME: Run student coverage testing here. See instructor testing
#    for details.

# FIXME: This will need extra cleaning and parameter modification.
# Args to run_test
#    $cmd           command to run,
#    $unitTesterDir path to directory with unittesters
#    $name          name of program
#    $title         fancy name of program
#    $temp_input    redirected input source
#    $outfile       redirected output destination for text
#    $resultReport  absolute place to put the HTML report
#    $resultReportRelative  relative name to output the HTML report
#    $show_details  whether or not to show details -- Equal to $debug?
#                   maybe how much detail to show in report.
#                   currently not using it, but could easily enough.

my $score = 0.0;
my @instr_eval;
$instr_eval[0] = 0.0;

# To run the instructor tests, need to meet one of the following conditions:
# (1) Only doing instructor tests ($doStudentTests == false), OR
# (2) Can proceed and
#    (a) not requiring that all students tests must pass, or
#    (b) they did pass.
# Run if student results not required, or if required but good enough.
if ( $can_proceed )
{
    @instr_eval   = run_test( $cpp_compiler,
			      $instr_files,
			      "Reference Implementation",
			      "Results From Running Your Instructor's Tests",
			      "",
			      $instr_output,
			      $instr_rpt,
			      $instr_rpt_relative,
			      1 );
    if ( !$timeout_occurred )
    {
	$cfg->setProperty( "instructorEval", join( " ", @instr_eval   ) );
    }
    elsif ( $timeout_occurred )
    {
	open( SCRIPT_LOG, ">>$script_log" ) ||
	    die "cannot open $script_log: $!";
	print SCRIPT_LOG "\n",
	"Your program exceeded the allowed time limit while running tests.\n";
	close( SCRIPT_LOG );
    }
}
$score = $instr_eval[0];

if ( $can_proceed ) {
    explain_results( $score * 100.0,
		     $instr_eval[0] * 100.0,
		     $maxCorrectnessScore );
}

# Scale to be out of range of $maxCorrectnessScore
$cfg->setProperty( "score.correctness", $score* $maxCorrectnessScore );
$cfg->setProperty( "numReports",      $reportCount );
adminLog("reportCount = " . $reportCount . "\n" );
$cfg->save();

if ( $debug )
{
    my $props = $cfg->getProperties();
    while ( ( my $key, my $value ) = each %{$props} )
    {
	print $key, " => ", $value, "\n";
    }
}

#-----------------------------------------------------------------------------
exit( 0 );

