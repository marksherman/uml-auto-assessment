package Web_CAT::HFile::HFile_prolog;

###############################
#
# Beautifier Perl HFile
# Language: prolog
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
    $self->{stringchars}        = ["\"", "\'"];
    $self->{delimiters}         = ["(", ")", "[", "]", ".", "\"", "\'", " ", ",", "\t"];
    $self->{escchar}            = "\\";
    $self->{linecommenton}      = ["%"];
    $self->{blockcommenton}     = ["/*"];
    $self->{blockcommentoff}    = ["*/"];
    $self->{keywords}           = {
            "true"  => "1",
            "fail"  => "1",
            "not"   => "1",
            "is"    => "1",
            "\\+"   => "2",
            ":-"    => "2",
            "?-"    => "2",
            ";"     => "2",
            "!"     => "2",
            ">="    => "2",
            "=<"    => "2",
            "\\="   => "2",
            "="     => "2",
            "<="    => "2",
            "<=>"   => "2",
            "=:="   => "2",
            "=.="   => "2",
            "=="    => "2",
            "=@="   => "2",
            "=\\="  => "2",
            "?="    => "2",
            "@<"    => "2",
            "@=<"   => "2",
            "@>"    => "2",
            "@>="   => "2",
            "\\=="  => "2",
            "\\=@=" => "2",
            "abort" => "1",
            "flag"  => "1",
            "break" => "1",
            "halt"  => "1",
            "repeat"  => "1",
            "autoload" => "1",
            "abolish" => "1",
            "assert"  => "1",
            "asserta" => "1",
            "assertz" => "1",
            "assume"  => "1",
        };

# Each category can specify a Perl function that takes in the function name, and returns a string
# to put in its place. This can be used to generate links, images, etc.

$self->{linkscripts}        = {
            "1" => "donothing"};

    return $self;
}


# DoNothing link function

sub donothing
{
my ( $self ) = @_;
return;
}
1;
