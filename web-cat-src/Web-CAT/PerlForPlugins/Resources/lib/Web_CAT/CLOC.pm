#========================================================================
package Web_CAT::CLOC;
#========================================================================
#
#  A wrapper for the cloc-x.xx.pl script found at
#  http://cloc.sourceforge.net.  This module runs the script, parses its
#  output, and makes its results accessible through the fileMetrics
#  method.
#
#  SETUP
#
#  To use this, download cloc-x.xx.pl from the above URL and place it in
#  PerlForPlugins.framework's Resources/lib/CLOC directory, and make
#  sure the $CLOC_SCRIPT_VERSION variable below matches the version of
#  the script.
#
#  USAGE
#
#    @files = ( 'file path', 'file path', ..., 'file path' );
#    $cloc = new Web_CAT::CLOC();
#    $cloc->execute(@files);
#
#    %metrics = $cloc->fileMetrics('one of your files');
#
#    print "Language =      " . $metrics->{language};
#    print "Blank lines =   " . $metrics->{blank};
#    print "Comment lines = " . $metrics->{comment};
#    print "Code lines =    " . $metrics->{code};
#
#========================================================================

use warnings;
use strict;
use Carp;
use IPC::System::Simple qw(capture);
use XML::Smart;
#use Data::Dump qw(dump);

my $MODULEPATH;
my $CLOC_SCRIPT_VERSION = "1.08";

BEGIN
{
	use Cwd ();
	use File::Basename;
	$MODULEPATH = Cwd::abs_path(
		dirname(Cwd::abs_path(__FILE__)) . '/../CLOC');
}

#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
# $object = new Web_CAT::CLOC()
#
sub new
{
	my $proto = shift;
	my $class = ref($proto) || $proto;
	my $self = {
		'files' => {},
		'extOverrides' => {}
	};
	
	bless($self, $class);
	
	return $self;
}

#========================================================================
# addExtensionOverride($extension, $language)
#
sub addExtensionOverride
{
	my $self = shift;
	my $ext = shift;
	my $lang = shift;
	
	$self->{'extOverrides'}{$ext} = $lang;
}

#========================================================================
# $hashRef = fileMetrics($filename)
#
sub fileMetrics
{
	my $self = shift;
	my $file = shift;

	return $self->{'files'}{$file};
}

#========================================================================
# execute(@files)
#
sub execute
{
	my $self = shift;
	my @filenames = @_;

	my @args = (
		"$MODULEPATH/cloc-$CLOC_SCRIPT_VERSION.pl",
		"--quiet",
		"--xml",
		"--by-file"
	);
	
	while ((my $ext,my $lang) = each(%{$self->{'extOverrides'}}))
	{
		push @args, "--force-lang=$lang,$ext"
	}

	push @args, @filenames;

	my $output = capture($^X, @args);
    $output =~ s/^\s+//;
    $output =~ s/\s+$//;

	my $xml = new XML::Smart($output);
	my @files = @{$xml->{results}{files}{file}};
	my $f;

	foreach $f (@files)
	{
		$self->{'files'}{$f->{name}} = {
			'language' => sprintf('%s', $f->{language}),
			'blank' => int($f->{blank}),
			'comment' => int($f->{comment}),
			'code' => int($f->{code})
		};
	}
}
