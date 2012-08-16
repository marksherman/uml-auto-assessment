package Web_CAT::HFile::HFile_pascal;

###############################
#
# Beautifier Perl HFile
# Language: Pascal
#
###############################

use Web_CAT::Beautifier::HFile;
@ISA = qw(Web_CAT::Beautifier::HFile);
sub new
{
    my $class  = shift;
    my $self = bless Web_CAT::Beautifier::HFile->new(), $class;

    # Flags:
    $self->{nocase}             = 1;
    $self->{notrim}             = 1;
    $self->{perl}               = 0;
    $self->{indent}             = [];  # ["begin"];
    $self->{unindent}           = [];  # ["end"];
    $self->{stringchars}        = ["'"];
    $self->{delimiters}         = ['@', "^", "*", "(", ")", "-", "+",
                                   "=", "/", "[", "]", ":", ";", "'",
                                   "<", ">", " ", ",", "\t", "."];
    $self->{escchar}            = "";
    $self->{linecommenton}      = ["//"];
    $self->{blockcommenton}     = ["{", "(*"];
    $self->{blockcommentoff}    = ["}", "*)"];
    $self->{keywords}           = {
            "absolute" => "1",
            "array" => "1",
            "assembler" => "1",
            "const" => "1",
            "constructor" => "1",
            "destructor" => "1",
            "export" => "1",
            "exports" => "1",
            "external" => "1",
            "far" => "1",
            "file" => "1",
            "forward" => "1",
            "function" => "1",
            "implementation" => "1",
            "index" => "1",
            "inherited" => "1",
            "inline" => "1",
            "interface" => "1",
            "interrupt" => "1",
            "library" => "1",
            "near" => "1",
            "nil" => "1",
            "object" => "1",
            "of" => "1",
            "packed" => "1",
            "private" => "1",
            "procedure" => "1",
            "program" => "1",
            "public" => "1",
            "record" => "1",
            "resident" => "1",
            "set" => "1",
            "string" => "1",
            "type" => "1",
            "unit" => "1",
            "uses" => "1",
            "var" => "1",
            "virtual" => "1",
            "asm" => "2",
            "begin" => "2",
            "case" => "2",
            "do" => "2",
            "downto" => "2",
            "else" => "2",
            "end" => "2",
            "for" => "2",
            "goto" => "2",
            "if" => "2",
            "label" => "2",
            "repeat" => "2",
            "then" => "2",
            "to" => "2",
            "until" => "2",
            "while" => "2",
            "with" => "2",
            "and" => "3",
            "div" => "3",
            "in" => "3",
            "mod" => "3",
            "not" => "3",
            "or" => "3",
            "shl" => "3",
            "shr" => "3",
            "xor" => "3",
            "+" => "3",
            "-" => "3",
            "*" => "3",
            ":" => "3",
            "=" => "3",
            "/" => "3",
            ">" => "3",
            "<" => "3",
            "^" => "3"};

# Each category can specify a Perl function that takes in the function name, and returns a string
# to put in its place. This can be used to generate links, images, etc.

$self->{linkscripts}        = {
            "1" => "donothing",
            "2" => "donothing",
            "3" => "donothing"};

    return $self;
}


# DoNothing link function

sub donothing
{
my ( $self ) = @_;
return;
}
1;
