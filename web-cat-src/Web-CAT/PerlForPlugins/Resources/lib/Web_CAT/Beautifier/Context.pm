package Web_CAT::Beautifier::Context;

# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 2 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

use Data::Dumper;
use warnings;

sub new
{
    my( $class ) = @_;
    my $self = {};
    bless $self, $class;
    return $self;
}

sub from_language
{
    my( $self, $lang, $output ) = @_;
    # Indent level
    $self->{ind}        = 0;
    # Status flags
    $self->{inquote}    = 0;
    $self->{incomment}  = 0;
    $self->{inbcomment}     = 0;
    $self->{inwhitespace}   = 1;
    # Quote match check
    $self->{currquotechar}  = "";
    # Make sure indenting doesn't go negative.
    $self->{begseen}    = 0;
    $self->{newline}    = 0;
    $self->{escaping}   = 0;
    $self->{lineselect} = 0;
    $self->{inselection}    = 0;
    $self->{closingstrings} = ();
    $self->{validkeys}  = ();
    foreach( keys(%{$lang->{keywords}}) )
    {
        if ($lang->{nocase})
        {
            $self->{validkeys}{lc($_)} = $_;            
        }
        else
        {
            $self->{validkeys}{$_} = $_;
        }
    }
    $self->{alldelims} = [];
    if ( defined $lang->{delimiters} )
    {
        push(@{$self->{alldelims}}, @{$lang->{delimiters}});
    }
    if ( defined $lang->{stringchars} )
    {
        push(@{$self->{alldelims}}, @{$lang->{stringchars}});
    }
    $self->{lcolengths} = ();
    $self->{bcolengths} = ();
    $self->{bcflengths} = ();

    foreach( @{$lang->{linecommenton}} )
    {
        $self->{lcolengths}{$_} = length($_);
    }
    foreach( @{$lang->{blockcommenton}} )
    {
        $self->{bcolengths}{$_} = length($_);   
    }
    foreach( @{$lang->{blockcommentoff}} )
    {
        $self->{bcflengths}{$_} = length($_);
    }

    $self->{bcomatches} = ();
    $self->{startingbkonchars} = ();
    my $bco = "";
    for(my $i=0; $i<(scalar @{$lang->{blockcommenton}}); $i++)
    {
        $bco = @{$lang->{blockcommenton}}[$i];
        if (!defined($self->{bcomatches}{$bco}))
        {
            $self->{bcomatches}{$bco} = ();
        }
        push(@{$self->{bcomatches}{$bco}}, @{$lang->{blockcommentoff}}[$i]);
        push(@{$self->{startingbkonchars}}, substr($bco, 0, 1));
    }
    $self->{preprolength} = 0;
    $self->{prepro} = 0;
    if (defined($lang->{prepro}))
    {
        $self->{preprolength} = length($lang->{prepro});
    }
    @{$self->{code_parts}}      = split("_WORD_", $output->{code});
    @{$self->{linecomment_parts}}   = split("_WORD_", $output->{linecomment});
    @{$self->{blockcomment_parts}}  = split("_WORD_", $output->{blockcomment});
    @{$self->{prepro_parts}}    = split("_WORD_", $output->{prepro});
    @{$self->{select_parts}}    = split("_WORD_", $output->{select});
    @{$self->{quote_parts}}     = split("_WORD_", $output->{quote});
    my $currcat = 1;
    while(1)
    {
        my $varname = "category_".$currcat;
        if (defined($output->{$varname}))
        {
            @{$self->{category_parts}{$currcat}} = split("_WORD_", $output->{$varname});
        }
        else
        {
            last;
        }
        $currcat++;
    }
}

1;
