#========================================================================
package Web_CAT::Beautifier;
#========================================================================
use warnings;
use strict;
use Web_CAT::Beautifier::Core;
use File::Path;
use Web_CAT::HFile::HFile_ascii;
use Web_CAT::Output::HTML;
use vars qw( @ISA %extensionMap );
use Carp;

@ISA = qw( Web_CAT::Beautifier::Core );

my %icons = (
    "Error"        => "http://web-cat.org/icons/exclaim.gif",
    "Warning"      => "http://web-cat.org/icons/caution.gif",
    "Question"     => "http://web-cat.org/icons/help.gif",
    "Suggestion"   => "http://web-cat.org/icons/suggestion.gif",
    "Answer"       => "http://web-cat.org/icons/answer.gif",
    "Good"         => "http://web-cat.org/icons/check.gif",
    "Extra Credit" => "http://web-cat.org/icons/excred.gif",
    "default"      => "http://web-cat.org/icons/todo.gif"
);

my %categoryPriority = (
    "Error"        => 1,
    "Warning"      => 2,
    "Question"     => 3,
    "Suggestion"   => 4,
    "Answer"       => 5,
    "Good"         => 6,
    "Extra Credit" => 7
);

%extensionMap = (
    "ascii"  => "ascii",
    "cpp"    => "cpp",
    "cxx"    => "cpp",
    "h"      => "cpp",
    "hpp"    => "cpp",
    "hs"     => "haskell",
    "htm"    => "html",
    "html"   => "html",
    "input"  => "ascii",
    "java"   => "java",
    "pas"    => "pascal",
    "pascal" => "pascal",
    "pl"     => "prolog",
    "plg"    => "prolog",
    "pp"     => "pascal",
    "pro"    => "prolog",
    "prolog" => "prolog",
    "py"     => "python",
    "python" => "python",
    "readme" => "ascii",
    "rss"    => "xml",
    "rkt"    => "scheme",
    "scm"    => "scheme",
    "scheme" => "scheme",
    "script" => "ascii",
    "ss"     => "scheme",
    "txt"    => "ascii",
    "text"   => "ascii",
    "xml"    => "xml",
    "xul"    => "xml",
    "zhtml"  => "html",
    "zul"    => "xml"
    );


#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $class = ref( $proto )  ||  $proto;
    my $self = $class->SUPER::new( new Web_CAT::HFile::HFile_ascii,
                                   new Web_CAT::Output::HTML );
    $self->{language} = "ascii";
    bless( $self, $class );
    return $self;
}


#========================================================================
sub setInputFileName
{
    my ( $self, $fileName ) = @_;
    my @fileNameParts = split( /\./, $fileName );
    my $extension = $fileNameParts[-1];
    $extension =~ tr/A-Z/a-z/;
    if ( defined $extensionMap{$extension} )
    {
        my $language = $extensionMap{$extension};
        if ( $language eq $self->{language} )
        {
            # print "already using $language\n";
            return 1;
        }
        else
        {
            my $langMod = "Web_CAT/HFile/HFile_${language}.pm";
            # print "langMod = $langMod\n";
            foreach my $dir ( @INC )
            {
                if ( -f "$dir/$langMod" )
                {
                    require $langMod;
                    $self->{language} = $language;
                    $self->{highlightfile} =
                        eval "Web_CAT::HFile::HFile_${language}::new "
                             . "Web_CAT::HFile::HFile_${language}";
                    $self->{fileName} = $fileName;
                    return 1;
                }
            }
        }
        return 0;
    }
    else
    {
        # print "extension not recognized: $extension\n";
        return 0;
    }
}


#========================================================================
sub loadFile
{
    my $self = shift;
    my $fileName = shift;
    if ( defined $fileName )
    {
        $self->{fileName} = $fileName;
    }
    else
    {
        $fileName = $self->{fileName};
    }
    return if ( ! -f $fileName );
    open( BEAUTIFIERFILEIN, $fileName ) or return;
    my @outstr = <BEAUTIFIERFILEIN>;
    close( BEAUTIFIERFILEIN );
    my $result = join( "", @outstr );
    # Strip all NULL characters, in case this is UTF-16 or something.
    $result =~ s/\x00//go;
    $result =~ s/^[\xfe\xff]+//o;
    return $result;
}


#========================================================================
sub highlightFile
{
    my $self = shift;
    my @lines = split( "\n", $self->highlight_text( $self->loadFile ) );
    my $numLines = scalar @lines;
    my @newLines = (
        qq(<TABLE cellspacing="0" cellpadding="0" class="srcView" )
            . qq(bgcolor="white" id="bigtab">\n),
        qq(<TBODY id="tab">)
        );
    my $messageCount = 0;

    for ( my $i = 0; $i < $numLines; $i++ )
    {
        my $num = $i + 1;
        my $id = "O:$num";
        my $line = $lines[$i];
        $line =~ s/"\@id\@"/"$id"/g;
        my $hl = "";
        my $Line = "Line";
        my $starta = "";
        my $enda = "";
        my $lineClass = "";

        if ( defined( $self->{codeMessages} )
             && defined( $self->{codeMessages}{$self->{fileName}} )
             && defined( $self->{codeMessages}{$self->{fileName}}{$num} ) )
        {
            $messageCount++;
            my $category = $self->{codeMessages}{$self->{fileName}}{$num}{category};
            
            if ($categoryPriority{$category})
            {
                $lineClass = " class=\"$category\"";
            }
            else
            {
                $starta = "<a title=\"" . $self->{codeMessages}{$self->{fileName}}{$num}{message} . "\" id=\"$id\">";
                $enda = "</a>";
            }
            $hl = "Hilight";
            $Line = $hl;
        }
        my $mungedFileName = lcfirst( $self->{fileName} );
        if ( defined( $self->{codeMessages} )
             && defined( $self->{codeMessages}{$mungedFileName} )
             && defined( $self->{codeMessages}{$mungedFileName}{$num} ) )
        {
            if ($lineClass eq "")
            {
                $messageCount++;
            }
            my $category = $self->{codeMessages}{$self->{fileName}}{$num}{category};

            if ($categoryPriority{$category})
            {
                $lineClass = " class=\"$category\"";
            }
            else
            {
                $starta = "<a title=\"" . $self->{codeMessages}{$self->{fileName}}{$num}{message} . "\" id=\"$id\">";
                $enda = "</a>";
            }
            $hl = "Hilight";
            $Line = $hl;
        }
        my $outLine = <<EOF;
<tr id="$id"$lineClass>
    <td align="right" class="lineCount$hl" id="$id">&#160;$num</td>
    <td align="right" class="lineCount$hl" id="$id">&#160;&#160;</td>
    <td class="src$Line" id="$id">
        <pre class="srcLine" id="$id">$starta&#160;$line$enda</pre>
    </td>
</tr>
EOF

        if ($lineClass)
        {
            my $messageId = "N:$messageCount:$num";
            my $commentBody = ${self}->commentBody($messageId,
                $self->{codeMessages}{$self->{fileName}}{$num});
            $outLine .= <<EOF;
<tr id="$messageId"><td colspan="3" id="$messageId">
    <img src="http://web-cat.cs.vt.edu/images/blank.gif" width="1" height="2" border="0" id="$messageId"/>
</td></tr>
<tr id="$messageId">
    <td id="$messageId">&#160;</td>
    <td id="$messageId">&#160;</td>
    <td id="$messageId">$commentBody</td>
</tr>
<tr id="$messageId"><td colspan="3" id="$messageId">
    <img src="http://web-cat.cs.vt.edu/images/blank.gif" width="1" height="2" border="0" id="$messageId"/>
</td></tr>
EOF
        }

        push(@newLines, $outLine);
    }

    push( @newLines, "</TBODY></TABLE>\n" );
    return @newLines;
}


#========================================================================
sub commentBody
{
    my $self = shift;
    my $messageId = shift;
    my $violationRef = shift;
    my (%violation) = %{$violationRef};
    my $category = $violation{category};
    my $deduction = "";
    if ($violation{deduction})
    {
        $deduction = ": -" . $violation{deduction};
    }
    my $message = $violation{message};
    my $icon = $icons{$category};
    if ( !defined( $icon ) )
    {
        $icon = $icons{default};
    }

    return <<EOF;
<table border="0" cellpadding="1" cellspacing="0" id="$messageId"><tbody>
    <tr id="$messageId"><td class="messageBox" id="$messageId">
        <img src="$icon" width="16" height="16" id="$messageId"/>
        <b id="$messageId"><span id="$messageId">$category</span>$deduction</b>
        <br id="$messageId"/>
        <i id="$messageId">$message</i>
    </td></tr>
</tbody></table>
EOF
}


#========================================================================
sub generateHighlightOutputFile
{
    my $self = shift;
    my $destPrefix = shift;
    my $fileName = shift;
    if ( defined $fileName )
    {
        $self->{fileName} = $fileName;
    }
    else
    {
        $fileName = $self->{fileName};
    }
    # print "  checking for $fileName\n";
    return if ( ! -f $fileName );
    # print "   found\n";

    # print "$fileName is file\n";

    if ( $self->setInputFileName( $fileName ) )
    {
        my $outFileName = $fileName . ".html";
        if ( defined $destPrefix )
        {
            $outFileName = "$destPrefix/$outFileName";
        }
        # print "    target => $outFileName\n";
        if ( $outFileName =~ m,/,o )
        {
            my @dirPath = split( "/", $outFileName );
            pop( @dirPath );
            mkpath( join( "/", @dirPath ) );
        }
        # print "    attempting to open output file\n";
        open( BEAUTIFIERFILEOUT, ">:utf8", $outFileName ) or return;
        my @lines = $self->highlightFile;
        print BEAUTIFIERFILEOUT @lines;
        close( BEAUTIFIERFILEOUT );
        return $outFileName;
    }
    else
    {
        return;
    }
}


#========================================================================
sub beautify
{
    my $self           = shift;
    my $fileName       = shift;
    my $outBase        = shift;
    my $outPrefix      = shift;
    my $numCodeMarkups = shift;
    my $skipExtensions = shift;
    my $cfg            = shift;
    # print "attempting to beautify $fileName\n";

    if ( defined $skipExtensions )
    {
        foreach my $ext ( @{$skipExtensions} )
        {
            if ( $fileName =~ m,\Q$ext\E$, )
            {
                return;
            }
        }
    }
    if ( -d $fileName )
    {
        # print "$fileName is directory\n";
        
        foreach my $f ( <$fileName/*> )
        {
            $self->beautify( $f, $outBase, $outPrefix, $numCodeMarkups,
                             $skipExtensions, $cfg );
        }
    }
    else
    {
        my $outfile = $self->generateHighlightOutputFile(
            "$outBase/$outPrefix", $fileName );

        if ( defined $outfile )
        {
            my $thisMarkup;
            my $mungedFileName = lcfirst( $self->{fileName} );
            if ( defined $self->{codeMarkupIds}
                 && defined $self->{codeMarkupIds}->{$fileName} )
            {
                $thisMarkup = $self->{codeMarkupIds}->{$fileName};
            }
            elsif ( defined $self->{codeMarkupIds}
                 && defined $self->{codeMarkupIds}->{$mungedFileName} )
            {
                $thisMarkup = $self->{codeMarkupIds}->{$mungedFileName};
            }
            else
            {
                $$numCodeMarkups++;
                $thisMarkup = $$numCodeMarkups;
            }
            $cfg->setProperty( "codeMarkup${thisMarkup}.sourceFileName",
                               $fileName );
            $outfile =~ s,^$outBase/,,o;
            $cfg->setProperty( "codeMarkup${thisMarkup}.markupFileName",
                               $outfile );
        }
    }
}


#========================================================================
sub beautifyCwd
{
    my $self           = shift;
    my $cfg            = shift;
    my $skipExtensions = shift;
    my $codeMarkupIds  = shift;
    my $codeMessages   = shift;
    my $outBase        = $cfg->getProperty( 'resultDir' );
    my $outPrefix      = 'html';
    my $numCodeMarkups = $cfg->getProperty( 'numCodeMarkups', 0 );

    if ( defined $codeMarkupIds )
    {
        $self->{codeMarkupIds} = $codeMarkupIds;
    }
    elsif ( defined $self->{codeMarkupIds} )
    {
        delete $self->{codeMarkupIds};
    }

    if ( defined $codeMessages )
    {
        $self->{codeMessages} = $codeMessages;
    }
    elsif ( defined $self->{codeMessages} )
    {
        delete $self->{codeMessages};
    }

    foreach my $f ( <*> )
    {
        $self->beautify( $f, $outBase, $outPrefix, \$numCodeMarkups,
                         $skipExtensions, $cfg );
    }
    $cfg->setProperty( 'numCodeMarkups', $numCodeMarkups );
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
