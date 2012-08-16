#!/opt/local/bin/perl -w
#=============================================================================
#   @(#)$Id: pldoctest.pl,v 1.2 2011/11/04 16:06:49 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Test-driven development script for Prolog predicates.  See course web
#   page for details on test case format and usage.
#
#   usage summary:
#       pldoctest.pl [-d] [-v] <prolog-src-file-name>
#=============================================================================

use strict;
use English;

my $version     = "1.1";
my $verbose     = 0;
my $debug       = 0;
my $deleteTemps = 0;          # Change to 0 to preserve temp files


while ($#ARGV >= 0)
{
    my $arg = shift @ARGV;
    if ($arg eq '-d')
    {
        $debug++;
        $deleteTemps = 1;
    }
    elsif ($arg eq '-v')
    {
        $verbose = 1;
    }
    else
    {
        unshift(@ARGV, $arg);
        last;
    }
}
if ($#ARGV != 0)
{
    die "usage:\n" .
	    "    pldoctest.pl [-d] [-v] <prolog-src-file-name>\n";
}
print "pldoctest.pl v$version   (c) 2011 Virginia Tech. All rights reserved.\n";


#=============================================================================
# User-configurable paths: You may need to edit these definitions to
# reflect where Prolog is installed on your system.  Note that spaces
# shouldn't be used in these path names, since they'll confuse system()
# unless they are properly escaped.
#
# For linux users, if "pl" is already on your path, you can try setting
# $prolog to "pl" (or use a fully qualified path).
#
# For Windows 95/98/Me users, note that you must use backslash (\) as your
# path separator, as shown in the commented-out second-line below.
#=============================================================================
my $prolog = 'c:/Progra~1/SWI-Prolog/bin/plcon';    # For Windows XP/2000/etc.
# my $prolog = 'c:\Progra~1\SWI-Prolog\bin\plcon';  # For Windows 95/98/Me
# my $prolog = 'swipl';                             # For *nix

if (! -f $prolog && ! -f "$prolog.exe")
{
    # If not found in the normal Windows location, default to unix instead
    $prolog = 'swipl';
}

#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $studentSrc   = $ARGV[0];    # source file name

print "Testing $studentSrc\n";


#=============================================================================
# In addition, some local definitions within this script
#=============================================================================
my @testCases      = ();       # test case input
my @expectedOutput = ();         # corresponding expected output
my @caseNos        = ();         # test case number for each output line
my $tempInput      = "$PID.in";  # Name for temp test input file
my $tempOutput     = "$PID.out"; # Name for temp test output file


#=============================================================================
# A subroutine for normalizing output lines before comparing them
#=============================================================================
sub normalize
{
    my $line = shift;
    $line =~ s/^\s+//o;        # Trim leading space
    $line =~ s/\s+$//o;        # Trim trailing space
    $line =~ s/\s+/ /go;       # Convert multi-space sequences to one space
    $line =~ s/\(\s+/(/go;     # Eliminate spaces inside parentheses
    $line =~ s/\)\s+/)/go;     # Eliminate spaces inside parentheses
    $line =~ tr/A-Z/a-z/;      # Convert to lower case
    return $line;
}


#=============================================================================
# Phase I: Parse and split the test case input file
#=============================================================================

open (CASES, $studentSrc) ||
    die "Cannot open '$studentSrc': $!";

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
                die 'unbalanced multi-line comment ending on line '
                    . "$lineNo\n";
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
    die "Runaway multi-line comment in source file.\n";
}

print $case + 1, " test cases identified.\n" if ($verbose);

## For debugging purposes:
# print "labels => '", join( "', '", @labels ), "'\n";
if ($debug)
{
    print "inputs => '", join("', '", @testCases), "'\n";
    print "outputs => '", join("', '", @expectedOutput), "'\n";
    print "caseNos => '", join("', '", @caseNos), "'\n";
}


#=============================================================================
# Phase II: Execute the program
#=============================================================================

open(INFILE, ">$tempInput") ||
    die "Cannot open '$tempInput': $!";
print INFILE<<EOF;
/*======================================================================*\\
 * pldoctest.pl: Automatically generated test driver
 *
 * Test cases taken from source file: $studentSrc
 *
 * This file was automatically generated.  To find the actual human-
 * written test cases themselves, scan down to the comment that contains:
 *
 * TEST CASES BEGIN HERE
 *
\\*======================================================================*/

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
    [ '$studentSrc:', Line, ': ' | Stripped_Rest ] ) :-
    integer( Line ),
    Line >= 0, !,
    strip_stream_refs( Rest, Stripped_Rest ).
strip_stream_refs( ['Stream ~w:~d: '-_ | Rest ],
    [ '$studentSrc: ' | Stripped_Rest ] ) :-
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

write_expected_output( [] ) :-
    write( '[fail]' ), !.
write_expected_output( [[]] ) :-
    write( '[true]' ), !.
write_expected_output( T ) :-
    write( T ).

pldoctest_normalize(X, X).

pldoctest_normalize_list([], []).
pldoctest_normalize_list([H | R], [Hnorm | Rnorm]) :-
    pldoctest_normalize(H, Hnorm),
    pldoctest_normalize_list(R, Rnorm).
pldoctest_normalize_list(L, Lnorm) :-
    L \= [_|_], L\= [], pldoctest_normalize(L, Lnorm).

expected_output_takeout( X, [ Z | R ], R ) :-
    pldoctest_normalize_list(X, Xnorm),
    pldoctest_normalize_list(Z, Znorm),
    ( is_list( Xnorm ), is_list( Znorm ) ->
       intersection( Xnorm, Znorm, Xnorm ); Znorm = Xnorm ), !.
expected_output_takeout( X, [ Z1 | R ], [ Z2 | S ] ) :-
    pldoctest_normalize(Z1, Znorm),
    pldoctest_normalize(Z2, Znorm),
    expected_output_takeout( X, R, S ).
expected_output_equivalent( [], [] ).
expected_output_equivalent( [ X | R ], S ) :-
    expected_output_takeout( X, S, T ),
    expected_output_equivalent( R, T ).

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
      write( '  Expected: ' ), write_expected_output( Output_As_Atom ), nl,
      write( '       Got: ' ), write( 'ERROR: ' ),
      write( 'free variables not in the input appear in the output' ), nl
    ).
testcase_output( _Label, _N, [],     [fail], _Bindings ) :- write( '.' ).
testcase_output( _Label, _N, [[]],   [true], _Bindings ) :- write( '.' ).
testcase_output( _Label, _N, Output, Expected, _Bindings ) :-
    expected_output_equivalent( Output, Expected ),
    write( '.' ).
testcase_output( Label,   N, Output, Expected, Bindings ) :-
    catch( ( make_readable( Expected, Bindings, Output_As_Atom ) ;
             Output_As_Atom = Expected ),
           _Exception,
           Output_As_Atom = Expected
    ),
    write( 'F' ), nl,
    write( 'case ' ), write( N ), write( ' FAILED: ' ), write( Label ), nl,
    write( '  Expected: ' ), write_expected_output( Output_As_Atom ), nl,
    write( '       Got: ' ), write_expected_output( Output ), nl.

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
   tell( '$tempOutput' ).

/* Load given input file, bail if any problems arise */
% ?- load_files( ['$studentSrc'], [silent(true)] ).
?- ['$studentSrc'].
?- style_check( [ -singleton, -atom ] ).


/*======================================================================*\\
 * TEST CASES BEGIN HERE
\\*======================================================================*/

/* Run test cases one at a time using testcase/2 */
EOF

# to counteract the ' in the output above, for emacs

for (my $c = 0; $c <= $#testCases; $c++)
{
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
print INFILE<<EOF;

/* Exit from Prolog when done */
?- told, halt.
EOF
close(INFILE);
system("$prolog -f $tempInput -t halt.");
if ($deleteTemps)
{
    unlink("$tempInput");
}


#=============================================================================
# Phase III: Compare the output to test case expectations
#=============================================================================

my $failures = 0;    # count of failures
my $errs     = 0;    # Number of runtime errors
my $foundDot = 0;

print "\n";
open(STUDENT, "$tempOutput") ||
    die "Cannot open temporary output file '$tempOutput': $!";

while (<STUDENT>)
{
    if (/FAILED:/o)
    {
        $failures++;
    }
    elsif (/ERROR:/o)
    {
        $errs++;
        $failures--;
    }
    if (/^\./o)
    {
        $foundDot = 1;
    }
    s/(\.{78})/$1\n/go;
    print;
}

close(STUDENT);

my $numCases = $#testCases + 1;
if ($errs > $numCases)
{
    $errs = $numCases;
}
if (!$foundDot)
{
    $failures = $numCases - $errs;
}
print "\n\nTests Run: $numCases, Errors: $errs, Failures: $failures (",
    sprintf("%.1f", ($numCases - $failures - $errs) / $numCases * 100),
    "%)\n";
if ($failures + $errs > 0 || ! $foundDot)
{
    print "Output has been saved in $tempOutput.\n";
}
elsif ($deleteTemps)
{
    unlink($tempOutput);
}


#=============================================================================
# Exit Script
#=============================================================================
exit(0);
