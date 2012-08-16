#========================================================================
package Web_CAT::DerefereeStatsReader;
#========================================================================
use warnings;
use strict;
use Carp;
use File::stat;

#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $class = ref( $proto )  ||  $proto;
    my $self = {
        'plist'                 => "",
        'hasResults'            => 0,
        'numLeaks'              => 0,
        'totalMemoryAllocated'  => 0,
        'maxMemoryInUse'        => 0,
        'numCallsToNew'         => 0,
        'numCallsToDelete'      => 0,
        'numCallsToArrayNew'    => 0,
        'numCallsToArrayDelete' => 0,
        'numCallsToDeleteNull'  => 0
    };
    my $fileName = shift;
    bless( $self, $class );
    if ( defined( $fileName ) )
    {
        $self->parseFile( $fileName );
    }
    return $self;
}


#========================================================================
sub parseFile
{
    my $self = shift;
    my $fileName = shift || croak( "file name required" );
    # print "attempting to parse '$fileName'\n";
    if ( -f $fileName && stat( $fileName )->size > 0 )
    {
        local *FH;
        open( FH, "$fileName" )
            or croak "Cannot open '$fileName' for reading: $!";
        # Slurp the whole file
        my @lines = <FH>;
        close( FH );
        my $results = $self;
        eval( join( "", @lines ) );
        carp $@ if $@;
        $self->{'hasResults'} = 1;
    }
    else
    {
        $self->{'hasResults'} = 0;
    }
}


#========================================================================
# setNumLeaks( int );
sub setNumLeaks
{
    my $self = shift;
    my $val = shift;
    croak( "value required" ) unless defined( $val );
    $self->{'numLeaks'} = $val;
}


#========================================================================
# setMemoryAmounts( int, int );
sub setMemoryAmounts
{
    my $self = shift;
    my $totalAllocated = shift;
    my $maxMemoryInUse = shift;
    croak( "values required" ) unless
        (defined( $totalAllocated ) && defined( $maxMemoryInUse ));
        
    $self->{'totalMemoryAllocated'} = $totalAllocated;
    $self->{'maxMemoryInUse'} = $maxMemoryInUse;
}


#========================================================================
# setNumCalls( int, int, int, int, int );
sub setNumCalls
{
    my $self = shift;
    my $toNew = shift;
    my $toDelete = shift;
    my $toArrayNew = shift;
    my $toArrayDelete = shift;
    my $toDeleteNull = shift;

    croak( "value required" ) unless
        (defined( $toNew ) && defined( $toDelete ) &&
         defined( $toArrayNew ) && defined( $toArrayDelete ) &&
         defined( $toDeleteNull ));

    $self->{'numCallsToNew'} = $toNew;
    $self->{'numCallsToDelete'} = $toDelete;
    $self->{'numCallsToArrayNew'} = $toArrayNew;
    $self->{'numCallsToArrayDelete'} = $toArrayDelete;
    $self->{'numCallsToDeleteNull'} = $toDeleteNull;
}


#========================================================================
# addToPlist( plist );
sub addToPlist
{
    my $self = shift;
    my $add = shift;
    while ( defined $add )
    {
        chomp $add;
        $self->{'plist'} .= $add;
        $add = shift;
    }
}


#========================================================================
sub numLeaks
{
    my $self = shift;
    return $self->{'numLeaks'};
}


#========================================================================
sub totalMemoryAllocated
{
    my $self = shift;
    return $self->{'totalMemoryAllocated'};
}


#========================================================================
sub maxMemoryInUse
{
    my $self = shift;
    return $self->{'maxMemoryInUse'};
}


#========================================================================
sub numCallsToNew
{
    my $self = shift;
    return $self->{'numCallsToNew'};
}


#========================================================================
sub numCallsToDelete
{
    my $self = shift;
    return $self->{'numCallsToDelete'};
}


#========================================================================
sub numCallsToArrayNew
{
    my $self = shift;
    return $self->{'numCallsToArrayNew'};
}


#========================================================================
sub numCallsToArrayDelete
{
    my $self = shift;
    return $self->{'numCallsToArrayDelete'};
}


#========================================================================
sub numCallsToDeleteNull
{
    my $self = shift;
    return $self->{'numCallsToArrayDelete'};
}


#========================================================================
sub hasResults
{
    my $self = shift;
    return $self->{'hasResults'};
}


#========================================================================
sub leakRate
{
    my $self = shift;
    my $result = 0;
    my $totalCallsToNew = $self->numCallsToNew + $self->numCallsToArrayNew;

    if ( $totalCallsToNew > 0 )
    {
        $result = $self->numLeaks / $totalCallsToNew;
    }
    return $result;
}


#========================================================================
sub plist
{
    my $self = shift;
    my $result = $self->{'plist'};
    $result =~ s/,$//o;
    return "(" . $result . ")";
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
