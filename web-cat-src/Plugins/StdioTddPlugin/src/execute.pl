#!c:\perl\bin\perl.exe -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.2 2011/10/25 05:24:53 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Curator: execution script for Free Pascal submissions
#
#   usage:
#       executePascal.pl <properties-file>
#=============================================================================
use strict;
use English;
use File::stat;
use Config::Properties::Simple;
use Proc::Background;
use Web_CAT::FeedbackGenerator;
use Web_CAT::Utilities;


#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $propfile    = $ARGV[0];	# property file name
my $cfg         = Config::Properties::Simple->new( file => $propfile );

my $pid         = $cfg->getProperty( 'userName' );
my $working_dir	= $cfg->getProperty( 'workingDir' );
my $script_home	= $cfg->getProperty( 'scriptHome' );
my $log_dir	    = $cfg->getProperty( 'resultDir' );
my $timeout	    = $cfg->getProperty( 'timeout' );


#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
my $debug                    = $cfg->getProperty( 'debug',      0 );
my $hintsLimit               = $cfg->getProperty( 'hintsLimit', 3 );
my $script_log_relative      = "execute-script-log.txt";
my $script_log               = "$log_dir/$script_log_relative";
my $test_input_relative      = "tests-in.txt";
my $test_input               = "$log_dir/$test_input_relative";
my $expected_output_relative = "expected-out.txt";
my $expected_output          = "$log_dir/$expected_output_relative";
my $student_output_relative  = "output.txt";
my $student_output           = "$log_dir/$student_output_relative";
my $student_rpt_relative     = "student-tdd-report.txt";
my $student_rpt              = "$log_dir/$student_rpt_relative";
my $instr_output_relative    = "instr-output.txt";
my $instr_output             = "$log_dir/$instr_output_relative";
my $instr_rpt_relative       = "instr-tdd-report.txt";
my $instr_rpt                = "$log_dir/$instr_rpt_relative";
my $assessment_relative      = "TDD-assessment.txt";
my $assessment               = "$log_dir/$assessment_relative";
my $student_exec             = $cfg->getProperty( 'studentExeFile',
						  "Executable.exe" );
my $can_proceed              = $cfg->getProperty( 'canProceed', 1 );
my $timeout_occurred         = 0;
my $instrTimeoutOccurred     = 0;
my $student_tests            = ""; # test case file
my $max_score                = $cfg->getProperty( 'max.score.correctness', 0 );

my @student_eval    = ();
my @instr_eval      = ();

#-----------------------------
my $version         = "1.3.2";
my @labels          = ();	# User-provided test case names
my @test_cases	    = ();	# test case input
my @expected_output = ();	# corresponding expected output
my @case_nos        = ();       # test case number for each output line
my $temp_input      = $test_input;       # Name for temp test input file
my $delete_temps    = 0;        # Change to 0 to preserve temp files


#=============================================================================
# Locate reference implementation
#=============================================================================
# instructorReference
my $scriptData = $cfg->getProperty( 'scriptData', '.' );
#my @scriptDataDirs = < $scriptData/* >;
$scriptData =~ s,/$,,;

sub findScriptPath
{
    my $subpath = shift;
    my $target = "$scriptData/$subpath";
#    foreach my $sddir ( @scriptDataDirs )
#    {
#	my $target = $sddir ."/$subpath";
#	#print "checking $target\n";
	if ( -e $target )
	{
	    return $target;
	}
#    }
    die "cannot file user script data file $subpath in $scriptData";
}

# instructorReference
my $instr_exec;
{
    my $reference = $cfg->getProperty( 'instructorReference' );
    if ( defined $reference && $reference ne "" )
    {
	$instr_exec = findScriptPath( $reference );
	$instr_exec =~ s,/,\\,g;
    }
}
if ( !defined( $instr_exec ) )
{
    die "Instructor reference not set.";
}
elsif ( ! -x $instr_exec )
{
    die "Instructor reference '$instr_exec' not found.";
}


#=============================================================================
# Script Startup
#=============================================================================
# Change to specified working directory and set up log directory
chdir( $working_dir );


#=============================================================================
# Find the test suite file to use
# Extensions supported *.txt, *.text, *.tst, *.test
#=============================================================================
my @sources = (<*.txt *.text *.tst *.test>);
open( SCRIPT_LOG, ">$script_log" ) ||
    die "cannot open $script_log: $!";
if ( $#sources < 0 || ! -f $sources[0] )
{
    print SCRIPT_LOG
	"Cannot identify a test suite file.\n",
	"Did you use a .txt, .text, .tst, or .test extension?\n";
    $can_proceed = 0;
}
else
{
    $student_tests = $sources[0];
    if ( $#sources > 0 )
    {
	print SCRIPT_LOG
	    "Multiple test suite files present.  Using $student_tests.\n",
	    "Ignoring other .txt/.text/.tst/.test files.\n";
    }
}
close( SCRIPT_LOG );


#=============================================================================
# A subroutine for normalizing output lines before comparing them
#=============================================================================
sub normalize
{
    my $line = shift;
    $line =~ s/^\s+//o;		# Trim leading space
    $line =~ s/\s+$//o;		# Trim trailing space
    $line =~ s/\s+/ /go;	# Convert multi-space sequences to one space
    $line =~ s,\s+/\s+,/,go;    # Remove spaces around /'s
    $line =~ tr/A-Z/a-z/;	# Convert to lower case
    return $line;
}


#=============================================================================
# A subroutine for executing a program/collecting the output
#=============================================================================
sub run_test
{
    my $pgm          = shift;
    my $name         = shift;
    my $title        = shift;
    my $outfile      = shift;
    my $resultfile   = shift;
    my $show_details = shift;

    # Exec program and collect output
    my $cmdline = $Web_CAT::Utilities::SHELL
        . "$pgm < $temp_input > $outfile";
    print $cmdline, "\n" if ( $debug );
    my ( $exitcode, $timeout_status ) = Proc::Background::timeout_system(
        $timeout * 2, $cmdline );
    if ( $timeout_status )
    {
        $timeout_occurred++;
    }


    #=========================================================================
    # Compare the output to test case expectations
    #=========================================================================

    my $line        = 0;    # next line in @expected_output to match
    my $last_failed = -1;   # index of last failed case
    my $failures    = 0;    # count of failures
    my $errs        = 0;    # Number of runtime errors, which is 0 or 1 since
    			    # such an error crashes the program

    open( STUDENT, "$outfile" ) ||
	die "Cannot open file for input '$outfile': $!";
    my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $resultfile );
    $feedbackGenerator->startFeedbackSection( $title, 1 );
    $feedbackGenerator->print( <<EOF );
<pre>
tddpas.pl v$version   (c) 2004 Virginia Tech. All rights reserved.
Testing $name using $student_tests
EOF
    while ( <STUDENT> )
    {
	if ( $line > $#expected_output )
	{
	    # If the expected output has run out, just add up the remaining
	    # lines as errors or crashes.
	    while ( defined $_ )
	    {
		if ( m/^runtime error/io )
		{
		    $feedbackGenerator->print( "\n" . normalize( $_ ) . "\n" );
		    $errs++;
		    last;
		}
		$failures++;
		$_ = <STUDENT>;
	    }
	    last;
	}

	# If the line does not match the expected output
	if ( normalize( $_ ) ne $expected_output[$line] )
	{
	    if ( $line % 78 == 0 )
	    {
		$feedbackGenerator->print( "\n" );
	    }
	    my $this_fail = $case_nos[$line];
	    $feedbackGenerator->print( "F" );
	    if ( $this_fail != $last_failed )
	    {
		$feedbackGenerator->print( "\ncase " . ( $this_fail + 1 ) .
		    " FAILED: $labels[$this_fail]\n" );
		$failures++;
		if ( $show_details )
		{
		    $feedbackGenerator->print(
			      "  Expected: '$expected_output[$line]'\n" );
		    $feedbackGenerator->print(
			      "       Got: '" . normalize( $_ ) . "'\n" );
		}
		else
		{
		    $feedbackGenerator->print(
		      "  Incorrectly Expected: '$expected_output[$line]'\n" );
		}
	    }
	    $last_failed = $this_fail;
	}
	else
	{
	    if ( $line % 78 == 0 )
	    {
		$feedbackGenerator->print( "\n" );
	    }
	    $feedbackGenerator->print( "." );
	}
	$line++;
    }

    close( STUDENT );

    if ( $line <= $#expected_output )
    {
	$failures += $#expected_output - $line + 1;
    }

    my $num_cases = $#labels + 1;
    my $succeeded = $num_cases - $failures - $errs;
    my $eval_score = ( $num_cases > 0 )
	? $succeeded/$num_cases
	: 0;
    $feedbackGenerator->print(
	"\n\nTests Run: $num_cases, Errors: $errs, Failures: $failures (" .
        sprintf( "%.1f", $eval_score*100 ) .
        "%)\n</pre>\n" );
    $feedbackGenerator->endFeedbackSection;
    $feedbackGenerator->close;
    return ( $eval_score, $succeeded, $num_cases );
}


if ( $can_proceed )
{

#=============================================================================
# Phase I: Parse and split the test case input file
#=============================================================================
open( SCRIPT_LOG, ">>$script_log" ) ||
    die "cannot open $script_log: $!";
open ( CASES, $student_tests ) ||
    die "Cannot open '$student_tests': $!";

my $scanning_input = 0;
my $case = -1;
while ( <CASES> )
{
    # skip comment lines
    next if ( m,^//(?!--|==),o );

    if ( m,^//==,o )
    {
	if ( $scanning_input )
        {
	    print SCRIPT_LOG
		"$student_tests: ", $INPUT_LINE_NUMBER - 1,
		": improperly formatted test case.\n";
        }
	my $label = $_;
	chomp $label;
	$label =~ s,^//==[-=\s]*,,o;
	$label =~ s,[-=\s]*$,,o;
	# if ( $label eq "" ) { $label = "(no label)"; }
	push( @labels, $label );
	push( @test_cases, "" );
	$case++;
	$scanning_input = 1;
    }
    elsif ( m,^//--,o )
    {
	if ( ! $scanning_input )
	{
	    print SCRIPT_LOG
		"$student_tests: ", $INPUT_LINE_NUMBER,
		": improperly formatted test case; cannot proceed.\n";
	}
	$scanning_input = 0;
    }
    else
    {
	if ( $scanning_input )
	{
	    # Then this is an input line
	    if ( $#test_cases < 0 )
	    {
	        print SCRIPT_LOG
		    "$student_tests: ", $INPUT_LINE_NUMBER,
		    ": improperly formatted test case.\n";
	    }
	    else
	    {
		$test_cases[$#test_cases] .= $_;
		if ( $labels[$#labels] eq "" )
		{
		    # Use first line of input for case label
		    chomp;
		    $labels[$#labels] = $_;
		}
	    }
	}
	else
	{
	    if ( $#labels < 0 )
	    {
		print SCRIPT_LOG
		    "$student_tests: ", $INPUT_LINE_NUMBER,
		    ": improperly formatted test case.\n";
	    }
	    push( @expected_output, normalize( $_ ) );
	    push( @case_nos, $case );
	}
    }
close( SCRIPT_LOG );
}
close( CASES );

# Produce stdin input file for programs to use:
open( INFILE, "> $temp_input" ) ||
    die "Cannot open '$temp_input': $!";
print INFILE for @test_cases;
close( INFILE );

#=============================================================================
# Phases II and III: Execute the program and produce results
#=============================================================================
@student_eval = run_test( $student_exec,
			  "your submission",
			  "Program Correctness (Your Solution)",
			  $student_output,
			  $student_rpt, 1 );
$cfg->setProperty( "studentEval",    join( " ", @student_eval ) );

if ( $student_eval[1] == $student_eval[2] && !$timeout_occurred )
{
    @instr_eval   = run_test( $instr_exec,
			      "reference implementation",
			      "Test Validity (Reference Solution)",
			      $instr_output,
			      $instr_rpt, 0 );

    if ( !$timeout_occurred )
    {
	my %hints = ();
	my $catastrophe = 0;
	my $coverage;
	if ( -f "__coverage__.txt" )
	{
	    open( COVERAGE, "__coverage__.txt" ) ||
		die "Cannot open '__coverage.txt__': $!";
	    while (<COVERAGE>)
	    {
		if ( defined( $coverage ) )
		{
		    $hints{$coverage}++;
		}
		chomp;
		$coverage = $_;
	    }
	    close( COVERAGE );
	}
	else
	{
	    $catastrophe++;
	}
	if ( !defined( $coverage ) )
	{
	    $coverage = "0 0 1";
	}
        #my ( $coverage_percent,
        #     $coverage_passed,
        #     $coverage_total ) = split( /\s+/, $coverage );

        #my $student_percent_int = int( $student_eval[0] * 100 + 0.5 );
        #my $instr_percent_int = int( $instr_eval[0] * 100 + 0.5 );
        #my $coverage_int = int( $coverage_percent * 100 + 0.5 );

        #$cfg->setProperty( "score.correctness",
        #	   $student_eval[0] * $instr_eval[0] * $coverage_percent
        #	   * $max_score );

	$cfg->setProperty( "instructorEval", join( " ", @instr_eval   ) );
	$cfg->setProperty( "coverage",       $coverage );

	# prep hints
	if ( $catastrophe || ( %hints && $hintsLimit > 0 ) )
	{
	    my $hintsReport = "$log_dir/hints.txt";
	    my $feedbackGenerator = new Web_CAT::FeedbackGenerator(
	       $hintsReport );
	    $feedbackGenerator->startFeedbackSection( "Hints" );
	    $feedbackGenerator->print( "The following hint(s) may help you "
            . "locate some ways in which your solution and your testing may "
	    . "be improved:</p>\n" );
	    if ( %hints && $hintsLimit > 0 )
	    {
		$feedbackGenerator->print( "<pre>\n" );
		foreach my $msg ( keys %hints )
		{
		    if ( $hintsLimit > 0 )
		    {
			$feedbackGenerator->print(
			    "hint: your testing may not "
			  . "completely cover " . $msg . "\n" );
			$hintsLimit--;
		    }
		    else
		    {
			last;
		    }
		}
		$feedbackGenerator->print( "</pre>" );
	    }
	    if ( $catastrophe )
	    {
		$feedbackGenerator->print(
		    "<p>Your test cases incorrectly forced a run-time error "
		    . "that prevented analysis of your test case coverage."
		    . "</p>\n" );
	    }
	    $feedbackGenerator->endFeedbackSection;
	    $feedbackGenerator->close;
	    $cfg->setProperty( 'hintsReport', 'hints.txt' );
	}
    }
    elsif ( !$timeout_occurred )
    {
	open( SCRIPT_LOG, ">>$script_log" ) ||
	    die "cannot open $script_log: $!";
	print SCRIPT_LOG "\nYour program must pass all of your test cases ",
        "before you can receive a non-zero score and before the reference ",
        "implementation will be run.\n";
	close( SCRIPT_LOG );
    }
    else
    {
	$instrTimeoutOccurred++;
	open( SCRIPT_LOG, ">>$script_log" ) ||
	    die "cannot open $script_log: $!";
	print SCRIPT_LOG "\n",
    "Your test cases exceeded the allowed time limit while being assessed\n",
    "against the reference implementation.\n";
	close( SCRIPT_LOG );
    }
}
elsif ( $timeout_occurred )
{
    open( SCRIPT_LOG, ">>$script_log" ) ||
	die "cannot open $script_log: $!";
    print SCRIPT_LOG "\n",
    "Your program exceeded the allowed time limit while running your tests.\n";
    close( SCRIPT_LOG );
}
}

#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================

my $reportCount = $cfg->getProperty( 'numReports', 0 );
if ( stat( $script_log )->size != 0 )
{
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $script_log_relative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/plain" );
}
if ( $can_proceed )
{
#    $reportCount++;
#    $cfg->setProperty( "report${reportCount}.file", $student_rpt_relative );
#    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );

#    $reportCount++;
#    $cfg->setProperty( "report${reportCount}.file", $instr_rpt_relative );
#    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );

    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",
		       $student_output_relative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/plain" );
    $cfg->setProperty( "report${reportCount}.inline",   "false" );
    $cfg->setProperty( "report${reportCount}.label",
		       "Your submission's output" );

}
if ( !$can_proceed )
{
    $cfg->setProperty( "score.correctness", 0 );
    $cfg->setProperty( 'canProceed', 0 );
}
if (  $timeout_occurred )
{
    $cfg->setProperty( "score.correctness", 0 );
    if ( $instrTimeoutOccurred )
    {
	$cfg->setProperty( 'instrTimeoutOccurred', 1 );
    }
    else
    {
	$cfg->setProperty( 'timeoutOccurred', 1 );
    }
}
$cfg->setProperty( "numReports", $reportCount );
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
