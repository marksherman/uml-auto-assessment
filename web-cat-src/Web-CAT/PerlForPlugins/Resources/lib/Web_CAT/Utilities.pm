#========================================================================
package Web_CAT::Utilities;

=head1 NAME

Web_CAT::Utilities - utility functions for Web-CAT grading plug-ins

=head1 SYNOPSIS

  use Web_CAT::Utilities qw( confirmExists copyHere addReportFile );

  my $fileName = confirmExists( $baseDir, $subpath );
  copyHere( $fileName, $baseDir );
  addReportFile( $subpath, "text/plain" );

=head1 DESCRIPTION

This module provides a number of top-level functions that may be
useful in writing Web-CAT grading plug-ins.

=cut

#========================================================================
use warnings;
use strict;
use File::stat;
use File::Copy;
use HTML::Entities;
use Text::Tabs;
use Carp qw( carp confess );
use vars qw( @ISA @EXPORT_OK $PATH_SEPARATOR $FILE_SEPARATOR $SHELL );
use Exporter qw( import );

@ISA = qw( Exporter );
@EXPORT_OK = qw( initFromConfig
                 confirmExists filePattern copyHere htmlEscape addReportFile
                 addReportFileWithStyle scanTo scanThrough printableSize
                 linesFromFile ltrim rtrim trim
                 $PATH_SEPARATOR
                 $FILE_SEPARATOR
                 $SHELL
                 );

$PATH_SEPARATOR = ':';
$FILE_SEPARATOR = '/';
$SHELL = '';

my %shells = (
    'MSWin32' => ['cmd.exe /c ', '\\', ';'],
    'dos'     => ['cmd.exe /c ', '\\', ';'],
    'NetWare' => ['cmd.exe /c ', '\\', ';']
);


#========================================================================
#                      -----  INITIALIZATION -----
#========================================================================
INIT
{
    if ( defined $shells{$^O})
    {
        ($SHELL, $FILE_SEPARATOR, $PATH_SEPARATOR) = @{$shells{$^O}};
    }
}


#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub initFromConfig
{

=head2 initFromConfig($cfg)

Pulls any overriding definitions of exported vars from supplied
config file.

=cut

    my $cfg = shift || confess "initFromConfig: cfg object required";

    $PATH_SEPARATOR = $cfg->getProperty(
        'PerlForPlugins.path.separator', $PATH_SEPARATOR );
    $FILE_SEPARATOR = $cfg->getProperty(
        'PerlForPlugins.file.separator', $FILE_SEPARATOR );
}


#========================================================================
sub confirmExists
{

=head2 confirmExists($baseDir, $subpath)

Checks a file or path name to ensure it exists, and returns the
resulting full path name.  The first parameter is a directory to
look in, and the (optional) second parameter is a relative sub-path
to look for.  If the given destination exists (as a file or directory),
then "$baseDir/$subpath" is returned.  Optionally, when invoked with
just a single argument, this function assumes that the lone argument
is the full path name to check for.  In either case, if the given
destination does not exist, the function dies via confess.

=cut

    my $baseDir = shift || carp "confirmExists: baseDir required";
    my $subpath = shift;
    my $target = $baseDir;
    if ( defined $subpath )
    {
        $target .= "/$subpath";
    }
    confess "required user data file/dir $target does not exist"
        unless ( -e $target );
    return $target;
}


#========================================================================
sub filePattern
{

=head2 filePattern($path)

Converts a file path to a corresponding regular expression pattern.
The pattern matches both all-forward-slash and all-backward-slash
versions of the path.  We cannot simply rely on the Perl OS build
information ($^O) to determine the correct pattern to use, since
the choice of slashes to use in various parts of the ANT log may
be made by various external programs (e.g., even under a cygwin
build of Perl, we must support both forward and backward slashes
in the ANT log, since other tools may be pure Win32 executables).

=cut

    my $path = shift || carp "filePattern: path required";
    if ( $path !~ m,/,o )
    {
        $path =~ s,\\,/,go;
    }
    $path =~ s,//+,/,go;
    my $dosPath = $path;
    $dosPath =~ s,/,\\,go;
    return qr/\Q$path\E|\Q$dosPath\E/;
}


#========================================================================
sub copyHere
{

=head2 copyHere($pathName, $prefixDirToStrip, $ignoreList)

Recursively copy a directory's contents (or a single file) to the
current working directory.  Recursively traverses the directory
structure rooted at $pathName, and copies each file to a local relative
path name obtained by stripping the leading $prefixDirToStrip portion
of the directory name.  The third (optional) parameter, if given,
should be a reference to an array.  If provided, each file copied
will be added to the corresponding array.

=cut

   # ' <- for emacs Perl mode

    my $file = shift || carp "copyHere: pathName required";
    my $base = shift || carp "copyHere: prefixDirToStrip required";
    my $ignoreList = shift;
    $base =~ s,/$,,o;
    my $newfile = $file;
    $newfile =~ s,^\Q$base/\E,,;
    if ( -d $file )
    {
        if ( $file ne $base )
        {
            # print "mkdir( $newfile );\n" if $debug;
            mkdir( $newfile );
        }
        foreach my $f ( glob("$file/*") )
        {
            copyHere( $f, $base );
        }
    }
    else
    {
        # print "copy( $file, $newfile );\n" if $debug;
        copy( $file, $newfile );
        if ( defined $ignoreList )
        {
            push( @{ $ignoreList }, $newfile );
        }
    }
}


#========================================================================
sub htmlEscape
{

=head2 htmlEscape($string)

Uses Text::Tabs::expand() to expand tabs, and then uses
HTML::Entities::encode_entities() to escape any HTML control
characters, returning the final result.

=cut

    my $str = shift;
    if ( defined $str )
    {
        $str = HTML::Entities::encode_entities_numeric(
            expand($str), '^\n\r\t !\#\$%\(-;=?-~');
        $str =~ s/&#([01]?[0-9A-Fa-f]);/&#171;&amp;#$1&#187;/g;
    }
    return $str;
}


#========================================================================
sub addReportFile
{
    my $cfg          = shift || confess "addReportFile: cfg object required";
    my $file         = shift || confess "addReportFile: file name required";
    my $mimeType     = shift || "text/html";
    my $to           = shift;
    my $inline       = shift;
    my $label        = shift;

    addReportFileWithStyle($cfg, $file, $mimeType, 0, $to, $inline, $label);
}


#========================================================================
sub addReportFileWithStyle
{

=head2 addReportFile($cfg, $file, $mimeType, $styleVersion, $to, $inline, $label)

Adds a specified report file to the specified properties file, and
update the "numReports" property appropriately.
The parameters are:

=over

=item $cfg (required)

A reference to a Config::Properties object containing the property
settings to change.

=item $file (required)

The relative pathname of the report file to add.

=item $mimeType (optional)

The mime type to record for the given file.  If omitted, the default is
"text/html".

=item $styleVersion (optional)

Use the specified style version for collapsible inline report sections.
0 = legacy styles, 1 = Dojo title panes.

=item $to (optional)

Set to "admin" to target a report for administrators.  The default is
"student".

=item $inline (optional)

If true, the report should be shown inline.  If false, the report should
instead be presented for download instead of shown inline.

=item $label (optional)

The label/description to use for downloaded (rather than inline) files.

=back

=cut

    my $cfg          = shift || confess "addReportFile: cfg object required";
    my $file         = shift || confess "addReportFile: file name required";
    my $mimeType     = shift || "text/html";
    my $styleVersion = shift || 0;
    my $to           = shift;
    my $inline       = shift;
    my $label        = shift;

    my $numReports = $cfg->getProperty( 'numReports', 0 );
    $numReports++;

    $cfg->setProperty( "report${numReports}.file",     $file );
    $cfg->setProperty( "report${numReports}.mimeType", $mimeType );
    if ( $styleVersion )
    {
        $cfg->setProperty( "report${numReports}.styleVersion", $styleVersion );
    }
    if ( defined $to )
    {
        $cfg->setProperty( "report${numReports}.to", $to );
    }
    if ( defined $inline )
    {
        $cfg->setProperty( "report${numReports}.inline", $inline );
    }
    if ( defined $label )
    {
        $cfg->setProperty( "report${numReports}.label", $label );
    }
    $cfg->setProperty( 'numReports', $numReports );
}


#========================================================================
sub scanTo
{

=head2 scanTo($pattern, $fileHandle)

Using the global $_ variable as a line buffer, reads from the
specified file handle until a line matching the given regular
expression is found.  No read will be performed if $_ already
matches the pattern.  The file handle is optional, and defaults
to *main::ANTLOG{IO}.

=cut

    my $pattern = shift || '^\s*$';
    my $fh      = shift || *main::ANTLOG{IO};

    if ( defined( $_ ) && !( m/$pattern/ ) )
    {
        while ( <$fh> )
        {
            last if m/$pattern/;
        }
    }
}


#========================================================================
sub scanThrough
{

=head2 scanThrough($pattern, $fileHandle)

Using the global $_ variable as a line buffer, reads from the
specified file handle until a non-blank line that does not match
the given regular expression is found.  No read will be performed
if $_ is already non-blank and fails to match the pattern.  The
file handle is optional, and defaults to *main::ANTLOG{IO}.

=cut

    my $pattern = shift || confess "scanThrough: pattern required";
    my $fh      = shift || *main::ANTLOG{IO};

    while ( defined( $_ ) && ( m/^\s*$/o || m/$pattern/ ) )
    {
        $_ = <$fh>;
    }
}


#========================================================================
sub printableSize
{

=head2 printableSize($size)

Converts an integer size (in bytes) into a human readable size
(e.g., 50Kb, 1.2Mb, etc.).

=cut

    my $size = shift || confess "printableSize: size required";
    my $result = $size;

    if ( $size < 1024 )
    {
        $result .= " bytes";
    }
    elsif ( $size < 1048576 )
    {
        $result = int( $size / 1024 * 10 ) / 10;
        $result .= "Kb";
    }
    else
    {
        $result = int( $size / 1048576 * 10 ) / 10;
        $result .= "Mb";
    }
    return $result;
}


#========================================================================
sub linesFromFile
{

=head2 linesFromFile($fileName, $sizeLimit, $lines, $isHTML)

Pull lines from a specified file into memory and return them as an
array.  If the file does not exist, returns undef.  If the file exists,
but is larger than $sizeLimit, only the first $lines number of lines
will be returned, rather than the entire file contents.  An HTML-ized
error message indicating the total file size will be added at the end
of the array of lines if the file is too large.  If $sizeLimit is
omitted, a default value of 50K is used.  If $lines is omitted, a
default value of 500 is used.  For large files with long lines, fewer
than $lines lines may be returned, since the function will stop reading
from the input file once it processes more than $sizeLimit bytes.
However, it will not truncate the last line--the full line will be
included in the result.

=cut

    my $fileName  = shift || confess "linesFromFile: fileName required";
    my $sizeLimit = shift || 50000;
    my $lines     = shift || 500;
    my $isHTML    = shift || 0;
    my @result = ();

    # print "linesFromFile: checking file $fileName\n";
    if ( ! -f $fileName )
    {
        return @result;
    }

    my $size = stat( $fileName )->size;
    my $limitLines = stat( $fileName )->size > $sizeLimit;
    # print "linesFromFile: size = $size, limit = $limitLines\n";
    open( LINESFROMFILE, $fileName ) || return undef;
    if ( $size <= $sizeLimit )
    {
        # print "linesFromFile: short case\n";
        @result = <LINESFROMFILE>;
    }
    else
    {
        # print "linesFromFile: long case\n";
        my $line;
        my $lineCount = 0;
        my $resultSize = 0;
        while ( $lineCount < $lines
                && $resultSize < $sizeLimit
                && ( $line = <LINESFROMFILE> ) )
        {
            push( @result, $line );
            $resultSize += length( $line );
            $lineCount++;
        }
        push( @result, "\n" );
        push( @result, ". . .\n" );
        push( @result, "<b class=\"warn\">(Output of "
              . printableSize( $size )
              . " too long, only the first "
              . $lineCount
              . " lines are shown)</b>\n" );
    }
    close( LINESFROMFILE );
    if ( !$isHTML )
    {
        @result = map( htmlEscape( $_ ), @result );
    }
    # print "$#result lines to return\n";
    return @result;
}


#========================================================================
sub ltrim
{

=head2 ltrim($string)

Returns the string with all white space removed from the left
end (front).

=cut

    my $string = shift || confess "ltrim: string required";
    $string =~ s/^\s+//o;
    return $string;
}


#========================================================================
sub rtrim
{

=head2 rtrim($string)

Returns the string with all white space removed from the right
end (back).

=cut

    my $string = shift || confess "rtrim: string required";
    $string =~ s/\s+$//o;
    return $string;
}


#========================================================================
sub trim
{

=head2 trim($string)

Returns the string with all white space removed from both ends.

=cut

    my $string = shift || confess "trim: string required";
    $string =~ s/^\s+//o;  # ltrim
    $string =~ s/\s+$//o;  # rtrim
    return $string;
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
__END__

=head1 AUTHOR

Stephen Edwards

$Id: Utilities.pm,v 1.8 2011/03/01 17:38:14 aallowat Exp $
