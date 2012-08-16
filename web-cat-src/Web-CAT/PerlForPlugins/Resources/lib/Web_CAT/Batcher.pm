#========================================================================
package Web_CAT::Batcher;

=head1 NAME

Web_CAT::Batcher - a class that manages the main loop and
  dispatch/response process of a batch plug-in

=head1 SYNOPSIS

  use Web_CAT::Batcher;

  my %state_map = (
     'start'  => \&handle_start,
     'item'   => \&handle_item,
     'finish' => \&handle_finish,
  );

  my $batcher = Web_CAT::Batcher->new( $properties_file, \%state_map );
  $batcher->run();

=head1 DESCRIPTION

This module provides a number of top-level functions that may be
useful in writing Web-CAT batch plug-ins.

=cut

#========================================================================

use warnings;
use strict;
use Carp;
use File::stat;
use Config::Properties::Simple;


#========================================================================
#                      -----  INITIALIZATION -----
#========================================================================
INIT
{
    # Enable autoflush. Important so that the plug-in and the server can
    # communicate back and forth on stdin/stdout channels.

    $| = 1;
}


#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $class = ref($proto) || $proto;
    my $self = { };

    my $properties_file = shift;
    my $state_map = shift;
    bless($self, $class);

    $self->{'propertiesFile'} = $properties_file;
    $self->{'properties'} = Config::Properties::Simple->new(
        file => $properties_file);

    $self->{'stateMap'} = $state_map;

    return $self;
}


#========================================================================
sub run
{
    my $self = shift;
    my $line;
    my $continue = 1;

    while ($continue && defined($line = <STDIN>))
    {
        $continue = $self->dispatch($line);
    }
}


#========================================================================
sub dispatch
{
    my $self = shift;
    my $state = shift;
    chomp $state;

    $self->reloadProperties();

    my $handler = $self->{'stateMap'}->{$state};

    if (defined $handler)
    {
        # Call the user's state handler.
        my $response = &$handler();

        # Provide reasonable default behavior if the user does not want
        # to manage job progress calculations himself. This assumes that
        # the batch is only iterated over once, and that any states
        # prior to or after the iteration take little enough time that
        # they can effectively be ignored.

        my $progress = $self->properties()->getProperty('batch.jobProgress', -1);
        if ($progress == -1)
        {
            $self->properties()->setProperty('batch.jobProgress',
                $self->iterationProgress());
        }

        $self->saveProperties();
        print "$response\n";

        if ($response eq 'die' || $response eq 'end')
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    else
    {
        print "die\n";
        return 0;
    }
}


#========================================================================
sub reloadProperties
{
    my $self = shift;
    my $reloaded = Config::Properties::Simple->new(
        file => $self->{'propertiesFile'});
    for my $key (keys %{$reloaded})
    {
        $self->{'properties'}->{$key} = $reloaded->{$key};
    }
}


#========================================================================
sub saveProperties
{
    my $self = shift;
    $self->{'properties'}->save();
}


#========================================================================
sub properties
{
    my $self = shift;
    return $self->{'properties'};
}


#========================================================================
sub iterationProgress
{
    my $self = shift;
    my $props = $self->properties();

    return $props->getProperty('batch.iterationProgress', 0);
}


#========================================================================
sub updateProgress
{
    my $self = shift;
    my $progress = shift;
    my $message = shift;

    my $props = $self->properties();

    $props->setProperty('batch.jobProgress', $progress);

    if (defined $message)
    {
        $props->setProperty('batch.jobProgressMessage', $message);
    }
}


#========================================================================
1;
#========================================================================
__END__

=head1 AUTHOR

Tony Allevato

$Id: Batcher.pm,v 1.2 2011/10/25 16:56:07 stedwar2 Exp $
