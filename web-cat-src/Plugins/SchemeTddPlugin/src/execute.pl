#!c:\perl\bin\perl.exe -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.2 2011/02/27 03:52:22 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Curator: execution script for Free Pascal submissions
#
#   usage:
#       executeSchemeTDD.pl <properties-file>
#=============================================================================
use strict;
use Config::Properties::Simple;
use English;
use File::stat;
use Lisp::Reader  qw(lisp_read);
use Lisp::Printer qw(lisp_print);
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
my $log_dir	= $cfg->getProperty( 'resultDir' );
my $timeout	= $cfg->getProperty( 'timeoutForOneRun', 30 );


#-------------------------------------------------------
# In the future, these could be set via parameters set in Web-CAT's
# interface
#-------------------------------------------------------
my $debug              = $cfg->getProperty( 'debug',       0 );
my $max_score          = $cfg->getProperty( 'max.score.correctness', 0 );
my $predicates         = $cfg->getProperty( 'predicateList', "" );
my @predicateList      = ();
if ( !defined( $predicates ) || $predicates eq "" )
{
    print STDERR "predicateList is undefined.\n";
}
else
{
    @predicateList = split(/\s+|[,|]\s*/, $predicates);
}

#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
my $script_log_relative      = "execute-script-log.txt";
my $script_log               = "$log_dir/$script_log_relative";
my $student_code_relative    = "student-code.txt";
my $student_code             = "$log_dir/$student_code_relative";
my $test_input_relative      = "tests-in.txt";
my $test_input               = "$log_dir/$test_input_relative";
my $ref_input_relative       = "ref-in.txt";
my $ref_input                = "$log_dir/$ref_input_relative";
my $student_output_relative  = "output.txt";
my $student_output           = "$log_dir/$student_output_relative";
my $instr_output_relative    = "reference-out.txt";
my $instr_output             = "$log_dir/$instr_output_relative";
my $student_rpt_relative     = "student-tdd-report.txt";
my $student_rpt              = "$log_dir/$student_rpt_relative";
my $instr_rpt_relative       = "instr-tdd-report.txt";
my $instr_rpt                = "$log_dir/$instr_rpt_relative";
my $assessment_relative      = "TDD-assessment.txt";
my $assessment               = "$log_dir/$assessment_relative";

my $student_src      = "";

my $can_proceed      = 1;
my $timeout_occurred = 0;
my @student_eval     = ();
my @instr_eval       = ();
my $coverage         = "0 0 1";

# From tddpas.pl
#-----------------------------
my $version         = "3.1";
my @test_cases	    = ();	# test cases, as a list of lists,
                                # where each nested list has three
                                # parts:
                                #   label
                                #   input
                                #   output
my $delete_temps    = 0;        # Change to 0 to preserve temp files

# Scheme interpreter to use
#-----------------------------
my $scheme_home = "C:\\Progra~1\\PLT\\";
if ( ! -d $scheme_home )
{
    $scheme_home = "G:\\PLT\\";
    if ( ! -d $scheme_home )
    {
	die "Cannot identify correct scheme_home ($scheme_home)";
    }
}
my $scheme      = $scheme_home . "MzScheme.exe";
# my $scheme_lib  = "-library ${scheme_home}lib";




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
my $instr_src;
{
    my $reference = $cfg->getProperty( 'instructorReference' );
    if ( defined $reference && $reference ne "" )
    {
	$instr_src = findScriptPath( $reference );
    }
}
if ( !defined( $instr_src ) )
{
    die "Instructor reference not set.";
}
elsif ( ! -f $instr_src )
{
    die "Instructor reference '$instr_src' not found.";
}


#=============================================================================
# Script Startup
#=============================================================================
# Change to specified working directory and set up log directory
chdir( $working_dir );

my $script_log_started = 0;

sub studentLog
{
    open( SCRIPT_LOG, ">>$script_log" ) ||
	die "cannot open $script_log: $!";
	if (!$script_log_started)
	{
		print SCRIPT_LOG "<div class=\"module\"><div ",
		    "dojoType=\"webcat.TitlePane\" title=\"Warnings\">\n";
		$script_log_started++;
	}
    print SCRIPT_LOG @_;
    close( SCRIPT_LOG );
}

#=============================================================================
# Find the source file to use
# Extensions supported *.scm, *.ss, *.scheme
#=============================================================================
my @sources = (<*.scm *.scheme *.ss *.rkt>);
if ( $#sources < 0 || ! -f $sources[0] )
{
    studentLog( "<p>Cannot identify a Scheme source file.\n",
		"Did you use a .rkt, .scm, .scheme, or .ss extension?</p>\n" );
    $can_proceed = 0;
}
else
{
    $student_src = $sources[0];
    if ( $#sources > 0 )
    {
	studentLog( "<p>Multiple Scheme source files present.  Using ",
		    "$student_src.\nIgnoring other Scheme files.",
		    "</p>\n" );
    }
}


#=============================================================================
# A subroutine for normalizing output lines before comparing them
#=============================================================================
sub normalize
{
    my $line = shift;
    $line =~ s/^\s+//o;		# Trim leading space
    $line =~ s/\s+$//o;		# Trim trailing space
    $line =~ s/\s+/ /go;	# Convert multi-space sequences to one space
    $line =~ s/\(\s+/(/go;	# Eliminate spaces inside parentheses
    $line =~ s/\)\s+/)/go;	# Eliminate spaces inside parentheses
    $line =~ tr/A-Z/a-z/;	# Convert to lower case
    return $line;
}

sub is_ref_test
{
    my $test_in = shift;
    foreach my $pred (@predicateList)
    {
        if ($test_in =~ m/^\((\Q$pred\E)\s/i)
        {
            return 1;
        }
    }
    return 0;
}


#=============================================================================
# A subroutine for executing a program/collecting the output
#=============================================================================
sub run_test
{
    my $pgm         = shift;
    my $temp_input  = shift;
    my $name        = shift;
    my $title       = shift;
    my $outfile     = shift;
    my $resultfile  = shift;
    my $show_details = shift;

# Produce stdin input file for programs to use:
open( INFILE, "> $temp_input" ) ||
    die "Cannot open '$temp_input': $!";
print INFILE<<EOF;
;; Some basic modules everyone may need
(require (lib "list.ss"))
(require (lib "etc.ss"))

;; Some global state for tracking test info
(define num-cases     0)
(define num-successes 0)
(define num-errors    0)

(define (SchemeTddPlugin-normalize x) x) ; default implementation
(define (SchemeTddPlugin-value-compare? x y)
 (cond
  [(null? x) (null? y)]
  [(null? y) #f]
  [(list? x) (if (list? y)
                 (and (SchemeTddPlugin-value-compare? (car x) (car y))
                      (SchemeTddPlugin-value-compare? (cdr x) (cdr y)))
                 #f)]
  [(and (number? x) (number? y))
   (cond
     [(or (inexact? x) (inexact? y))
      (let [(delta (abs (* 0.00001 (min x y))))]
        (> (if (= 0.0 delta) 0.00001 delta) (abs (- x y)))
      )]
     [else (= x y)]
   )]
  [else (equal? x y)]
  )
)

;; Redefine check-expect to do nothing
(define-syntax check-expect
  (syntax-rules ()
    ((check-expect x y) '())
  )
)


;; First, define a testing function that will trap and print out
;; any run-time errors that arise in user code:
(define (exec-test-case expr raw-expected num)
  (set! num-cases (+ 1 num-cases))
  (let ( [result   (with-handlers
                       ( [exn:fail? (lambda (e) e)] )
                     (SchemeTddPlugin-normalize (eval expr))) ]
         [expected (with-handlers
                       ( [exn:fail? (lambda (e) e)] )
                     (SchemeTddPlugin-normalize (eval raw-expected))) ] )
    (cond
      [(SchemeTddPlugin-value-compare? result expected)
       (display ".")
       (set! num-successes (+ 1 num-successes))]
      [else
       (begin
         (display "F")
         (newline)
         (display "case ")
         (display num)
         (display " FAILED: ")
         (write expr)
         (newline)
EOF
	if ( $show_details )
	{
	    print INFILE<<EOF;
         (display "  Expected: ")
         (write expected)
         (newline)
         (display "       Got: ")
	 (cond
	  [(exn? result)
	       (set! num-errors (+ 1 num-errors))
               (display "exception: ")
               (display (exn-message result))]
	  [else
             (write result)]
	     )
EOF
	}
	else
	{
	    print INFILE<<EOF;
         (display "  Incorrectly expected: ")
         (display expected)
EOF
	}
print INFILE<<EOF;
         (newline)
         )
       ])
    )
  )

;; Now, redirect output to the desired file:
(with-output-to-file "$outfile" (lambda ()
(with-handlers ( [exn:fail? (lambda (e)
			      (display "exception: ")
			      (display (exn-message e))
			      (newline)
			     )] )
    ;; Now load the user file, trapping any load-time errors:
    $pgm
EOF
# to counteract the ' in the output above, for emacs
for ( my $case_no = 0; $case_no <= $#test_cases; $case_no++ )
{
    my $case = $test_cases[$case_no][0];

    if ( $show_details || is_ref_test( $case ) )
    {

	# need to ensure $case has balanced parentheses
	my $lparens = ( () = $case =~ /\(/g );
	my $rparens = ( () = $case =~ /\)/g );
	if ( $lparens > $rparens )
	{
	    studentLog( "$student_src: unbalanced parentheses in test case ",
			$case_no, ":\n", $case );
	    $case .= ')' x ( $lparens - $rparens );
	}
	elsif ( $rparens > $lparens )
	{
	    studentLog( "$student_src: unbalanced parentheses in test case ",
			$case_no, ":\n", $case );
	    $case = ( '(' x ( $rparens - $lparens ) ) . $case;
	}

	print INFILE "(exec-test-case '", $case, " '", $test_cases[$case_no][1],
	    " ", ($case_no + 1), ")\n";
    }
}
print INFILE<<EOF;
 )

(newline)
(newline)
(display "Tests Run: ")
(display num-cases)
(display ", Errors: ")
(display num-errors)
(display ", Failures: ")
(display (- num-cases num-errors num-successes))
(display " (")
(let ([rate (if (= 0 num-cases)
	        0
	        (* 100 (/ num-successes num-cases)))])
 (if (integer? rate)
      (display rate)
  (display (exact->inexact (/ (round (* rate 100)) 100)))
  )
)
(display "%)")
(newline)
(with-handlers ( [exn:fail? (lambda (e) (void))] )
 (ref__coverage-results))
))
;; Exit from Scheme when done
(exit)
EOF
close( INFILE );

    # Exec program and collect output
    my $cmdline = $Web_CAT::Utilities::SHELL
        . "$scheme --load $temp_input 2>&1 > $outfile.err";
    print $cmdline, "\n" if ( $debug );
    my ( $exitcode, $timeout_status ) = Proc::Background::timeout_system(
        $timeout, $cmdline );
    if ( $timeout_status )
    {
        $timeout_occurred++;
    }


    #=========================================================================
    # Compare the output to test case expectations
    #=========================================================================

    if ( !$timeout_occurred )
    {
    my $line        = 0;    # next line in @expected_output to match
    my $last_failed = -1;   # index of last failed case
    my $num_cases   = 0;
    my $failures    = 0;    # count of failures
    my $errs        = 0;    # Number of runtime errors, which is 0 or 1 since
    			    # such an error crashes the program
    my $has_hints   = 0;

    open( STUDENT, "$outfile" ) ||
	die "Cannot open file for input '$outfile': $!";
    my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $resultfile );
    $feedbackGenerator->startFeedbackSection( $title, 1 );
    $feedbackGenerator->print( <<EOF );
<pre>
tddscm.pl v$version   (c) 2008 Virginia Tech. All rights reserved.
Testing $name using your embedded test cases

EOF
    while ( <STUDENT> )
    {
	s,\Q$working_dir\E(/?),,o;
	s,\Q$log_dir\E(/?),,o;
	while ( length( $_ ) > 78  &&  $_ =~ m/^[.F]*$/o )
	{
	    $feedbackGenerator->print( substr( $_, 0, 78 ) . "\n" );
	    $_ = substr( $_, 78 );
	}
	if ( m/tests run:\s*([0-9]+)/io )
	{
	    $num_cases = $1;
	}
	if ( m/errors:\s*([0-9]+)/io )
	{
	    $errs = $1;
	}
	if ( m/failures:\s*([0-9]+)/io )
	{
	    $failures = $1;
	}

	if ( !$has_hints && m/^hint:/io )
	{
	    $feedbackGenerator->print( "\n\n" );
	    $has_hints++;
	}

	if ( !$show_details && m/coverage:\s*(.*)$/io )
	{
	    $coverage = $1;
	}
	else
	{
		$_ =~ s/\&/\&amp;/g;
        $_ =~ s/</&lt;/g;
        $_ =~ s/>/&gt;/g;
	    $feedbackGenerator->print( $_ );
	}
    }
    $feedbackGenerator->print( "</pre>\n" );
    $feedbackGenerator->endFeedbackSection;
    $feedbackGenerator->close;
    close( STUDENT );

    my $succeeded = $num_cases - $failures - $errs;
    my $eval_score = ( $num_cases > 0 )
	? $succeeded/$num_cases
	: 0;
    return ( $eval_score, $succeeded, $num_cases );
    }
    else
    {
	return (0, 0, 1);
    }
}


if ( $can_proceed )
{

#=============================================================================
# Phase I: Parse and split the test case input file
#=============================================================================
open ( CASES, $student_src ) ||
    die "Cannot open '$student_src': $!";
my @src_lines = <CASES>;
close( CASES );

if ( $#src_lines >= 0
     && $src_lines[0] =~ m/^WXME[0-9][0-9][0-9][0-9]/ )
{
    $can_proceed = 0;
    studentLog<<EOF;
You appear to have uploaded a binary file, but only plain ASCII files
can be graded.  Perhaps your file is in DrScheme's native format.  If
so, use the File-&gt;Save Other-&gt;Save Definitions as Text... menu
command in DrScheme to create an ASCII-only version of your file
(save it under a different name to preserve your original work).
EOF
    # '
}

if ( $can_proceed )
{
    if ($#src_lines >= 0 && $src_lines[0] =~ /^;; The first three lines/o)
    {
        shift @src_lines;
        if ($#src_lines >= 0 && $src_lines[0] =~ /^;; about the language level/o)
        {
            shift @src_lines;
            if ($#src_lines >= 0 && $src_lines[0] =~ /^\#reader/o)
            {
                shift @src_lines;
            }
        }
    }
    my $src = join( "", @src_lines );

    my @studentCode = ();
    my $lineNo = 1;
    my $exprString = '';

    while ( $src ne "" )
    {
	    my ($expr, $pos, $err) = lisp_read( $src, 1, 0, 1 );
	    if ( !$err )
	    {
            $exprString .= substr( $src, 0, $pos );
            $src = substr( $src, $pos );
	        my $printableExpr = lisp_print( $expr );
	        if ( $printableExpr =~ /^\(check-expect\s/o )
	        {

		        # got a test case, so $lastExpr is the expected output
                # print "found a test case\n";
                push( @test_cases, [ lisp_print( $expr->[1] ),
                                     lisp_print( $expr->[2] ) ] );
	        }
    	}
	    elsif ( $pos == 0 )
	    {
	        $pos++;
            $exprString .= substr( $src, 0, $pos );
            $src = substr( $src, $pos );
	    }
	    $lineNo += 0 + ( $exprString =~ s/\n/\n/go );
	    push( @studentCode, $exprString );
	    $exprString = '';
	    if ( $src =~ m/^[\s\n]+/o )
	    {
	        $exprString .= $&;
	        $src = $'; #'
	    }
    }

    push( @studentCode, $exprString );
    $lineNo += 0 + ( $exprString =~ s/\n/\n/go );
    my $studentCode = join( "", @studentCode );
    open ( CODE, ">$student_code" ) ||
	die "Cannot open '$student_code': $!";
    print CODE $studentCode;
    close( CODE );



#=============================================================================
# Phases II and III: Execute the program and produce results
#=============================================================================
@student_eval = run_test( "(load \"$student_code\")",
			  $test_input,
			  "your submission",
		  "Program Correctness (Results From Your Tests on Your Code)",
			  $student_output,
			  $student_rpt, 1 );
$cfg->setProperty( "studentEval", join( " ", @student_eval ) );

if ( $timeout_occurred )
{
	studentLog( "Your program exceeded the allowable time limit.\n",
		    "This may be due to an infinite recursion problem or ",
		    "and infinite looping problem.\n" );
}
elsif ( $student_eval[1] == $student_eval[2] )
{
    @instr_eval   = run_test( "(load \"$student_code\") (load \"$instr_src\")",
			      $ref_input,
			      "reference implementation",
      "Test Validity (Results From Your Tests on the Reference Solution)",
			      $instr_output,
			      $instr_rpt, 0 );

    if ( !$timeout_occurred )
    {
	# open( COVERAGE, "coverage.txt" ) ||
	#     die "Cannot open 'coverage.txt': $!";
	# $coverage = <COVERAGE>;
	# chomp $coverage;
	# close( COVERAGE );

	#open( ASSESSMENT, ">$assessment" ) ||
	#    die "Cannot open 'coverage.txt': $!";
	#print ASSESSMENT
	#    join( " ", @student_eval ), "\n",
	#    join( " ", @instr_eval   ), "\n",
	#    "$coverage\n";
	#close( ASSESSMENT );
	$cfg->setProperty( "instructorEval", join( " ", @instr_eval ) );
	$cfg->setProperty( "coverage",       $coverage );
    }
    else
    {
	print STDERR "reference implementation exceeded timeout!\n";
    }
}
else
{
    studentLog( "\nYour program must pass all of your test cases ",
		"before you can receive\na non-zero score and before ",
		"the reference implementation will be run.\n" );
	$cfg->setProperty( "instructorEval", "0 0 1" );
	$cfg->setProperty( "coverage",       $coverage );
#    open( ASSESSMENT, ">$assessment" ) ||
#	die "Cannot open 'coverage.txt': $!";
#    print ASSESSMENT
#	join( " ", @student_eval ), "\n",
#	"0 0 1\n",
#	"$coverage\n";
#    close( ASSESSMENT );
}
}
}

#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================

my $reportCount = $cfg->getProperty( 'numReports', 0 );
if ( -f $script_log ) # ( stat( $script_log )->size != 0 )
{
	studentLog("</div></div>");
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $script_log_relative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
}
if ( 0 ) #( $can_proceed )
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
if (    !$can_proceed
     || $timeout_occurred
     || $student_eval[1] != $student_eval[2] )
{
    $cfg->setProperty( "score.correctness", 0 );
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

