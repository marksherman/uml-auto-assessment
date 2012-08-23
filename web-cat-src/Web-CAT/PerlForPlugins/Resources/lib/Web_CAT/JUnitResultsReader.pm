#========================================================================
package Web_CAT::JUnitResultsReader;
#========================================================================
use warnings;
use strict;
use Carp;
use File::stat;
use Web_CAT::Utilities qw(htmlEscape);
use Data::Dumper;


#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $class = ref( $proto )  ||  $proto;
    my $self = {
        'hints'      => [{}, {}, {}],
        'plist'      => "",
        'perlList'   => "",
        'executed'   => 0.0,
        'failed'     => 0.0,
        'hasResults' => 0,
        'hintId'     => 0
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
# addHint( mandatory, hint, trace );
sub addHint
{
    my $self     = shift;
    my $category = shift;
    my $priority = shift;

    # legacy support, for calls with no priority specified (i.e., hint next)
    if ($priority !~ m/^[+-]?\d+$/o)
    {
        # priority wasn't an integer, so assume it was a hint
        unshift @_, $priority;
        $priority = 0;
    }

    my $hint     = shift; croak( "hint text required" ) if !defined( $hint );
    my $trace    = shift;
    croak( "category required" ) if !defined( $category );

#    print "addHint( $category, \"$hint\", ... );\n";
    my $hintRecord;
    if ( !exists( $self->{'hints'}->[$category]->{$hint} ) )
    {
        # print "adding new hint record\n";
        $hintRecord = {
            'text'     => $hint,
            'count'    => 0,
            'priority' => $priority,
            'id'       => ++$self->{'hintId'}
        };
        if ( defined( $trace ) )
        {
            $hintRecord->{'trace'} = $trace;
        }
        $self->{'hints'}->[$category]->{$hint} = $hintRecord;
    }
    else
    {
        $hintRecord = $self->{'hints'}->[$category]->{$hint};
    }
    $hintRecord->{'count'}++;
    if ($hintRecord->{'priority'} < $priority)
    {
        $hintRecord->{'priority'} = $priority;
    }
    if ( defined( $trace ) && !exists( $hintRecord->{'trace'} ) )
    {
        $hintRecord->{'trace'} = $trace;
    }
#    {
#       my $trace = "";
#       if ( exists( $hintRecord->{'trace'} ) )
#       {
#           $trace = $hintRecord->{'trace'};
#       }
#       print "hint: ", $hintRecord->{'text'}, "\n\tcount = ",
#           $hintRecord->{'count'}, "\n\ttrace = ", $trace, "\n\n";
#    }
}


#========================================================================
# addTestsExecuted( dbl );
sub addTestsExecuted
{
    my $self = shift;
    my $amt = shift;
    croak( "amount required" ) unless defined( $amt );
    $self->{'executed'} += $amt;
}


#========================================================================
# addTestsFailed( dbl );
sub addTestsFailed
{
    my $self = shift;
    my $amt = shift;
    croak( "amount required" ) unless defined( $amt );
    $self->{'failed'} += $amt;
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
# addToPlist( plist );
sub addToPerlList
{
    my $self = shift;
    my $add = shift;
    while ( defined $add )
    {
        chomp $add;
        $self->{'perlList'} .= $add;
        $add = shift;
    }
}


#========================================================================
sub testsExecuted
{
    my $self = shift;
    return $self->{'executed'};
}


#========================================================================
sub testsFailed
{
    my $self = shift;
    return $self->{'failed'};
}


#========================================================================
sub hasResults
{
    my $self = shift;
    return $self->{'hasResults'};
}


#========================================================================
sub testPassRate
{
    my $self = shift;
    my $result = 0;
    if ( $self->testsExecuted > 0 )
    {
        $result = ( $self->testsExecuted - $self->testsFailed )
            / $self->testsExecuted;
    }
    return $result;
}


#========================================================================
sub allTestsPass
{
    my $self = shift;
    my $result = ( $self->testsExecuted > 0 )  &&  ( $self->testsFailed == 0 );

    # Ensure an integer return value for properties file usage
    if (!$result)
    {
        $result = 0;
    }
    return $result;
}


#========================================================================
sub allTestsFail
{
    my $self = shift;
    my $result = ( $self->testsExecuted > 0 )
        && ( $self->testsFailed == $self->testsExecuted );

    # Ensure an integer return value for properties file usage
    if (!$result)
    {
        $result = 0;
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


#========================================================================
sub perlList
{
    my $self = shift;
    my $result = $self->{'perlList'};
    $result =~ s/,$//o;
    return "[" . $result . "]";
}


#========================================================================
# formatHints( category, limit );
sub formatHints
{
    my $self          = shift;
    my $category      = shift || 0;
    my $limit         = shift || -1;
    my $showMandatory = shift || 0;
    my $result        = undef;

    my @hints = sort
        { ( $b->{'priority'} <=> $a->{'priority'} )
          || ( $b->{'count'} <=> $a->{'count'} )
          || ( $a->{'id'} <=> $b->{'id'} ) }
        values %{$self->{'hints'}->[$category]};

    my $total = $#hints + 1;
    if ( $limit < 0 || $limit > $total )
    {
        $limit = $total;
    }

    if ( $category == 0 && $showMandatory )
    {
        my @mandatory = sort
        { ( $b->{'priority'} <=> $a->{'priority'} )
          || ( $b->{'count'} <=> $a->{'count'} )
          || ( $a->{'id'} <=> $b->{'id'} ) }
            values %{$self->{'hints'}->[$category + 1]};
        $limit += $#mandatory + 1;
        @hints = (@mandatory, @hints);
    }

    my $shown = $limit;

#    print "sorted hints:\n";
#    foreach my $hint ( @hints )
#    {
#       my $trace = "";
#       if ( exists( $hint->{'trace'} ) )
#       {
#           $trace = $hint->{'trace'};
#       }
#       print "hint: ", $hint->{'text'}, "\n\tcount = ",
#           $hint->{'count'}, "\n\ttrace = ", $trace, "\n\n";
#    }

    if ( $limit > 0 && $#hints >= 0 )
    {
        $result = "<ul>";
        while ( $limit >0 && $#hints >= 0 )
        {
            my $hint = pop( @hints );
            $result .= "<li><p>" . htmlEscape($hint->{'text'});
            if ( $hint->{'count'} > 1 )
            {
                $result .= " (" . $hint->{'count'} . " occurrences)";
            }
            $result .= "</p>\n";
            if ( defined $hint->{'trace'} )
            {
                $result .= "<pre>\n" . htmlEscape($hint->{'trace'})
                    . "</pre>\n";
            }
            $result .= "</li>";

            $limit--;
        }
        $result .= "</ul>";
        if ( $#hints >= 0 )
        {
            $result .= "<p>(only $shown of $total hints shown)</p>";
        }
    }

    return $result;
}


#========================================================================
# hintsAsPerlDump;
sub hintsAsPerlDump
{
    my $self = shift;
    my $result = Data::Dumper->new([$self->{'hints'}])->Indent(0)->Dump;
    $result =~ s/^\$VAR[0-9]+\s*=\s*//o;
    $result =~ s/\s*;\s*$//o;
    return $result;
}


#========================================================================
# saveToCfg(simpleCfg, propertyPrefix);
sub saveToCfg
{
    my $self   = shift;
    my $cfg    = shift || croak "cfg required";
    my $prefix = shift || croak "property prefix required";

    $cfg->setProperty($prefix . '.results',      $self->plist);
    $cfg->setProperty($prefix . '.results.perl', $self->perlList);
    $cfg->setProperty($prefix . '.executed',     $self->testsExecuted);
    $cfg->setProperty($prefix . '.passed',
        $self->testsExecuted - $self->testsFailed);
    $cfg->setProperty($prefix . '.failed',       $self->testsFailed);
    $cfg->setProperty($prefix . '.passRate',     $self->testPassRate);
    $cfg->setProperty($prefix . '.allPass',      $self->allTestsPass);
    $cfg->setProperty($prefix . '.allFail',      $self->allTestsFail);
    $cfg->setProperty($prefix . '.hints',        $self->hintsAsPerlDump);
}


#========================================================================
# loadFromCfg(simpleCfg, propertyPrefix);
sub loadFromCfg
{
    my $self   = shift;
    my $cfg    = shift || croak "cfg required";
    my $prefix = shift || croak "property prefix required";

    $self->{'plist'}    = $cfg->getProperty($prefix . '.results', '');
    $self->{'perlList'} = $cfg->getProperty($prefix . '.results.perl', '');
    $self->{'executed'} = $cfg->getProperty($prefix . '.executed', 0.0);
    eval('$self->{\'hints\'} = ' .
        $cfg->getProperty($prefix . '.hints', '[{}, {}, {}]'));
    $self->{'hasResults'} = ($self->{'executed'} > 0) ? 1 : 0;
    $self->{'hintId'} = 0;
    for my $catMap ($self->{'hints'})
    {
        for my $key (keys %{$catMap})
        {
            my $id = $catMap->{$key}->{'id'};
            if ($id > $self->{'hintId'})
            {
                $self->{'hintId'} = $id;
            }
        }
    }
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
