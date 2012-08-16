#!c:\perl\bin\perl.exe -w
#=============================================================================
#   @(#)$Id: tddplg.pl,v 1.1 2011/10/26 15:53:18 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Test-driven development script for Prolog predicates.  See course web
#   page for details on test case format and usage.
#
#   usage summary:
#       tddplg.pl <prolog-src-file-name> <test-case-file-name> 
#=============================================================================

use strict;
use English;

if ( $#ARGV != 1 )
{
    die "usage:\n" .
	"    tddplg.pl <src-file-name> <test-case-file-name>\n";
}

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
my $prolog = 'c:/Progra~1/SWI-Prolog/bin/plcon';  # For Windows XP/2000/etc.
# my $prolog = 'c:\Progra~1\SWI-Prolog\bin\plcon';  # For Windows 95/98/Me
# my $prolog = 'pl';                                # For *nix


#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $student_src   = $ARGV[0];	# source file name 
my $student_tests = $ARGV[1];	# test case file
                                # (includes expected output)


#=============================================================================
# In addition, some local definitions within this script
#=============================================================================
my $version         = "1.6";
my @labels          = ();	# User-provided test case names
my @test_cases	    = ();	# test case input
my @expected_output = ();	# corresponding expected output
my @case_nos        = ();       # test case number for each output line
my $temp_input      = "$PID.in";  # Name for temp test input file
my $temp_output     = "$PID.out"; # Name for temp test output file
my $delete_temps    = 1;        # Change to 0 to preserve temp files


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


#=============================================================================
# Phase I: Parse and split the test case input file
#=============================================================================

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
	    print "$student_tests: ", $INPUT_LINE_NUMBER - 1,
	          ": improperly formatted test case.\n";
        }
	my $label = $_;
	chomp $label;
	$label =~ s,^//==[-=\s]*,,o;
	$label =~ s,[-=\s]*$,,o;
	# if ( $label eq "" ) { $label = "(no label)"; }
	push( @labels, $label );
	push( @test_cases, "" );
	push( @expected_output, "" );
	$case++;
	push( @case_nos, $case );
	$scanning_input = 1;
    }
    elsif ( m,^//--,o )
    {
	if ( ! $scanning_input )
	{
	    print "$student_tests: ", $INPUT_LINE_NUMBER,
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
	        print "$student_tests: ", $INPUT_LINE_NUMBER,
	              ": improperly formatted test case.\n";
	    }
	    else
	    {
		s/'/\\'/go;
		$test_cases[$#test_cases] .= $_;
	    }
	}
	else
	{
	    if ( $#labels < 0 )
	    {
		print "$student_tests: ", $INPUT_LINE_NUMBER,
	              ": improperly formatted test case.\n";
	    }
	    s/\b_\b/Underscore/go;
	    s/'/\\'/go;
	    $expected_output[$#expected_output] .= $_;
	}
    }
}

if ( $#case_nos < 0 || $case_nos[$#case_nos] != $case )
{
    push( @case_nos, $case );
}

## For debugging purposes:
# print "labels => '", join( "', '", @labels ), "'\n";
# print "inputs => '", join( "', '", @test_cases ), "'\n";
# print "outputs => '", join( "', '", @expected_output ), "'\n";
# print "case_nos => '", join( "', '", @case_nos ), "'\n";


#=============================================================================
# Phase II: Execute the program
#=============================================================================

print "\ntddplg.pl v$version   (c) 2003 Virginia Tech. All rights reserved.\n";
print "Testing $student_src using $student_tests\n";
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
		   [ '$student_src:', Line, ': ' | Stripped_Rest ] ) :-
    integer( Line ),
    Line >= 0, !,
    strip_stream_refs( Rest, Stripped_Rest ).
strip_stream_refs( ['Stream ~w:~d: '-_ | Rest ],
		   [ '$student_src: ' | Stripped_Rest ] ) :-
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


expected_output_takeout( X, [ Z | R ], R ) :-
    ( is_list( X ), is_list( Z ) -> intersection( X, Z, X ); Z = X ), !.
expected_output_takeout( X, [ Z | R ], [ Z | S ] ) :-
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
   tell( '$temp_output' ).

/* Load given input file, bail if any problems arise */
% ?- load_files( ['$student_src'], [silent(true)] ).
?- ['$student_src'].
?- style_check( [ -singleton, -atom ] ).
   
/* Run test cases one at a time using testcase/2 */
EOF
# to counteract the ' in the output above, for emacs
for ( my $c = 0; $c <= $#test_cases; $c++ )
{
    chomp( $test_cases[$c] );
    print INFILE "?- testcase( ";
    if ( $labels[$c] ne "" )
    {
	$labels[$c] =~ s/'/\\'/go;
	print INFILE "'", $labels[$c], "', ";
    }
    print INFILE $c+1, ",\n'", $test_cases[$c], "',\n";
    print INFILE "'[\n", $expected_output[$c], "]').\n\n";
}
print INFILE<<EOF;

/* Exit from Prolog when done */
?- told, halt.
EOF
close( INFILE );
system( "$prolog -f $temp_input -t halt." );
if ( $delete_temps )
{
    unlink( "$temp_input" );
}


#=============================================================================
# Phase III: Compare the output to test case expectations
#=============================================================================

my $failures    = 0;    # count of failures
my $errs        = 0;    # Number of runtime errors
my $found_dot   = 0;

print "\n";
open( STUDENT, "$temp_output" ) ||
    die "Cannot open temporary output file '$temp_output': $!";

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
    if ( /^\./o )
    {
	$found_dot = 1;
    }
    s/(\.{78})/$1\n/go;
    print;
}

close( STUDENT );

my $num_cases = $#labels + 1;
if ( $errs > $num_cases )
{
    $errs = $num_cases;
}
if ( !$found_dot )
{
    $failures = $num_cases - $errs;
}
print "\n\nTests Run: $num_cases, Errors: $errs, Failures: $failures (",
    sprintf( "%.1f", ($num_cases - $failures - $errs)/$num_cases*100 ),
    "%)\n";
if ( $failures + $errs > 0 || ! $found_dot )
{
    print "Output has been saved in $temp_output.\n";
}
elsif ( $delete_temps )
{
    unlink( $temp_output );
}


#=============================================================================
# Exit Script
#=============================================================================
exit(0);
