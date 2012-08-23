package Web_CAT::Output::DocBook;

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

sub new
{
    my( $class ) = @_;
    my $self = {};
    bless $self, $class;
    $self->{html}           = 1;
    $self->{code}           = '<programlisting>_WORD_</programlisting>';
    $self->{linecomment}    = '<comment>_WORD_</comment>';
    $self->{blockcomment}   = '<comment>_WORD_</comment>';
    $self->{prepro}         = '<prepro>_WORD_</prepro>';
    $self->{select}         = '<select>_WORD_</select>';
    $self->{quote}          = '<quote>_WORD_</quote>';
    $self->{category_1}     = '<category number="1">_WORD_</category>';
    $self->{category_2}     = '<category number="2">_WORD_</category>';
    $self->{category_3}     = '<category number="3">_WORD_</category>';
    return $self;
}

1;
