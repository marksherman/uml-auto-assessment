#!c:\perl\bin\perl.exe
#=============================================================================
#   @(#)$Id: patchClasspathTxt.pl,v 1.1 2006/02/19 19:24:25 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT: fix errors in woproject-generated CLASSPATH.TXT entries
#
#   usage:
#       patchClasspathTxt.pl path-to-CLASSPATH.TXT
#=============================================================================


use strict;

if ( $#ARGV < 0 || ! -f $ARGV[0] )
{
    print STDERR "usage: patchClasspathTxt.pl <filename>\n";
    exit(1);
}

my $classpathFile = $ARGV[0];
my $oldFile = "$classpathFile-old";
rename( $classpathFile, $oldFile );
open( OLDFILE, $oldFile ) ||
    die "Cannot open '$oldFile': $!";
open( NEWFILE, ">$classpathFile" ) ||
    die "Cannot open '$classpathFile': $!";

my %frameworks = ();
my @frameworkOrder = ();

sub printFrameworks
{
    # Force the ordering for these frameworks
    my @protos     = ();
    my @jars       = ();
    my @extensions = ();
    foreach my $f ( @frameworkOrder )
    {
	if ( $f =~ m/Prototypes.framework/o )
	{
	    push( @protos, $f );
	}
	elsif ( $f =~ m/ERJars.framework/o )
	{
	    push( @jars, $f );
	}
	elsif ( $f =~ m/ERExtensions.framework/o )
	{
	    push( @extensions, $f );
	}
    }
    foreach my $f ( @protos, @jars, @extensions )
    {
	print NEWFILE $f,"\n";
	delete $frameworks{$f};
    }
    foreach my $f ( @frameworkOrder )
    {
	if ( defined $frameworks{$f} )
	{
	    print NEWFILE $f,"\n";
	}
    }
}

while ( <OLDFILE> )
{
    if    ( m/^\s*\#/o )
    {
	print NEWFILE $_;
    }
    else
    {
	chomp;
	$frameworks{$_} = 1;
	push( @frameworkOrder, $_ );
    }
}
printFrameworks();

close( NEWFILE );
close( OLDFILE );
#unlink( $oldFile );
