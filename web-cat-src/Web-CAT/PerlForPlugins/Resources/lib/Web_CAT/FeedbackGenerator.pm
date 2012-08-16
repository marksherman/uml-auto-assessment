#========================================================================
package Web_CAT::FeedbackGenerator;
#========================================================================
use warnings;
use strict;
use Carp;
use vars qw( $URL_BASE );
use Web_CAT::Utilities qw( htmlEscape );

$URL_BASE    = "http://web-cat.cs.vt.edu/wcstatic/Core.framework"
    . "/WebServerResources/icons/";

#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $class = ref( $proto )  ||  $proto;
    my $self = {
        'sectionOpen' => 0,
        'hasContent'  => 0,
        'closeTags'   => []
    };
    my $baseDir  = shift;
    my $fileName = shift;
    bless( $self, $class );
    $self->setFile( $baseDir, $fileName );
    return $self;
}


#========================================================================
sub setFile
{
    my $self = shift;
    my $baseDir  = shift;
    my $fileName = shift;
    $self->{'baseDir'}  = $fileName;
    $self->{'fileName'} = $fileName;
    if ( defined $baseDir )
    {
        if ( !defined $fileName )
        {
            $fileName = $baseDir;
            $self->{'fileName'} = $fileName;
            $self->{'baseDir'} = undef;
        }
        else
        {
            $baseDir =~ s,/$,,o;
            $fileName = "$baseDir/$fileName";
        }
    }
    $self->close;
    if ( defined $fileName )
    {
        $self->{'fullFileName'} = $fileName;
        local *FH;
        open( FH, ">$fileName" )
            or croak "Cannot open '$fileName' for writing: $!";
        $self->{'fileHandle'} = *FH;
    }
}


#========================================================================
sub close
{
    my $self = shift;
    while ( $self->sectionIsOpen )
    {
        $self->endFeedbackSection;
    }
    if ( !$self->isOpen ) { return; }
    close( $self->{'fileHandle'} );
    delete $self->{'fileHandle'};
}


#========================================================================
sub startFeedbackSection
{
    my $self      = shift;
    my $title     = shift;
    my $id        = shift;
    my $collapsed = shift;
    my $level     = shift;
    my $openTags  = shift;
    my $closeTags = shift;
    croak "No file open!" if ( !defined( $self->{'fileHandle'} ) );

    # If $level is not defined, assume we're starting another section
    # at the same level.
    if ( !defined $level )
    {
        $level = $self->{'sectionOpen'} || 1;
    }
    # Close existing sections first, as necessary.
    for ( my $diff = $self->{'sectionOpen'} - $level + 1;
          $diff > 0; $diff-- )
    {
        $self->endFeedbackSection;
    }

    $self->{'sectionOpen'}++;
    push( @{$self->{'closeTags'}}, $closeTags );

    # Convert $level (Starts at one) to heading level number
    # (should start at H2)
    $level++;

    my $moduleClass = ($level > 2) ? '' : ' class="module"';

    # Output the section header
    if ( defined $id )
    {
        $self->{'sectionId'} = $id;
        if ( !defined( $collapsed ) )
        {
            $collapsed = 0;
        }
        if ( !defined( $level ) )
        {
            $level = 2;
        }
        my $escapedTitle = htmlEscape($title);
        my $openAttr = $collapsed ? "false" : "true";
        print { $self->{'fileHandle'} } <<EOF;
<div$moduleClass>
<div dojoType="webcat.TitlePane" title="$escapedTitle" open="$openAttr">
EOF
    }
    else
    {
        $self->{'sectionId'} = undef;
        if ( !defined( $level ) )
        {
            $level = 2;
        }
        my $escapedTitle = htmlEscape($title);
        print { $self->{'fileHandle'} } <<EOF;
<div$moduleClass>
<div dojoType="webcat.TitlePane" title="$escapedTitle" open="false">
EOF
    }
    if ( defined $openTags )
    {
        print { $self->{'fileHandle'} } "$openTags\n";
    }
    $self->{'hasContent'} = 1;
}


#========================================================================
sub endFeedbackSection
{
    my $self = shift;
    croak "No file open!" if ( !$self->isOpen );
    if ( !$self->sectionIsOpen )
    {
        carp "No section open!";
        return;
    }
    my $closeTags = pop( @{$self->{'closeTags'}} );
    if ( defined $closeTags )
    {
        print { $self->{'fileHandle'} } "$closeTags\n";
    }
    print { $self->{'fileHandle'} } "</div></div>\n";
    $self->{'sectionOpen'}--;
}


#========================================================================
sub print
{
    my $self = shift;
    croak "No file open!" if ( !defined( $self->{'fileHandle'} ) );
    print { $self->{'fileHandle'} }  @_;
    $self->{'hasContent'} = 1;
}


#========================================================================
sub baseDir
{
    my $self = shift;
    return $self->{'baseDir'};
}


#========================================================================
sub fileName
{
    my $self = shift;
    return $self->{'fileName'};
}


#========================================================================
sub fullFileName
{
    my $self = shift;
    return $self->{'fullFileName'};
}


#========================================================================
sub isOpen
{
    my $self = shift;
    return defined $self->{'fileHandle'};
}


#========================================================================
sub sectionIsOpen
{
    my $self = shift;
    return $self->{'sectionOpen'};
}


#========================================================================
sub hasContent
{
    my $self = shift;
    return $self->{'hasContent'};
}


#========================================================================
sub unlink
{
    my $self = shift;
    $self->close;
    unlink( $self->fullFileName );
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
