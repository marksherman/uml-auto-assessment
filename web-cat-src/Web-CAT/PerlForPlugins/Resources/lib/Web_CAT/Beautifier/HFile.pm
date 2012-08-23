package Web_CAT::Beautifier::HFile;

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
    $self = {};
    bless $self, $class;

    $self->{config}             = ();
    $self->{validkeys}          = ();
    $self->{indent}             = ();
    $self->{unindent}           = ();
    $self->{blockcommenton}     = ();
    $self->{blockcommentoff}    = ();
    $self->{linecommenton}      = ();
    $self->{preprocessors}      = ();
    $self->{zones}              = ();
    $self->{keywords}           = ();
    $self->{keypats}            = ();
    $self->{stringchars}        = ();
    $self->{delimiters}         = ();
    $self->{delimitersinkeywords} = ();
    $self->{escchar}            = "\\";
    $self->{perl}               = 0;
    $self->{notrim}             = 1;
    $self->{nocase}             = 0;
    $self->{tabwidth}           = 8;

    return $self;
}

1;
