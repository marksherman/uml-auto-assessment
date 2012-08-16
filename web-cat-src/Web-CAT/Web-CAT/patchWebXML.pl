#!c:\perl\bin\perl.exe
#=============================================================================
#   @(#)$Id: patchWebXML.pl,v 1.1 2006/02/19 19:24:25 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT: fix errors in woproject-generated web.xml WOClasspath entries
#
#   usage:
#       patchWebXML.pl path-to-web.xml
#=============================================================================


use strict;

if ( $#ARGV < 0 || ! -f $ARGV[0] )
{
    print STDERR "usage: patchWebXML.pl <filename>\n";
    exit(1);
}

my $webXMLFile = $ARGV[0];
#  value="${dest.dir}/${project.name}.woa/Contents/web.xml"
$webXMLFile =~ m,(/|\\)([^/\\]+(/|\\)Contents)(/|\\)[^/\\]+$,;
my $appRoot = $2;

#print "webXMLFile = $webXMLFile\n";
#print "appRoot    = $appRoot\n";

my $oldFile = "$webXMLFile-old";
rename( $webXMLFile, $oldFile );
open( OLDFILE, $oldFile ) ||
    die "Cannot open '$oldFile': $!";
open( NEWFILE, ">$webXMLFile" ) ||
    die "Cannot open '$webXMLFile': $!";

my %frameworks = ();
my @frameworkOrder = ();
my $state = 0;

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
    # print NEWFILE "</param-value>\n";
}

while ( <OLDFILE> )
{
    if    ( $state == 0 &&
	    m,<param-name>WOClasspath</param-name>,o ) { $state = 1; }
    elsif    ( $state == 0 &&
	    m,<!--\s*$,o ) { $_ = ""; $state = 3; }
    elsif ( $state == 1 &&
	    m,<param-value>,o ) { $state = 2; }
    elsif ( $state == 2 &&
	    m,</param-value>,o ) { $state = 0; printFrameworks(); }
    elsif ( $state == 2 )
    {
	chomp;
	s,^\s*WEBINFROOTAPPROOT,WEBINFROOT/$appRoot,o;
	s/^\s*WEBINFROOT((WO|LOCAL|WOAINSTALL)ROOT)/$1/o;
	if ( s,</param-value>,,o ) { $state = 0; }
	s,\\,/,go;
	$frameworks{$_} = 1;
	push( @frameworkOrder, $_ );
	if ( $state == 0 ) { printFrameworks(); }
	next;
    }
    elsif    ( $state == 3 &&
	    m,^\s*-->\s*$,o ) { $_ = ""; $state = 0; }
    s,/Local\\Library/,/Local/Library/,o;
    s,/WEB-INF/tlds/(/WEB-INF/tlds/),$1,o;
    print NEWFILE $_;
}

close( NEWFILE );
close( OLDFILE );
# unlink( $oldFile );
