#!c:\perl\bin\perl.exe -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.2 2011/11/04 16:07:11 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Curator: execution script for Free Pascal submissions
#
#   usage:
#       executePascal.pl <properties-file>
#=============================================================================
use strict;
use English;
use File::stat;
use File::Copy;
use Proc::Background;
use Config::Properties::Simple;
use Web_CAT::Beautifier;

#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $propfile    = $ARGV[0];	# property file name
my $cfg         = Config::Properties::Simple->new( file => $propfile );

my $pid         = $cfg->getProperty( 'userName' );
my $workingDir	= $cfg->getProperty( 'workingDir' );
my $scriptHome	= $cfg->getProperty( 'scriptHome' );
my $resultDir   = $cfg->getProperty( 'resultDir' );
my $timeout	    = $cfg->getProperty( 'timeoutForOneRun', 30 );


#-------------------------------------------------------
# In the future, these could be set via parameters set in Web-CAT's
# interface
#-------------------------------------------------------
my $debug              =  $cfg->getProperty( 'debug', 0 );
my $maxScore          = $cfg->getProperty( 'max.score.correctness', 0 );
my $hintsLimit         = $cfg->getProperty( 'hintsLimit', 3 );
my $predicateList      = $cfg->getProperty( 'predicateList', "" );
if ( !defined( $predicateList ) || $predicateList eq "" )
{
    print STDERR "predicateList is undefined.\n";
}
my $verbose = $debug;


#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
my $script_log_relative      = "execute-script-log.txt";
my $script_log               = "$resultDir/$script_log_relative";
my $test_input_relative      = "tests-in.txt";
my $test_input               = "$resultDir/$test_input_relative";
my $instr_output_relative    = "instr-out.txt";
my $instr_output             = "$resultDir/$instr_output_relative";
my $student_output_relative  = "output.txt";
my $student_output           = "$resultDir/$student_output_relative";
my $student_rpt_relative     = "student-tdd-report.txt";
my $student_rpt              = "$resultDir/$student_rpt_relative";
my $instr_rpt_relative       = "instr-tdd-report.txt";
my $instr_rpt                = "$resultDir/$instr_rpt_relative";
my $assessment_relative      = "TDD-assessment.txt";
my $assessment               = "$resultDir/$assessment_relative";

my $studentSrc               = "";

my $can_proceed              = 1;
my $timeout_occurred         = 0;
my @student_eval             = ();
my @instr_eval               = ();
my $studentTests            = ""; # test case file
my $coverage                 = "0 0 1";
# my @beautifierIgnoreFiles = ( '.java' );

# From tddpas.pl
#-----------------------------
my $version         = "1.1";
my @testCases       = ();    # test case input
my @expectedOutput  = ();    # corresponding expected output
my @caseNos         = ();    # test case number for each output line
my $deleteTemps     = 0;     # Change to 0 to preserve temp files


# Prolog interpreter to use
#-----------------------------
my $prolog = "C:\\Progra~1\\SWI-Prolog\\bin\\plcon.exe";
if ( ! -x $prolog )
{
    $prolog = "G:\\SWI-Prolog\\bin\\plcon.exe";
    if ( ! -x $prolog )
    {
        # assume unix
        $prolog = "swipl";
#        die "Cannot identify correct prolog interpreter ($prolog)";
    }
}


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
    $target =~ s,\\,/,g;
#    foreach my $sddir ( @scriptDataDirs )
#    {
#    my $target = $sddir ."/$subpath";
#    #print "checking $target\n";
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
chdir( $workingDir );
$workingDir =~ s,\\,/,g;

sub studentLog
{
    open( SCRIPT_LOG, ">>$script_log" ) ||
    die "cannot open $script_log: $!";
    print SCRIPT_LOG @_;
    close( SCRIPT_LOG );
}


#=============================================================================
# Find the test suite file to use
# Extensions supported *.txt, *.text, *.tst, *.test
#=============================================================================
my @sources = (<*.plg *.pro *.prolog *.pl>);
if ( $#sources < 0 || ! -f $sources[0] )
{
    studentLog(    "<p>Cannot identify a Prolog source file.\n",
        "Did you use the correct extension (.plg, .pro, ",
        ".prolog, or .pl)?</p>\n" );
    $can_proceed = 0;
}
else
{
    $studentSrc  = $sources[0];
    $studentTests = $studentSrc ;
    if ( $#sources > 0 )
    {
        studentLog( "<p>Multiple Prolog source files present.  Using ",
            "$studentSrc .\nIgnoring other Prolog files.</p>\n" );
    }
}


if ( $can_proceed && -d "$scriptHome/local" )
{
    foreach my $f ( <$scriptHome/local/*> )
    {
        my $newf = $f;
        $newf =~ s,^\Q$scriptHome/local/\E,,;
        print "copy( $f, $newf );\n" if $debug;
        copy( $f, $newf );
#       push( @beautifierIgnoreFiles, $newf );
    }
}



#=============================================================================
# A subroutine for normalizing output lines before comparing them
#=============================================================================
sub normalize
{
    my $line = shift;
    $line =~ s/^\s+//o;       # Trim leading space
    $line =~ s/\s+$//o;       # Trim trailing space
    $line =~ s/\s+/ /go;      # Convert multi-space sequences to one space
    $line =~ s/\(\s+/(/go;    # Eliminate spaces inside parentheses
    $line =~ s/\)\s+/)/go;    # Eliminate spaces inside parentheses
    #$line =~ tr/A-Z/a-z/;    # Convert to lower case
    return $line;
}

sub is_ref_test
{
    my $test_in = shift;
    return $test_in =~ m/^\s*($predicateList)\(/io;
}


#=============================================================================
# A subroutine for executing a program/collecting the output
#=============================================================================
sub run_test
{
    my $pgm          = shift;
    my $temp_input   = shift;
    my $name         = shift;
    my $title        = shift;
    my $outfile      = shift;
    my $resultfile   = shift;
    my $show_details = shift;

# Produce stdin input file for programs to use:
open( INFILE, "> $temp_input" ) ||
    die "Cannot open '$temp_input': $!";
print INFILE<<EOF;
/* send error messages to current output */
message_hook( _, silent,        _ ) :- !.
message_hook( _, informational, _ ) :- !.
message_hook( _, banner,        _ ) :- !.
message_hook( _Term, Level, OrigLines ) :-
    strip_stream_refs( OrigLines, Lines ),
    '\$messages':prefix(Level, LinePrefix, _ ), !,
    current_output( Stream ),
    print_message_lines( Stream, LinePrefix, Lines ).
strip_stream_refs( [], [] ).
strip_stream_refs( ['Stream ~w:~d: '-[_Stream, Line] | Rest ],
           [ '$pgm:', Line, ': ' | Stripped_Rest ] ) :-
    integer( Line ),
    Line >= 0, !,
    strip_stream_refs( Rest, Stripped_Rest ).
strip_stream_refs( ['Stream ~w:~d: '-_ | Rest ],
           [ '$pgm: ' | Stripped_Rest ] ) :-
    strip_stream_refs( Rest, Stripped_Rest ).
strip_stream_refs( [ H | Rest ], [ H | Stripped_Rest ] ) :-
    strip_stream_refs( Rest, Stripped_Rest ).
error_output( Label, N, Error, Output ) :-
    message_to_string( Error, Message_String ), !,
    string_to_atom( Message_String, Message_Atom ),
    concat( 'ERROR: ', Message_Atom, Message ),
    testcase_output( Label, N, Message, Output, [] ).

make_readable( Var, [ Name=Binding | More_Bindings ], VarName ) :-
    var( Var ),
    ( Var == Binding, Name = VarName ;
      make_readable( Var, More_Bindings, VarName ) ).
make_readable( Var=Exp, Bindings, VarName=Exp ) :-
    make_readable( Var, Bindings, VarName ).
make_readable( [], _Bindings, [] ).
make_readable( [ H | T ], Bindings, [ NewH | NewT ] ) :-
    make_readable( H, Bindings, NewH ),
    make_readable( T, Bindings, NewT ).

contained_in_free_vars( [], _ ).
contained_in_free_vars( [ Var=_ | More ], List ) :-
    member( Var=_, List ),
    contained_in_free_vars( More, List ).

write_expectedOutput( [] ) :-
    write( '[fail]' ), !.
write_expectedOutput( [[]] ) :-
    write( '[true]' ), !.
write_expectedOutput( T ) :-
    write( T ).

prologTDDPlugin_normalize(X, X).

prologTDDPlugin_normalize_list([], []).
prologTDDPlugin_normalize_list([H | R], [Hnorm | Rnorm]) :-
    prologTDDPlugin_normalize(H, Hnorm),
    prologTDDPlugin_normalize_list(R, Rnorm).
prologTDDPlugin_normalize_list(L, Lnorm) :-
    L \= [_|_], L\= [], prologTDDPlugin_normalize(L, Lnorm).


expectedOutput_takeout( X, [ Z | R ], R ) :-
    prologTDDPlugin_normalize_list(X, Xnorm),
    prologTDDPlugin_normalize_list(Z, Znorm),
    ( is_list( Xnorm ), is_list( Znorm ) ->
       intersection( Xnorm, Znorm, Xnorm ); Znorm = Xnorm ), !.
expectedOutput_takeout( X, [ Z1 | R ], [ Z2 | S ] ) :-
    prologTDDPlugin_normalize(Z1, Znorm),
    prologTDDPlugin_normalize(Z2, Znorm),
    expectedOutput_takeout( X, R, S ).
expectedOutput_equivalent( [], [] ).
expectedOutput_equivalent( [ X | R ], S ) :-
    expectedOutput_takeout( X, S, T ),
    expectedOutput_equivalent( R, T ).

testcase_output( Label, N, Output, [ Expected, Free ] ) :-
    atom_to_term( Expected, Unsorted_Term, Bindings ),
    sort( Unsorted_Term, Term ),
    sort( Free, Free_Sorted ),
    sort( Bindings, Bindings_Sorted ),
    ( contained_in_free_vars( Bindings_Sorted, Free_Sorted ) ->
      testcase_output( Label, N, Output, Term, Bindings ) ;
      write( 'F' ), nl,
      write( 'case ' ), write( N ), write( ' FAILED: ' ), write( Label ), nl,
      catch( ( make_readable( Expected, Bindings, Output_As_Atom ) ;
             Output_As_Atom = Expected ),
           _Exception,
           Output_As_Atom = Expected
      ),
      write( '  Expected: ' ), write_expectedOutput( Output_As_Atom ), nl,
      write( '       Got: ' ), write( 'ERROR: ' ),
      write( 'free variables not in the input appear in the output' ), nl
    ).
testcase_output( _Label, _N, [],     [fail], _Bindings ) :- write( '.' ).
testcase_output( _Label, _N, [[]],   [true], _Bindings ) :- write( '.' ).
testcase_output( _Label, _N, Output, Expected, _Bindings ) :-
    expectedOutput_equivalent( Output, Expected ),
    write( '.' ).
testcase_output( Label,   N, Output, Expected, Bindings ) :-
    catch( ( make_readable( Expected, Bindings, Output_As_Atom ) ;
             Output_As_Atom = Expected ),
           _Exception,
           Output_As_Atom = Expected
    ),
    write( 'F' ), nl,
    write( 'case ' ), write( N ), write( ' FAILED: ' ), write( Label ), nl,
    write( '  Expected: ' ), write_expectedOutput( Output_As_Atom ), nl,
    write( '       Got: ' ), write_expectedOutput( Output ), nl.

testcase( Label, N, Case, Expected ) :-
    catch(
    (
        atom_to_term( Case, Predicate, Bindings ),
        % write( Bindings ), nl,
        % write( Predicate ), nl, nl,
        ( setof( Bindings, Predicate, Solutions ) ; Solutions = [] ),
        testcase_output( Label, N, Solutions, [ Expected, Bindings ] )
    ),
    Exception,
    error_output( Label, N, Exception, Expected )
    ).
testcase( N, Case, Expected ) :-
    testcase( Case, N, Case, Expected ).

?- set_feature( debug_on_error, false ),
   tell( '$outfile' ).

/* Load given input file, bail if any problems arise */
% ?- load_files( ['$pgm'], [silent(true)] ).
?- ['$pgm'].
?- style_check( [ -singleton, -atom ] ).

/* Run test cases one at a time using testcase/2 */
EOF
# to counteract the ' in the output above, for emacs
my $num_cases = 0;
for ( my $c = 0; $c <= $#testCases; $c++ )
{
    if ( $show_details || is_ref_test( $testCases[$c] ) )
    {
        $num_cases++;
        print INFILE "?- testcase(";
        print INFILE $c + 1, ",\n'", $testCases[$c], "',\n";
        if ($expectedOutput[$c] eq "")
        {
            $expectedOutput[$c] = "true";
        }
        elsif ($expectedOutput[$c] eq "false")
        {
            $expectedOutput[$c] = "fail";
        }
        elsif ($expectedOutput[$c] eq "succeed")
        {
            $expectedOutput[$c] = "true";
        }
        print INFILE "'[", $expectedOutput[$c], "]').\n\n";
    }
}
if ( $name =~ /reference/io )
{
    print INFILE<<EOF;

/* generate coverage report */
?- report_coverage.
?- print_hints( $hintsLimit ).
EOF
}
print INFILE<<EOF;

/* Exit from Prolog when done */
?- told, halt.
EOF
close( INFILE );

    # Exec program and collect output
    my $cmdline = $Web_CAT::Utilities::SHELL
        . "$prolog -f $temp_input -t halt.";
    print $cmdline, "\n" if ($debug);
    my ($exitcode, $timeout_status) = Proc::Background::timeout_system(
        $timeout, $cmdline);
    if ($timeout_status)
    {
        $timeout_occurred++;
    }


    #=========================================================================
    # Compare the output to test case expectations
    #=========================================================================

    my $failures    = 0;    # count of failures
    my $errs        = 0;    # Number of runtime errors
    my $found_dot   = 0;

    open( STUDENT, "$outfile" ) ||
    die "Cannot open file for input '$outfile': $!";
    open( RESULT, ">$resultfile" ) ||
    die "Cannot open file for output '$resultfile': $!";

    print RESULT <<EOF;
<div class="module"><div dojoType="webcat.TitlePane" title="$title">
<pre>
pldoctest.pl v$version   (c) 2011 Virginia Tech. All rights reserved.
Testing $name using $studentTests

EOF
    while ( <STUDENT> )
    {
        if ( /FAILED:/o )
        {
            $failures++;
        }
        elsif ( /ERROR:/o )
        {
            $errs++;
            $failures--;
        }
        elsif ( s/^coverage:\s+//o )
        {
            chomp;
            $coverage = $_;
            next;
        }
        if ( /^\./o )
        {
            $found_dot = 1;
        }
        s/\Q$instr_src\E/&lt;reference solution&gt;/gio;
        s/\Q$workingDir\E(\\|\/)?//gio;
        s/(\.{78})/$1\n/go;
        print RESULT $_;
    }

    close( STUDENT );

    if ( $errs > $num_cases )
    {
        $errs = $num_cases;
    }
    if ( !$found_dot )
    {
        $failures = $num_cases - $errs;
    }
    my $succeeded = $num_cases - $failures - $errs;
    my $eval_score = ( $num_cases > 0 )
        ? $succeeded/$num_cases
        : 0;
    print RESULT
        "\nTests Run: $num_cases, Errors: $errs, Failures: $failures (",
        sprintf( "%.1f", $eval_score*100 ),
        "%)\n</pre></div></div>\n";
    close( RESULT );
    return ( $eval_score, $succeeded, $num_cases );
}


if ( $can_proceed )
{

#=============================================================================
# Phase I: Parse and split the test case input file
#=============================================================================
open ( CASES, $studentTests ) ||
    die "Cannot open '$studentTests': $!";

# Mini state machine for parsing input.  States are:
use constant IN_PROLOG_CODE             => 0;
use constant IN_MULTILINE_COMMENT       => 1;
use constant IN_PLDOC_COMMENT           => 2;
use constant IN_EXAMPLE_GOAL            => 3;
use constant IN_EXAMPLE_EXPECTED_RESULT => 4;
my @state = (IN_PROLOG_CODE);

my $case       = -1;
my $lineNo     = 0;
my $goalIndent = 0;

while (<CASES>)
{
    my $line = $_;
    $lineNo++;
    chomp;
    $_ =~ s/[\r\n]+$//so;
    print $line if ($debug);

#    print "start: [", join(", ", @state), "]: $line";

    # Repeat until the whole line is exhausted
    while ($_ ne '')
    {
        my $text = $_;
        my $state = $state[$#state];
        my $startComment = 0;
        my $endComment = 0;

        if (s,^((?:(?!\/\*|\*\/).)*)(\/\*\*?|\*\/),,o)
        {
            $text = '';
            my $before = $_;
            if (defined $1)
            {
                $text = $1;
            }
            print "SPLIT: '$before' into '$text' then '$2' then '$_'\n"
                if ($debug);
            if ($2 eq '*/')
            {
                $endComment++;
            }
            else
            {
                $startComment = ($state == IN_PROLOG_CODE && $2 eq '/**')
                    ? IN_PLDOC_COMMENT
                    : IN_MULTILINE_COMMENT;
            }
        }
        else
        {
            $_ = '';
        }

        if (($state == IN_EXAMPLE_EXPECTED_RESULT || $state == IN_EXAMPLE_GOAL)
            && $text =~ m/^\s*(\*\s*)?\?-/o)
        {
            pop(@state);
            $state = $state[$#state];
        }

        if ($state == IN_PLDOC_COMMENT && $text =~ s/^(\s*(?:\*\s*)?)\?-\s*//o)
        {
            $goalIndent = (defined $1) ? length($1) : 0;
            push(@state, IN_EXAMPLE_GOAL);
            push(@testCases, $text);
            push(@expectedOutput, '');
            $case++;
            push(@caseNos, $case);
            print "case ", $case + 1, ": GOAL => $text\n" if ($verbose);
        }
        elsif ($state == IN_EXAMPLE_GOAL && $text =~ s/^(\s*(?:\*\s*)?)//o)
        {
            my $thisIndent = (defined $1) ? length($1) : 0;
            if ($thisIndent > $goalIndent)
            {
                # Continuation of goal
                $testCases[$#testCases] .= "\n" . $text;
                print "    $text\n" if ($verbose);
            }
            else
            {
                # beginning of expected result
                pop(@state);
                push(@state, IN_EXAMPLE_EXPECTED_RESULT);
                if ($text ne '')
                {
                    if ($expectedOutput[$#expectedOutput] ne '')
                    {
                        $expectedOutput[$#expectedOutput] .= "\n";
                    }
                    $expectedOutput[$#expectedOutput] .= $text;
                }
                $testCases[$#testCases] =~ s/\.\s*$//so;
                print "case ", $case + 1, ": EXPECTED => $text\n" if ($verbose);
            }
        }
        elsif ($state == IN_EXAMPLE_EXPECTED_RESULT)
        {
            $text =~ s/^(\s*(?:\*\s*)?)//o;
            if ($text eq '')
            {
                pop(@state);
            }
            else
            {
                if ($expectedOutput[$#expectedOutput] ne '')
                {
                    $expectedOutput[$#expectedOutput] .= "\n";
                }
                $expectedOutput[$#expectedOutput] .= $text;
                print "    $text\n" if ($verbose);
            }
        }

        if ($endComment)
        {
            while ($#state >= 0 && $state[$#state] > IN_PLDOC_COMMENT)
            {
                pop(@state);
            }
            if ($#state >= 0)
            {
                pop(@state);
            }
            if ($#state < 0)
            {
                studentLog '<p>unbalanced multi-line comment ending on line '
                    . "$lineNo</p>\n";
                push(@state, IN_PROLOG_CODE);
            }
        }
        if ($startComment)
        {
            push(@state, $startComment);
        }

        print "[$state : " . join(', ', @state) . "]$text" if ($debug);
    }
}


if ($#caseNos < 0 || $caseNos[$#caseNos] != $case)
{
    push(@caseNos, $case);
}

if ($#state > 0 || $state[0] != 0)
{
    studentLog "<p>Runaway multi-line comment in source file.</p>\n";
}

print $case + 1, " test cases identified.\n" if ($verbose);


#=============================================================================
# Phases II and III: Execute the program and produce results
#=============================================================================
if ( $can_proceed )
{
@student_eval = run_test( $studentSrc ,
              $test_input,
              "your submission",
          "Program Correctness (Results from Your Tests on Your Code)",
              $student_output,
              $student_rpt, 1 );
$cfg->setProperty( "studentEval", join( " ", @student_eval ) );

if ( $timeout_occurred )
{
    studentLog( "<p>Your program exceeded the allowable time limit.\n",
            "This may be due to an infinite recursion problem or ",
            "and infinite looping problem.</p>\n" );
}
elsif ( $student_eval[1] == $student_eval[2] )
{
    @instr_eval   = run_test( $instr_src,
                  $test_input,
                  "reference implementation",
      "Test Validity (Results From Your Tests on the Reference Solution)",
                  $instr_output,
                  $instr_rpt, 0 );

    if ( !$timeout_occurred )
    {
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
    studentLog( "<p>Your program must pass all of your test cases ",
        "before you can receive a non-zero score and before ",
        "the reference implementation will be run.</p>\n" );
    $cfg->setProperty( "instructorEval", "0 0 1" );
    $cfg->setProperty( "coverage",       $coverage );
#    open( ASSESSMENT, ">$assessment" ) ||
#    die "Cannot open 'coverage.txt': $!";
#    print ASSESSMENT
#    join( " ", @student_eval ), "\n",
#    "0 0 1\n",
#    "$coverage\n";
#    close( ASSESSMENT );
}

#my $beautifier = new Web_CAT::Beautifier;
#$beautifier->beautifyCwd( $cfg, \@beautifierIgnoreFiles );

}
}


#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================

my $reportCount = $cfg->getProperty( 'numReports', 0 );
if ( -f $script_log && stat( $script_log )->size != 0 )
{
    open(SCRIPT_LOG, $script_log) ||
        die "cannot open $script_log: $!";
    my @lines = <SCRIPT_LOG>;
    close(SCRIPT_LOG);

    open(SCRIPT_LOG, ">$script_log") ||
        die "cannot open $script_log: $!";
    print SCRIPT_LOG <<EOF;
<div class="module"><div dojoType="webcat.TitlePane" title="Problems with Your Tests">
@lines
</div></div>
EOF
    close(SCRIPT_LOG);

    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $script_log_relative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
    $cfg->setProperty( "report${reportCount}.styleVersion", "1" );
}
#if ( $can_proceed )
#{
#    $reportCount++;
#    $cfg->setProperty( "report${reportCount}.file", $student_rpt_relative );
#    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );

#    $reportCount++;
#    $cfg->setProperty( "report${reportCount}.file", $instr_rpt_relative );
#    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );

#    $reportCount++;
#    $cfg->setProperty( "report${reportCount}.file",
#               $student_output_relative );
#    $cfg->setProperty( "report${reportCount}.mimeType", "text/plain" );
#    $cfg->setProperty( "report${reportCount}.inline",   "false" );
#    $cfg->setProperty( "report${reportCount}.label",
#               "Your submission's output" );
#}
if ( !$can_proceed
     || $timeout_occurred
     || $student_eval[1] != $student_eval[2] )
{
    $cfg->setProperty( "score.correctness", 0 );
    if ( $timeout_occurred )
    {
    $cfg->setProperty( "timeoutOccurred", 1 );
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
