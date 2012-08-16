package Web_CAT::Output::ANSI;

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
    $self->{html}           = 0;
    $self->{code}           = '_WORD_';
    $self->{linecomment}    = '[01;32m_WORD_[00m';
    $self->{blockcomment}   = '[01;32m_WORD_[00m';
    $self->{prepro}         = '[01;35m_WORD_[00m';
    $self->{select}         = '[01;33m_WORD_[00m';
    $self->{quote}          = '[01;34m_WORD_[00m';
    $self->{category_1}     = '[01;31m_WORD_[00m';
    $self->{category_2}     = '[01;36m_WORD_[00m';
    $self->{category_3}     = '[01;37m_WORD_[00m';
    return $self;
}

1;

