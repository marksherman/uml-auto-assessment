package Web_CAT::HFile::HFile_ascii;

###############################
#
# Beautifier Perl HFile
# Language: plain ascii text
#
###############################

use Web_CAT::Beautifier::HFile;
@ISA = qw(Web_CAT::Beautifier::HFile);
sub new
{
    my $class  = shift;
    my $self = bless Web_CAT::Beautifier::HFile->new(), $class;

    # Flags:
    $self->{nocase}             = 0;
    $self->{notrim}             = 1;
    $self->{perl}               = 0;
    $self->{indent}             = [];
    $self->{unindent}           = [];
    $self->{stringchars}        = [];
    $self->{delimiters}         = [];
    $self->{escchar}            = "";
    $self->{linecommenton}      = [];
    $self->{blockcommenton}     = [];
    $self->{blockcommentoff}    = [];
    $self->{keywords}           = {};

# Each category can specify a Perl function that takes in the function name, and returns a string
# to put in its place. This can be used to generate links, images, etc.

$self->{linkscripts}        = {};

    return $self;
}


# DoNothing link function

sub donothing
{
my ( $self ) = @_;
return;
}
1;
