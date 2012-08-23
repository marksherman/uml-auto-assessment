package Web_CAT::HFile::HFile_cpp;

###############################
#
# Beautifier Perl HFile
# Language: C++
#
###############################

use Web_CAT::Beautifier::HFile;
@ISA = qw(Web_CAT::Beautifier::HFile);
sub new
{
    my $class  = shift;
    my $self = bless Web_CAT::Beautifier::HFile->new(), $class;

    # Flags:
    $self->{nocase}             = "0";
    $self->{notrim}             = "1";
    $self->{perl}               = "1";

    # Indent Strings
    $self->{indent}             = [];  # ["{"];
    $self->{unindent}           = [];  # ["}"];

    # String Chars and delimiters
    $self->{stringchars}        = ["\"", "'"];
    $self->{delimiters}         = ["~", "!", "@", "%", "^", "&", "*", "(", ")", "-", "+", "=", "|", "{", "}", "[", "]", "<", ">", ":", ";", "\"", "'", " ", "," ,"\t" , ".", "?",];
    $self->{escchar}            = "\\";

    # Comment settings
    $self->{linecommenton}      = ["//"];
    $self->{blockcommenton}     = ["/*"];
    $self->{blockcommentoff}    = ["*/"];
    $self->{tabwidth}               = 4;

    #Keywords
    $self->{keywords}           = {
            "bool"          => "1",
            "char"      => "1",
            "class"     => "1",
            "const"     => "1",
            "case"      => "1",
            "catch"     => "1",
            "const_cast"    => "1",
            "double"        => "1",
            "double"        => "1",
            "default"       => "1",
            "do"            => "1",
            "delete"        => "1",
            "dynamic_cast"  => "1",
            "else"          => "1",
            "enum"      => "1",
            "explicit"      => "1",
            "export"        => "1",
            "extern"        => "1",
            "for"           => "1",
            "false"     => "1",
            "float"     => "1",
            "friend"        => "1",
            "if"            => "1",
            "inline"        => "1",
            "int"           => "1",
            "long"          => "1",
            "mutable"       => "1",
            "new"           => "1",
            "namespace" => "1",
            "operator"      => "1",
            "protected"     => "1",
            "private"       => "1",
            "public"        => "1",
            "reinterpret_cast"  => "1",
            "return"        => "1",
            "short"     => "1",
            "signed"        => "1",
            "sizeof"        => "1",
            "static"        => "1",
            "struct"        => "1",
            "static_cast"   => "1",
            "switch"        => "1",
            "template"      => "1",
            "throw"     => "1",
            "true"          => "1",
            "typedef"       => "1",
            "typename"      => "1",
            "this"          => "1",
            "try"           => "1",
            "typeid"        => "1",
            "union"     => "1",
            "unsinged"      => "1",
            "using"     => "1",
            "virtual"       => "1",
            "void"          => "1",
            "volatile"      => "1",
            "wchar_t"       => "1",
            "while"     => "1",
            "asm"           => "2",
            "auto"      => "2",
            "break"     => "2",
            "continue"      => "2",
            "goto"      => "2",
            "register"      => "2",
            "#define"       => "3",
            "#error"        => "3",
            "#include"      => "3",
            "#elif"     => "3",
            "#if"           => "3",
            "#line"     => "3",
            "#else"     => "3",
            "#ifdef"        => "3",
            "#pragma"       => "3",
            "#endif"        => "3",
            "#ifndef"       => "3",
            "#undef"        => "3",
            "+"         => "4",
            "-"         => "4",
            "="         => "4",
            "//"            => "4",
            "/"         => "4",
            "%"         => "4",
            "&"         => "4",
            ">"         => "4",
            "<"         => "4",
            "^"         => "4",
            "!"         => "4",
            "|"         => "4",
            "*"         => "4",
            "{"         => "5",
            "}"         => "5",
            ";"         => "5",
            "("         => "5",
            ")"         => "5",
            ","         => "5",
        };


# Each category can specify a Perl function that takes in the function name, and returns a string
# to put in its place. This can be used to generate links, images, etc.

$self->{linkscripts}        = {
            "1" => "donothing",
            "2" => "donothing",
            "3" => "donothing",
            "4" => "donothing",
            "5" => "donothing"};

    return $self;
}


# DoNothing link function

sub donothing
{
my ( $self ) = @_;
return;
}
1;
