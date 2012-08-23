#========================================================================
package Web_CAT::PrefixedPropertySubset;
#========================================================================
use warnings;
use strict;
use Carp;
use File::stat;
use Config::Properties;


#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $class = ref($proto)  ||  $proto;
    my $self = {
        'prefix' => (shift || croak "prefix required"),
         'cfg'   => (shift || croak "cfg required")
    };
    bless($self, $class);
    return $self;
}


#========================================================================
sub getProperty
{
    my $self = shift;
    my $propName = shift || croak "property name required";
    $propName = $self->{'prefix'} . '.' . $propName;
    unshift(@_, $propName);
    return $self->{'cfg'}->getProperty(@_);
}


#========================================================================
sub setProperty
{
    my $self = shift;
    my $propName = shift || croak "property name required";
    $propName = $self->{'prefix'} . '.' . $propName;
    unshift(@_, $propName);
    $self->{'cfg'}->setProperty(@_);
}


#========================================================================
sub propertyNames
{
    my $self = shift;
    my @names = ();
    my $prefix = $self->{'prefix'} . '.';
    for my $key ($self->{'cfg'}->propertyNames)
    {
        my $tail = $key;
        if ($tail =~ s/^\Q$prefix\E//)
        {
            push(@names, $tail);
        }
    }
    return @names;
}


#========================================================================
sub exportToPrefix
{
    my $self = shift;
    my $newPrefix = shift || croak "new prefix required";
    $newPrefix .= '.';
    my $oldPrefix = $self->{'prefix'} . '.';
    my $cfg = $self->{'cfg'};
    for my $key ($cfg->propertyNames)
    {
        my $tail = $key;
        if ($tail =~ s/^\Q$oldPrefix\E//)
        {
            $cfg->setProperty($newPrefix . $tail, $cfg->getProperty($key));
        }
    }
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
