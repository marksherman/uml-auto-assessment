#!c:\perl\bin\perl.exe -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.3 2011/10/25 05:25:16 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Curator: grading script for all submissions
#
#   usage:
#       grading.pl <pid> <working-dir> <script-home-dir> <logDir> \
#					<resultXMLfilename> <timeout>
#=============================================================================
use strict;
use File::stat;
use Config::Properties::Simple;
use Web_CAT::Beautifier;

#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $propfile    = $ARGV[0];	# property file name
my $cfg         = Config::Properties::Simple->new( file => $propfile );

my $pid         = $cfg->getProperty( 'userName' );
my $working_dir	= $cfg->getProperty( 'workingDir' );
my $script_home	= $cfg->getProperty( 'scriptHome' );
my $log_dir	= $cfg->getProperty( 'resultDir' );


#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
my $debug               = $cfg->getProperty( 'debug',       0 );

my $report_relative     = "grading.html";
my $report              = "$log_dir/$report_relative";


#=============================================================================
# Script Startup
#=============================================================================
if ( $debug )
{
    print<<EOF;
pid          = $pid
working_dir  = $working_dir
script_home  = $script_home
log_dir      = $log_dir
report       = $report
EOF
}

if ( ! $cfg->getProperty( 'canProceed', 1 ) )
{
    exit( 0 );
}

# Change to specified working directory and set up log directory
chdir( $working_dir );


#=============================================================================
# HTML-ize source code
#=============================================================================
my $beautify = $cfg->getProperty('SummarizeTddGradePlugin.beautify', 1);
$beautify = ($beautify =~ m/^(true|on|yes|y|1)$/i);

if ($beautify)
{
    my $beautifier = new Web_CAT::Beautifier;
    $beautifier->beautifyCwd( $cfg, [ '__coverage__.txt', 'coverage.txt' ] );
}


#=============================================================================
# Print out the HTML report fragment
#=============================================================================
sub meter
{
    my $fh = shift;
    my $fraction = shift;
    my $useRed = shift;
    my $covered = int( 200.0 * $fraction + 0.5 );
    my $uncovered = 200 - $covered;

    print $fh '<table class="percentbar"><tr><td ';
    if ( $covered < 1 )
    {
	# Completely uncovered
        print $fh 'class="minus"';
	if ( defined( $useRed ) && $useRed )
	{
	    print $fh ' style="background-color: black"';
	}
	print $fh '><img src="/images/blank.gif" width="200" height="12" alt="nothing covered">';
    }
    elsif ( $uncovered > 0 )
    {
	# Partially covered
        print $fh 'class="';
	print $fh ( ( defined( $useRed ) && $useRed ) ? "minus" : "plus" );
	print $fh '"><img src="/images/blank.gif" width="';
	print $fh $covered;
	print $fh '" height="12" alt="';
	print $fh int( 100.0 * $fraction + 0.5 );
	print $fh ' covered"></td><td class="minus"';
	if ( defined( $useRed ) && $useRed )
	{
	    print $fh ' style="background-color: black"';
	}
	print $fh '><img src="/images/blank.gif" width="';
	print $fh $uncovered;
	print $fh '" height="12" alt="';
	print $fh ( 100 - int( 100.0 * $fraction + 0.5 ) );
	print $fh ' uncovered">';
    }
    else
    {
	# Completely covered
	print $fh 'class="';
	print $fh ( ( defined( $useRed ) && $useRed ) ? "minus" : "plus" );
	print $fh '"><img src="/images/blank.gif" width="200" height="12" alt="fully covered">';
    }
    print $fh '</td></tr></table>';
}


#=============================================================================
# Print out the HTML report fragment
#=============================================================================
my $final_score = 0;
my ( $student_percent,
     $student_passed,
     $student_total ) = split( /\s+/,
			       $cfg->getProperty( "studentEval", "0 0 1" ) );
if ( ! $cfg->getProperty( "timeoutOccurred", 0 ) )
{
my $student_percent_int = int( $student_percent * 100 + 0.5 );
my ( $instr_percent,
     $instr_passed,
     $instr_total ) = split( /\s+/,
			     $cfg->getProperty( "instructorEval", "0 0 1" ) );
my $instr_percent_int = int( $instr_percent * 100 + 0.5 );
my ( $coverage_percent,
     $coverage_passed,
     $coverage_total ) = split( /\s+/,
				$cfg->getProperty( "coverage", "0 0 1" ) );
my $coverage_int = int( $coverage_percent * 100 + 0.5 );
my $max_score = $cfg->getProperty( 'max.score.correctness', 0 );
$final_score = ( $student_passed == $student_total ) ?
     int( $student_percent * $instr_percent
	  * $coverage_percent * $max_score + 0.5 )
     : 0;
my $score_equation = sprintf( "(%d%% x %d%% x %d%%) x 50 = %d",
			      $student_percent_int,
			      $instr_percent_int,
			      $coverage_int,
			      $final_score );

open( HTML, ">$report" ) ||
    die "Cannot open '$report': $!";

print HTML<<EOF;
<div class="module"><div dojoType="webcat.TitlePane" title="Summary">
<table><tbody>
<tr><th colspan="3">Correctness Based on Your Tests</th></tr>
<tr><td valign="top" align="right"><b>Your Program</b></td>
<td valign="top">
EOF
    meter( *HTML, $student_percent );
print HTML<<EOF;
</td>
<td valign="top"><b>$student_passed of $student_total</b> tests passed
($student_percent_int%)</td>
</tr>
EOF
if ( $student_passed == $student_total
     && !$cfg->getProperty("instrTimeoutOccurred", 0 ) )
{
print HTML<<EOF;
<tr><td colspan="3">&nbsp;</td></tr>
<tr><th colspan="3">Thoroughness of Your Testing</th></tr>
<tr><td valign="top" align="right"><b>Your Test Cases</b></td>
<td valign=top>
EOF
    meter( *HTML, $coverage_percent, ( $instr_percent < 1 ) );
print HTML<<EOF;
</td>
<td valign="top"><b>$coverage_int%</b> coverage,<br><b>$instr_passed of $instr_total</b> tests valid</td>
</tr>
<tr><td colspan="3" align="center">Score = $score_equation</td></tr>
EOF
}
print HTML "</table></div></div>\n";
close(HTML);
}


#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================
my $reportCount = $cfg->getProperty( 'numReports', 0 );

$reportCount++;
$cfg->setProperty( "report${reportCount}.file",     "$report_relative" );
$cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
$cfg->setProperty( "report${reportCount}.styleVersion", 1);

# This was produced by the execute script
if ( -f "$log_dir/student-tdd-report.txt" )
{
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file", "student-tdd-report.txt" );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
    $cfg->setProperty( "report${reportCount}.styleVersion", 1);
}

if ( $student_passed == $student_total && -f "$log_dir/instr-tdd-report.txt" )
{
# This was produced by the execute script
$reportCount++;
$cfg->setProperty( "report${reportCount}.file",   "instr-tdd-report.txt" );
$cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
$cfg->setProperty( "report${reportCount}.styleVersion", 1);
}

my $hintsReport = $cfg->getProperty( 'hintsReport' );
if ( defined( $hintsReport ) && -f "$log_dir/$hintsReport" )
{
# This was produced by the execute script
$reportCount++;
$cfg->setProperty( "report${reportCount}.file",   "$hintsReport" );
$cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
$cfg->setProperty( "report${reportCount}.styleVersion", 1);
}

$cfg->setProperty( "score.correctness", $final_score );
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


#=============================================================================
exit( 0 );
#=============================================================================
