package Web_CAT::Beautifier::Core;

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
#
#Change Log:
#            SG: 12/7/04 Reduced the level of indentation from 8 spaces to 4 spaces.

use Web_CAT::Beautifier::Context;
use Web_CAT::Utilities;
use Data::Dumper;
use Text::Tabs qw(expand);
use warnings;
use strict;

my $DEBUG = 0;

sub new
{
    my( $class, $file, $outputmodule ) = @_;
    $outputmodule = "css" unless defined($outputmodule);

    my $self = {};
    bless $self, $class;
    $self->{highlightfile} = $file;
    $self->{output_module} = $outputmodule;

    return $self;
}

sub set_stats
{
    my( $self, $statobj) = @_;
    if (defined($statobj))
    {
        $self->{statobj} = $statobj;
    }
    return $self->{statobj};
}

sub set_output_module
{
    my( $self, $output_module) = @_;
    if (defined($output_module))
    {
        $self->{output_module} = $output_module;
    }
    return $self->{output_module};
}

sub highlight_text
{
    my( $self, $text ) = @_;
    if ( !defined $text )
    {
        $text = "";
    }
    if ($self->{output_module}->{html}) {
#       $text =~ s/&/&amp;/g;
#       $text =~ s/</&lt;/g;
#       $text =~ s/>/&gt;/g;
    }
    $self->{context} = new Web_CAT::Beautifier::Context();
    $self->{context}->from_language($self->{highlightfile}, $self->{output_module});
    $self->{langstack} = ();
    $self->{contextstack} = ();

    if (defined($self->{highlightfile}->{zones}))
    {
        # Make hash mappings from start tag to language.
        $self->{starttags} = ();
        $self->{endtags} = ();
        $self->{starttaglengths} = ();
        my $zone = ();
        foreach $zone ($self->{highlightfile}->{zones})
        {
            $self->{startmap}[@{$zone}[0]] = @{$zone}[2];
            push(@{$self->{starttags}}, @{$zone}[0]);
            if (!defined($self->{endtags}[@{$zone}[0]]))
            {
                $self->{endtags}[@{$zone}[0]] = ();
            }
            push(@{$self->{endtags}[@{$zone}[0]]}, @{$zone}[1]);
            $self->{starttaglengths}[@{$zone}[0]] = strlen(@{$zone}[0]);
        }
        $self->{endtaglist} = ();
        $self->{langcache} = ();
    }

    # Get the lines
    my @lines = split("\n", $text);
    my $numlines = scalar @lines;
    my $out = "";
    my $lineout = "";
    if ( defined $self->{highlightfile}->{tabwidth} )
    {
            $Text::Tabs::tabstop = $self->{highlightfile}->{tabwidth};
    }
    @lines = expand( @lines );
    for (my $i=0; $i<$numlines; $i++)
    {
        if (defined($self->{context}->{code_parts}[0]))
        {
        $out .= $self->{context}->{code_parts}[0];
        }

        if ($self->{context}->{inselection})
        {
        $out .= $self->{context}->{select_parts}[0];
        }
        if ($self->{context}->{inbcomment})
        {
        $out .= $self->{context}->{blockcomment_parts}[0];
        }
        if ($self->{context}->{inquote})
        {
        $out .= $self->{context}->{quote_parts}[0];
        }

        $self->{context}->{prepro} = 0;
        my $line = $lines[$i];
        if ($self->{context}->{preprolength}>0 && substr($line, 0, $self->{context}->{preprolength}) == $self->{highlightfile}->{prepro})
        {
            $out .= $self->{context}->{prepro_parts}[0];
            $self->{context}->{prepro} = 1;
        }
        $self->{context}->{inwhitespace} = 1;
        $self->{context}->{incomment} = 0;
        if (defined($self->{highlightfile}->{lineselect}) && !($self->{context}->{inselection}) && substr($line, 0, length($self->{highlightfile}->{lineselect}))==$self->{highlightfile}->{lineselect})
        {
            $out .= $self->{context}->{select_parts}[0];
            $line = substr($line, length($self->{highlightfile}->{lineselect}));
            $self->{context}->{lineselect} = 1;
        }

        if ($self->{highlightfile}->{notrim} == 0)
        {
            $line =~ s/^\s+(.*)/$1/;
        }

        my $lineorig = $line;

        my $sw = $self->starts_with($lineorig, @{$self->{highlightfile}->{unindent}});
        if ($self->{context}->{ind}>0 && $sw ne "")
        {
            $lineout = "    " x ($self->{context}->{ind}-1);
        }
        else
        {
            $lineout = "    " x ($self->{context}->{ind});
        }
        my $ln = length($lineorig);
        my $currchar = "";
        for (my $j = 0; $j < $ln; $j++)
        {
            #print $lineNumber . "  ";
            $currchar = substr($lineorig, $j, 1);
            # Opening selection blocks
            if (defined($self->{highlightfile}->{selecton}) && !$self->{context}->{inselection} && !$self->{context}->{inquote} && !$self->{context}->{inbcomment} && substr($line, $j, length($self->{highlightfile}->{selecton})) eq $self->{highlightfile}->{selecton})
            {
if ($DEBUG) { print "Selection open\n" };
                $lineout = $self->munge($lineout).$self->{context}->{select_parts}[0];
                $out .= $lineout;
                $lineout = "";
                $self->{context}->{inselection} = 1;
                $j += length($self->{highlightfile}->{selecton})-1;
                next;
            }
            # Closing selection blocks
            if (defined($self->{highlightfile}->{selectoff}) && $self->{context}->{inselection} && substr($line, $j, length($self->{highlightfile}->{selectoff})) eq $self->{highlightfile}->{selectoff})
            {
if (!$DEBUG) { print "Selection close\n"; }
                $lineout .= $self->{context}->{select_parts}[1];
                $out .= $lineout;
                $lineout = "";
                $self->{context}->{inselection} = 0;
                $j += length($self->{highlightfile}->{selectoff});
                next;
            }
            # Line comments. Once we get a line comment we can skip straight to the next line, as nothing else can be done.
            if (!$self->{context}->{lineselect} && !$self->{context}->{inselection} && !$self->{context}->{inquote} && !$self->{context}->{incomment} && !($self->{highlightfile}->{perl} && $j>0 && substr($line, $j-1, 1) eq "\$"))
            {
                my $currmax = 0;
                my $lnc = "";
                foreach(@{$self->{highlightfile}->{linecommenton}})
                {
                    if (substr($_, 0, 1) ne $currchar)
                    {
                        next;
                    }
                    my $lln = $self->{context}->{lcolengths}{$_};
                    if (substr($line, $j, $lln) eq $_)
                    {
                        if ($lln > $currmax)
                        {
                            $lnc = $_;
                            $currmax = $lln;
                        }
                    }
                }

                if ($currmax != 0)
                {
if ($DEBUG) { print "Line Comment!\n"; }
                    $line = substr($line, $j);
                    $lineout = $self->munge($lineout);
                    # TODO: doesn't munge() already do this?
                    if ($self->{output_module}->{html}) { $line = Web_CAT::Utilities::htmlEscape($line); }
                    $out .= $lineout;
                    if ($self->{context}->{prepro})
                    {
                        $out .= $self->{context}->{prepro_parts}[1];
                        $self->{context}->{prepro} = 0;
                    }
                    $out .= $self->{context}->{linecomment_parts}[0].$line;
                    if (defined($self->{statobj}) && $self->{statobj}->{harvest_comments})
                    {
                        $self->{statobj}->{comment_cache} .= " ".substr($line, $currmax);
                    }
                    $lineout = "";
                    $self->{context}->{incomment} = 1;
                    $j = $ln + 1;
                    next;
                }
            }

            # Handle block comments. This can't be done quickly (like above), as we may have a block comment
            # embedded inside a line.


            if (!$self->{context}->{lineselect} && !$self->{context}->{inselection} && !$self->{context}->{inquote} && !$self->{context}->{inbcomment} && in_array($currchar, $self->{context}->{startingbkonchars}))
            {
if ($DEBUG) { print "BC check\n"; }
                my $currmax = 0;
                my $bkcl = 0;
                my $bkc = "";
                foreach(@{$self->{highlightfile}->{blockcommenton}})
                {
                    if (substr($_, 0, 1) ne $currchar) { next; }
                    my $boln = @{$self->{context}->{bcolengths}}{$_};
                    if (substr($line, $j, $boln) eq $_)
                    {
                        if ($boln > $currmax)
                        {
                            $bkc = $_;
                            $bkcl = $boln;
                            $currmax = $boln;
                        }
                    }
                }

                if ($currmax != 0)
                {
if ($DEBUG) { print "Block Comment!\n"; }
                    if ($self->{prepro})
                    {
                        $out .= $self->{context}->{prepro_parts}[1];
                        $self->{prepro} = 0;
                    }
                    $self->{context}->{closingstrings} = @{$self->{context}->{bcomatches}}{$bkc};
                    $lineout = $self->munge($lineout);
                    if ($self->{output_module}->{html})
                    {
                        $bkc =~ s/>/&gt;/g;
                        $bkc =~ s/</&lt;/g;
                    }
                    $out .= $lineout;
                    $out .= @{$self->{context}->{blockcomment_parts}}[0].$bkc;
                    $lineout = "";
                    $self->{context}->{inbcomment} = 1;
                    $j += $bkcl-1;
                    next;
                }
            }

            # Handle closing comments
            if (!$self->{context}->{lineselect} && !$self->{context}->{inselection} && !$self->{context}->{inquote} && $self->{context}->{inbcomment})
            {
                my $currmax = 0;
                my $bku = "";
                my $bkul = 0;

                foreach my $str (@{$self->{context}->{closingstrings}})
                {
                    if (substr($str, 0, 1) ne $currchar) { next; }
                    my $bfln = $self->{context}->{bcflengths}{$str};
                    if (substr($line, $j, $bfln) eq $str)
                    {
                        if ($bfln > $currmax)
                        {
                            $bku = $str;
                            $bkul = $bfln;
                            $currmax = $bfln;
                        }
                    }
                }

                if ($currmax != 0)
                {
if ($DEBUG) { print "Closing Block Comment!\n"; }
                    if ($self->{output_module}->{html})
                    {
                        $bku =~ s/>/&gt;/g;
                        $bku =~ s/</&lt;/g;
                    }
                    $lineout .= $bku.$self->{context}->{blockcomment_parts}[1];
                    $out .= $lineout;
                    $lineout = "";
                    $self->{context}->{inbcomment} = 0;
                    $j += $bkul - 1;
                    next;
                }
            }

            # TODO: Zones

            # If we're in a comment, skip keyword checks, cache comments, and continue.

            if ($self->{context}->{incomment} || $self->{context}->{inbcomment})
            {
if ($DEBUG) { print "Skipping checks.\n"; }
                $lineout .= Web_CAT::Utilities::htmlEscape( $currchar );
                if ($self->{context}->{newline})
                {
                    if (defined($self->{statobj}) && $self->{statobj}->{harvest_comments})
                    {
                        $self->{statobj}->{comment_cache} .= " ";
                    }
                    $self->{context}->{newline} = 0;
                }
                if (defined($self->{statobj}) && $self->{statobj}->{harvest_comments})
                {
                    $self->{statobj}->{comment_cache} .= $currchar;
                }
                next;
            }
            # Indent has to be either preceded by, or be, a delimiter.
            my $delim = ($j == 0 || in_array($currchar, $self->{context}->{alldelims}) || ($j>0 && in_array(substr($lineorig, $j-1, 1), $self->{context}->{alldelims})));

            # Handle quotes
            if (!$self->{context}->{lineselect} && !$self->{context}->{inselection} && !$self->{context}->{escaping} &&
                ((in_array($currchar, $self->{highlightfile}->{stringchars}) && $self->{context}->{inquote} && $currchar eq $self->{context}->{currquotechar}) || (in_array($currchar, $self->{highlightfile}->{stringchars}) && !$self->{context}->{inquote})))
            {
                # First quote.
                if (!$self->{context}->{escaping} && defined($self->{context}->{inquote}) && !$self->{context}->{inquote})
                {
                    $lineout = $self->munge($lineout);
                    $out .= $lineout;
                    $self->{context}->{inquote} = 1;
                    if (defined($self->{statobj}) && $self->{statobj}->{harvest_strings}) { $self->{string_cache} .= " "; }
                    if ($self->{context}->{prepro})
                    {
                        $lineout = $self->{context}->{prepro_parts}[1].$currchar.$self->{context}->{quote_parts}[0];
                    }
                    else
                    {
                        $out .= $currchar.$self->{context}->{quote_parts}[0];
                        $lineout = "";
                    }
                    $self->{context}->{currquotechar} = $currchar;
                }
                # Last quote, so deactivate tag.
                elsif ($self->{context}->{inquote} && !$self->{context}->{escaping} && $currchar eq $self->{context}->{currquotechar})
                {
                    $self->{context}->{inquote} = 0;
                    if ($self->{context}->{prepro})
                    {
                        $lineout .= $self->{context}->{quote_parts}[1].$self->{context}->{prepro_parts}[0].substr($lineorig, $j, 1);
                    }
                    else
                    {
                        $lineout .= $self->{context}->{quote_parts}[1].substr($lineorig, $j, 1);
                    }
                    $out .= $lineout;
                    $lineout = "";
                    $self->{context}->{currquotechar} = "";
                }
            }

            # If we've got an indent character, increase the level and add an indent.
            elsif (!$self->{context}->{inselection} && $delim && !$self->{context}->{inquote} && (my $stri = $self->starts_with(substr($line, $j), @{$self->{highlightfile}->{indent}})) ne "")
            {
                if (!$self->{context}->{inwhitespace})
                {
                    $lineout .= "    " x $self->{context}->{ind};
                }
                $lineout .= $stri;
                $self->{context}->{ind}++;
                $j += length($stri)-1;
            }
            # If we've got an unindent (and we are indented), go back a level.
            elsif (!$self->{context}->{inselection} && $delim && $self->{context}->{ind}>0 && !$self->{context}->{inquote} && (my $stru = $self->starts_with(substr($line, $j), @{$self->{highlightfile}->{unindent}})) ne "")
            {
                $self->{context}->{ind}--;
                if (!$self->{context}->{inwhitespace})
                {
                    $lineout .= "    " x $self->{context}->{ind};
                }
                $lineout .= $stru;
                $j += length($stru) - 1;
            }
            elsif (!$self->{context}->{inwhitespace} || $currchar ne " " || $currchar ne "\t")
            {
if ($DEBUG) { print "String checks\nAdding $currchar..."; }
                if ($self->{context}->{inquote} && defined($self->{statobj}) && $self->{statobj}->{harvest_strings})
                {
                    $self->{statobj}->{string_cache} .= $currchar;
                }
                if ($self->{output_module}->{html})
                {
                    $lineout .= Web_CAT::Utilities::htmlEscape($currchar);
                }
                else
                {
                    $lineout .= $currchar;
                }
            }
            if ($self->{context}->{inquote} && $self->{context}->{escaping})
            {
                $self->{context}->{escaping} = 0;
            }
            elsif ($self->{context}->{inquote} && $currchar eq $self->{highlightfile}->{escchar} && !$self->{context}->{escaping})
            {
                $self->{context}->{escaping} = 1;
            }
        }
        if ($currchar ne " " && $currchar ne "\t")
        {
            $self->{context}->{inwhitespace} = 0;
        }
        if (!$self->{context}->{incomment} && !$self->{context}->{inbcomment} && !$self->{context}->{inquote})
        {
            $lineout = $self->munge($lineout);
if ($DEBUG) { print "Munging. Got $lineout\n"; }
        }
        if ($i<($numlines-1))
        {
            if ($self->{context}->{prepro})
            {
                $lineout .= $self->{context}->{prepro_parts}[1];
            }
        }
        # Close dangling tags.
        if ($self->{context}->{inquote})
        {
            $lineout .= $self->{context}->{quote_parts}[1];
        }

        if ($self->{context}->{incomment})
        {
            $lineout .= $self->{context}->{linecomment_parts}[1];
        }

        if ($self->{context}->{lineselect})
        {
            $lineout .= $self->{context}->{select_parts}[1];
        }
        if ($self->{context}->{inbcomment})
        {
            $lineout .= $self->{context}->{blockcomment_parts}[1];
        }
        elsif ($self->{context}->{inselection})
        {
            $lineout .= $self->{context}->{select_parts}[1];
        }
        if (defined($self->{context}->{code_parts}[1]))
        {
            $lineout .= $self->{context}->{code_parts}[1];
        }
        if ($i<($numlines-1))
        {
            $lineout .= "\n";
        }
        $out .= $lineout;
        $self->{context}->{newline} = 1;
        $self->{context}->{lineselect} = 0;
    }

    # If finished and in a comment, close it.

# Single-line comments are *always* properly closed by the logic w/i the loop
#   if ($self->{context}->{incomment} && !$commentClosed)
#   {
#       $out .= $self->{context}->{linecomment_parts}[1];
#   }
#       elsif ($self->{context}->{inbcomment})
#   {
#       $out .= $self->{context}->{blockcomment_parts}[1];
#   }
#   elsif ($self->{context}->{inselection})
#   {
#       $out .= $self->{context}->{select_parts}[1];
#   }

#   if (defined($self->{context}->{code_parts}[1]))
#   {
#       $out .= $self->{context}->{code_parts}[1];
#   }
    return $out;
}

sub in_array
{
    my ( $item, $array) = @_;
    foreach(@{$array})
    {
        if ($item eq $_) { return $item; }
    }
    return "";
}

sub starts_with
{
    my( $self, $text, @array) = @_;
    my $ml = 0;
    my $curr = "";
    my $l = 0;
    foreach(@array)
    {
        $l = length($_);
        if (((!$self->{highlightfile}->{nocase} && substr($text, 0, $l) eq $_) || ($self->{highlightfile}->{nocase} && lc(substr($text, 0, $l)) eq lc($_))) && (substr($text, $l, 1) eq " " || $l == 1 || substr($text, $l, 1) eq "\n" || substr($text, $l, 1) eq "\t" || substr($text, $l, 1) eq "." || substr($text, $l, 1) eq ";" || $l == length($text)))
        {
            if ($l > $ml)
            {
                $curr = substr($text, 0, $l);
                $ml = $l;
            }
        }
    }
    return $curr;
}

sub munge
{
    my( $self, $munge ) = @_;
    $_ = $munge;
    s/&gt;/>/g;
    s/&lt;/</g;
    $munge = $_;

    my $inword  = 0;
    my $currword    = "";
    my $oldword = "";
    my $checkword   = "";
    my $currchar    = "";
    my $strout  = "";
    my $lngth   = length($munge);
    if ($self->{context}->{inselection} || $self->{context}->{lineselect}) { return $munge; }
    if (!$self->{context}->{prepro})
    {
        for (my $i=0; $i<=$lngth; $i++)
        {
            $currchar = substr($munge, $i, 1);
            my $delim = in_array($currchar, $self->{highlightfile}->{delimiters});
            if ($delim || $i==$lngth)
            {
                my $deliminkeyword = $delim
                    && in_array($currchar,
                        $self->{highlightfile}->{delimitersinkeywords});
                if ($inword)
                {
                    if ($deliminkeyword)
                    {
                        $currword .= $currchar;
                        $currchar = "";
                    }
                    $inword = 0;
                    $oldword = $currword;
                    $checkword = $oldword;
                    if ($self->{highlightfile}->{nocase})
                    {
                        $checkword = lc($checkword);
                    }
                    $_ = $currword;
                    if ($self->{output_module}->{html})
                    {
                        s/</&lt;/g;
                        s/>/&gt;/g;
                    }
                    $currword = $_;
                    if (defined($self->{context}->{validkeys}{$checkword}))
                    {
                        if ($self->{highlightfile}->{nocase}) { $checkword = $self->{context}->{validkeys}{$checkword}; }
                        my $category = $self->{highlightfile}->{keywords}{$checkword};
                        my $outchunk = "";
                        if (defined $self->{context}->{category_parts}{$category}[0])
                        {
                            $outchunk = $self->{context}->{category_parts}{$category}[0];
                        }
                        if (defined $currword)
                        {
                            $outchunk .= $currword;
                        }
                        if (defined $self->{context}->{category_parts}{$category}[1])
                        {
                            $outchunk .= $self->{context}->{category_parts}{$category}[1];
                        }
                        $strout .= $outchunk;
                        # TODO: Linkscripts
                    }
                    else
                    {
                        $strout .= $currword;
                    }
                }
                else
                {
                    $inword = 1;
                    $currword = $currchar;
                    $currchar = "";
                }
                $_ = $currchar;
                if ($self->{output_module}->{html})
                {
                    s/</&lt;/g;
                    s/>/&gt;/g;
                }
                $strout .= $_;
            }
            else
            {
                if ($inword)
                {
                    $currword .= $currchar;
                }
                else
                {
                    $inword = 1;
                    $currword = $currchar;
                }
            }
        }
    }
    else
    {
        if ($self->{output_module}->{html})
        {
            $strout = Web_CAT::Utilities::htmlEscape($munge);
        }
        else
        {
            $strout = $munge;
        }
    }
    $strout =~ s/((ht|f)tps?:\/\/       # The protocol
        [\da-z\.-]+\.[a-z\.]{1,5}[a-z]  # The host name
        (:?                             # There might be a port number
         (&amp;|(?!&\#?[a-zA-Z0-9]{2,5};)[^\s,;:!()"'])*
         (?!&\#?[a-zA-Z0-9]{2,5};)[^\s,;:!()"'\.])?)
        /<a href="$1">$1<\/a>/giox;
    return $strout;
}

1;
