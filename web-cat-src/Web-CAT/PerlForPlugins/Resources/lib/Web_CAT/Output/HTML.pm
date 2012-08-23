package Web_CAT::Output::HTML;

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
#            SG: 12/7/04 removed <pre></pre> from {code} since this is embedded
#                        in each line as it is put into the table.

sub new
{
    my( $class ) = @_;
    my $self = {};
    bless $self, $class;
    $self->{html}	  = 1;
#   $self->{code} 	  = '<pre>_WORD_</pre>';
    $self->{code} 	  = '_WORD_';
    $self->{linecomment}  = '<span class="comment" id="@id@">_WORD_</span>';
    $self->{blockcomment} = '<span class="comment" id="@id@">_WORD_</span>';
    $self->{prepro} 	  = '<span class="keyword" id="@id@">_WORD_</span>';
    $self->{select} 	  = '<b id="@id@">_WORD_</b>';
    $self->{quote} 	  = '<span class="string" id="@id@">_WORD_</span>';
    $self->{category_1}   = '<span class="keyword" id="@id@">_WORD_</span>';
    $self->{category_2}   = '<span class="keyword" id="@id@">_WORD_</span>';
    $self->{category_3}   = '<span class="keyword" id="@id@">_WORD_</span>';
    $self->{category_4}   = '<span class="keyword" id="@id@">_WORD_</span>';
    $self->{category_5}   = '<span class="keyword" id="@id@">_WORD_</span>';
    return $self;
}

1;
