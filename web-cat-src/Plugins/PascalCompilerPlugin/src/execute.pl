#!c:\perl\bin\perl.exe -w
#=============================================================================
#   @(#)$Id: execute.pl,v 1.2 2011/10/25 05:13:59 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT Curator: compile script for Free Pascal submissions
#
#   usage:
#       compile.pl <properties-file>
#=============================================================================

use strict;
use Config::Properties::Simple;
use File::stat;
use Proc::Background;
use Web_CAT::FeedbackGenerator;
use Web_CAT::Utilities;

#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $propfile    = $ARGV[0];	# property file name
my $cfg         = Config::Properties::Simple->new( file => $propfile );

# 		          student's pid
my $pid 	    = $cfg->getProperty( 'userName' );
#		          working dir for compilation
my $working_dir	= $cfg->getProperty( 'workingDir' );
#		          directory where script is located
my $script_home	= $cfg->getProperty( 'scriptHome' );
#		          directory where logs should be generated
my $log_dir	    = $cfg->getProperty( 'resultDir' );
#		          Timeout for compile job
my $timeout	    = $cfg->getProperty( 'timeout', 45 );


#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
my $debug              = $cfg->getProperty( 'debug',       0 );

my $scriptLogRelative   = "compile-script-log.txt";
my $scriptLog           = "$log_dir/$scriptLogRelative";
my $compilerLogRelative = "compiler-log.txt";
my $compilerLog         = "$log_dir\\$compilerLogRelative";
my $execFile            = "Executable.exe";
my $can_proceed         = 1;
my $pascalFile          = "";

# Pascal compiler to use
#-----------------------------
my @fpBinDirs = ( "i386-win32", "win32" );
my @fpLocations = ( "G:\\freepascal", "C:\\freepascal" );
my $pascalHome = "";
PASCAL_SEARCH: foreach my $fpBinDir ( @fpBinDirs )
{
    foreach my $fpLocation ( @fpLocations )
    {
	$pascalHome = $fpLocation . "\\bin\\" . $fpBinDir;
	# print "trying $pascalHome ...\n";
	if ( -d $pascalHome )
	{
	    last PASCAL_SEARCH;
	}
	else
	{
	    $pascalHome = "";
	}
    }
}
if ( $pascalHome eq "" )
{
    die "Cannot identify correct PASCAL_HOME";
}
my $compiler = $pascalHome . "\\ppc386.exe";


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
timeout      = $timeout
scriptLog    = $scriptLog
compilerLog  = $compilerLog
execFile     = $execFile
compiler     = $compiler
EOF
}

# Change to specified working directory and set up log directory
chdir( $working_dir );
if ( -f $execFile ) { unlink( $execFile ); }


#=============================================================================
# Find the pascal file to compile
# Extensions supported *.pp, *.pas
#=============================================================================
my @sources = (<*.pp *.pas>);
if ( $#sources < 0 || ! -f $sources[0] )
{
    open( SCRIPT_LOG, ">$scriptLog" ) ||
	die "cannot open $scriptLog: $!";
    print SCRIPT_LOG "Cannot identify a Pascal source file.\n",
        "Did you use a .pas or .pp extension?\n";
    close( SCRIPT_LOG );
    $can_proceed = 0;
}
else
{
    $pascalFile = $sources[0];
    if ( $#sources > 0 )
    {
	open( SCRIPT_LOG, ">$scriptLog" ) ||
	    die "cannot open $scriptLog: $!";
	print SCRIPT_LOG
	    "Multiple source files present.  Using $pascalFile.\n",
	    "Ignoring other .pas/.pp files.\n";
	close( SCRIPT_LOG );
    }
}

if ( $debug )
{
    print<<EOF;

--- after identifying source file ---
can_proceed  = $can_proceed
pascalFile  = $pascalFile
EOF
}


#=============================================================================
# Compilation Phase
#=============================================================================
# Create a separate thread for compilation, and run it with a timeout

if ( $can_proceed )
{
    my $job = Win32::Job->new;
    my $cmdline = $Web_CAT::Utilities::SHELL
	    . "$compiler $pascalFile -vi- -o$execFile 2>&1 > $compilerLog";
    print $cmdline, "\n" if ( $debug );
    my ( $exitcode, $timeout_status ) = Proc::Background::timeout_system(
        $timeout, $cmdline );
    if ( $timeout_status )
    {
        $can_proceed = 0;
    }
#    system( "$compiler $pascalFile -o$execFile -vi- -Fe$compilerLog" );
    if ( ! -f $execFile ) { $can_proceed = 0; }
    if ( $debug )
    {
        print "\n--- after compiling ---\n can_proceed = $can_proceed\n";
    }
}

#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================

my $reportCount = $cfg->getProperty( 'numReports', 0 );
if ( -f $scriptLog )
{
    $reportCount++;
    $cfg->setProperty( "report${reportCount}.file",     $scriptLogRelative );
    $cfg->setProperty( "report${reportCount}.mimeType", "text/plain" );
}
if ( -f $compilerLog && open( COMPILER_LOG, $compilerLog ) )
{
    my @compilerLines = <COMPILER_LOG>;
    close( COMPILER_LOG );
    if ( $#compilerLines > 1 )
    {
	my $feedbackGenerator = new Web_CAT::FeedbackGenerator( $compilerLog );
	$feedbackGenerator->startFeedbackSection( "Compiler Messages", 1 );
	$feedbackGenerator->print("<pre>\n");
	foreach my $line ( @compilerLines )
	{
	    $feedbackGenerator->print( $line );
	}
    $feedbackGenerator->print("</pre>\n");
	$feedbackGenerator->endFeedbackSection;
	$feedbackGenerator->close;
	$reportCount++;
        $cfg->setProperty( "report${reportCount}.file",
			   $compilerLogRelative );
        $cfg->setProperty( "report${reportCount}.mimeType", "text/html" );
    }
}
if ( $can_proceed )
{
    $cfg->setProperty( "executable", $execFile );
}
else
{
    $cfg->setProperty( "score.correctness", 0 );
    $cfg->setProperty( "canProceed", 0 );
}
$cfg->setProperty( "numReports", $reportCount );
$cfg->setProperty( "studentExeFile", $execFile );
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
#-----------------------------------------------------------------------------
