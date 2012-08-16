#========================================================================
package Web_CAT::Clover::Reformatter;
#========================================================================
use strict;
use HTML::SimpleParse;
use vars qw( @ISA );
use Data::Dump qw( dump );
use Carp;

@ISA = qw( HTML::SimpleParse );

$HTML::SimpleParse::FIX_CASE = -1;

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

#========================================================================
#                      -----  PUBLIC METHODS -----
#========================================================================

#========================================================================
sub new
{
    my $proto = shift;
    my $commentList = shift;
    my $stripEmptyCoverage = shift || 0;
    my $class = ref( $proto )  ||  $proto;
    my $self = $class->SUPER::new( @_ );
    $self->{commentList}        = $commentList;
    $self->{commentId}          = 0;
    $self->{killNL}             = 0;
    $self->{killAll}            = 0;
    $self->{tableDepth}         = 0;
    $self->{spanDepth}          = [0];
#    $self->{spansInPRE}        = 0;
    $self->{lineNo}             = 0;
    $self->{recording}          = 0;
    $self->{stripEmptyCoverage} = $stripEmptyCoverage;
    bless( $self, $class );
    $self->advanceComment;
    return $self;
}


#========================================================================
sub advanceComment
{
    my $self = shift;

    if ( defined( $self->{commentList} ) &&
         $#{$self->{commentList}} >= 0 )
    {
        $self->{commentId}++;
        $self->{nextComment} = pop( @{ $self->{commentList} } );
        if ( !$self->{nextComment}->{kill}->null )
        {
            $self->advanceComment;
        }
        # else
        # {
        # print "\ncurrent comment:\n--------\n",
        #  $self->{nextComment}->data_pointer( noheader => 1, nometagen => 1 ),
        #    "--------\n";
        # }
    }
    else
    {
        delete $self->{nextComment};
        #print "\ncurrent comment:\n--------\nnull\n--------\n";
    }
}


#========================================================================
sub highestPriorityCommentClass
{
    my $self  = shift;
    my $class = $self->{nextComment}->{category}->content;
    my $priority = $categoryPriority{$class};

    for ( my $i = $#{$self->{commentList}}; $i >= 0; $i-- )
    {
        last if ( $self->{commentList}->[$i]->{line} != $self->{lineNo} );
        my $thisClass = $self->{commentList}->[$i]->{category};
        my $thisPriority = $categoryPriority{$thisClass};
        if (  $thisPriority < $priority )
        {
            $class    = $thisClass;
            $priority = $thisPriority;
        }
    }

    $class =~ tr/ /_/;
    return $class;
}


#========================================================================
sub commentBody
{
    my $self = shift;
    my $id   = "N:" . $self->{commentId} . ":" . $self->{lineNo};
    my $message = ( $self->{nextComment}->{message}->null )
        ? $self->{nextComment}->content
        : $self->{nextComment}->{message}->content;
    $message = Web_CAT::Utilities::htmlEscape( $message );
    my $deduction = "";
    if ( $self->{nextComment}->{deduction}->content > 0 )
    {
        $deduction = ": -" . $self->{nextComment}->{deduction}->content;
        if ( $self->{nextComment}->{overLimit}->content > 0 )
        {
            $deduction .=
                " <font size=\"-1\" id=\"$id\">(limit exceeded)</font>";
        }
    }
    else
    {
        if ( $self->{nextComment}->{overLimit}->content > 0 )
        {
            $deduction =
                ": 0 <font size=\"-1\" id=\"$id\">(limit exceeded)</font>";
        }
    }
    my $category = Web_CAT::Utilities::htmlEscape(
        $self->{nextComment}->{category}->content );
    my $source = ( $self->{nextComment}->{message}->null )
        ? "PMD"
        : "Checkstyle";
    my $icon = $icons{$category};
    if ( !defined( $icon ) )
    {
        $icon = $icons{default};
    }
    my $url = $self->{nextComment}->{url}->content;
    if ( !defined( $url ) )
    {
        $url = "";
    }
    elsif ( $url ne "" )
    {
        $url = "&#160;&#160;<a id=\"$id\" href=\"$url\"><img id=\"$id\" "
            . "src=\"http://web-cat.cs.vt.edu/icons/info.gif\" border=\"0\" width=\"16\" "
            . "height=\"16\" target=\"WCHelp\" alt\"help\"/>"
    }

    # still need to place icon

    my $result = <<EOF;
><tr id="$id"><td colspan="3" id="$id"><img src="http://web-cat.cs.vt.edu/images/blank.gif" width="1" height="2" alt="" border="0" id="$id"/></td></tr>
<tr id="$id"><td id="$id">&#160;</td><td id="$id">&#160;</td><td id="$id">
<table border="0" cellpadding="1" cellspacing="0" id="$id">
<tr id="$id"><td class="messageBox" id="$id">
<img src="$icon" width="16" height="16" alt="" id="$id"/> <b id="$id"><span id="$id:C">$category</span> \[$source\]$deduction</b>$url<br id="$id"/><i id="$id">
$message
</i></td></tr></table></td></tr>
<tr id="$id"><td colspan="3" id="$id"><img src="http://web-cat.cs.vt.edu/images/blank.gif" width="1" height="2" alt="" id="$id"/></td>
</tr
EOF
    # print "\n\ncomment:\n--------\n", $result;
    return $result;
}


#========================================================================
sub output_comment
{
    my $self = shift;
    $self->{killNL} = 1;
#    print "comment: <$_[0]>\n";
#    "<$_[0]>";
    "";
}


#========================================================================
sub output_markup
{
    my $self = shift;
    $self->{killNL} = 1;
#    print "markup: <$_[0]>\n";
#    "<$_[0]>";
    "";
}


#========================================================================
# sub output_ssi       { "<$_[1]>"; }


#========================================================================
sub output_text {
    my $self   = shift;
    my $text   = $_[0];
    my $killNL = $self->{killNL};
    my $t = 0;

    $self->{killNL} = 0;
    if ( ( $text =~ /^\s*$/o  &&  $killNL )  || $self->{killAll} )
    {
        return "";
    }
    $text = HTML::Entities::encode_numeric($text, '^\n\r\t \x20-\x7e');
#    $text = HTML::Entities::encode_numeric($text, '^\n\x20-\x7e');
    $text =~ s/&#([01]?[0-9A-Fa-f]);/&#171;&amp;#$1&#187;/g;
    $text =~ s/&nbsp;/&\#160;/go;
    $text =~ s/((ht|f)tps?:\/\/         # The protocol
        [\da-z\.-]+\.[a-z\.]{1,5}[a-z]  # The host name
        (:?                             # There might be a port number
         (&amp;|(?!&\#?[a-zA-Z0-9]{2,5};)[^\s,;:!()"'])*
         (?!&\#?[a-zA-Z0-9]{2,5};)[^\s,;:!()"'\.])?)
        /<a href="$1">$1<\/a>/gix;

    $self->SUPER::output_text( $text );
}


#========================================================================
sub output_starttag
{
    my $self = shift;
    my $tag  = $_[0];
    my %args = $self->parse_args( $tag );
    my $word = $tag;  $word =~ s/\s.*$//io;  $word = lc( $word );
    delete $args{ $word };

    # Handle various tag types
    #--------------------------
    if ( $word eq "html"  ||  $word eq "body" )
    {
        # print "killing <$tag>\n";
        undef $tag;
    }
    elsif ( $word eq "head" )
    {
        $self->{killAll}++;
    }
#    elsif ( $word eq "link" )
#    {
#       $tag =~ s,\.\./,,;
#    }
    elsif ( $word eq "table" )
    {
        if ( !defined( $args{class} ) )
        {
            if ( ! $self->{killAll} )
            {
                $self->{killAll}++;
            }
            $self->{tableDepth}++;
        }
        elsif ( $self->{killAll} )
        {
            $self->{tableDepth}++;
        }
        else
        {
            $tag .= ' bgcolor="white" id="bigtab"';
            $self->{recording} = 1;
            # print "looking for messages on line ", $self->{lineNo},
            #     "(next = ", $self->{nextComment}->{line}, ")\n";
            while ( defined( $self->{nextComment} )  &&
                    $self->{nextComment}->{line} == $self->{lineNo} )
            {
                $tag .= $self->commentBody;
                $self->advanceComment;
            }
        }
    }
    elsif ( $word eq "tbody" )
    {
        if ( $self->{recording} )
        {
            $tag .= ' id="tab"';
        }
    }
    elsif ( $word eq "tr" )
    {
        if ( $self->{recording} )
        {
            $self->{lineNo}++;
            $tag .= ' id="O:' . $self->{lineNo} . '"';
            if ( defined( $self->{nextComment} )  &&
                 $self->{nextComment}->{line} == $self->{lineNo} )
            {
                $tag .=
                    " class=\"" . $self->highestPriorityCommentClass . "\"";
            }
        }
    }
    elsif ( $word eq "td" || $word eq "a" )
    {
        $tag .= ' id="O:' . $self->{lineNo} . '"';
        if ( $self->{stripEmptyCoverage} )
        {
            $tag =~ s/"([^"]*)Hilight"/"$1"/o;
            if ( $word eq "a" )
            {
                $tag =~ s/title="[^"]*"\s*//o;
            }
        }

        my $spanCountList = $self->{spanDepth};
        push( @{$spanCountList}, 0 );
    }
    elsif ( $word eq "span" )
    {
        $tag =~ s/SPAN/span/io;
        $tag .= ' id="O:' . $self->{lineNo} . '"';
        if ( $self->{stripEmptyCoverage} )
        {
            $tag =~ s/"srcLineHilight"/"srcLine"/o;
        }
        my $spanCountList = $self->{spanDepth};
        $spanCountList->[$#{$spanCountList}]++;
#       if ( defined( $args{class} ) && $args{class} eq "srcHilight" )
#       {
#           undef $tag;
#           $self->{spanDepth}++;
#       }
#       else
#       {
#           if ( $self->{spanDepth} )
#           {
#               $self->{spanDepth}++;
#           }
#           $tag .= ' id="O:' . $self->{lineNo} . '"';
#           $self->{spansInPRE}++;
#       }
#       # print "<span>: spansInPRE = ", $self->{spansInPRE},
#       #       " spanDepth = ", $self->{spanDepth}, "\n";
    }
    # For all other tags
    elsif ( $self->{recording} && $tag !~ m/id="[^"]*"$/io ) #"
    {
        # ad the id tag here
        $tag .= ' id="O:' . $self->{lineNo} . '"';
    }

#    if ( $word eq "pre" )
#    {
#       $self->{spansInPRE} = 0;
#       # print "<pre>: spansInPRE = ", $self->{spansInPRE},
#       #       " spanDepth = ", $self->{spanDepth}, "\n";
#    }

    # Uniform postprocessing for all tag types
    #--------------------------
    if ( !defined( $tag ) )
    {
        $self->{killNL} = 1;
    }

    if ( defined $tag && !$self->{killAll} )
    {
        # print "leaving <$word>\n";
        # print "tag = $tag\n";
        $self->SUPER::output_starttag( $tag );
    }
}


#========================================================================
sub output_endtag
{
    my $self = shift;
    my $tag  = $_[0];
    my $word = $tag;  $word =~ s/^\///o;  $word = lc( $word );

    # Handle various tag types
    #--------------------------
    if ( $word eq "html"  ||  $word eq "body" )
    {
        # print "killing <$tag>\n";
        undef $tag;
    }
    elsif ( $word eq "head" )
    {
        # print "killing <$tag>\n";
        $self->{killAll}--;
        undef $tag;
    }
    elsif ( $word eq "table" )
    {
        if ( $self->{killAll} )
        {
            $self->{tableDepth}--;
            if ( !$self->{tableDepth} )
            {
                undef $tag;
                $self->{killAll}--;
            }
        }
        elsif ( $self->{recording} )
        {
            $self->{recording} = 0;
        }
    }
    elsif ( $word eq "span" )
    {

        $tag =~ s/SPAN/span/io;
        my $spanCountList = $self->{spanDepth};
        $spanCountList->[$#{$spanCountList}]--;
#       if ( $self->{spanDepth} )
#       {
#           $self->{spanDepth}--;
#           if ( !$self->{spanDepth} )
#           {
#               undef $tag;
#           }
#           else
#           {
#               $self->{spansInPRE}--;
#           }
#       }
#       else
#       {
#           $self->{spansInPRE}--;
#       }
#       # print "</span>: spansInPRE = ", $self->{spansInPRE},
#       #      " spanDepth = ", $self->{spanDepth}, "\n";
    }
    elsif ( $word eq "tr" )
    {
        if ( $self->{recording} )
        {
            # print "looking for messages on line ", $self->{lineNo},
            #     "(next = ", $self->{nextComment}->{line}, ")\n";
            while ( defined( $self->{nextComment} )  &&
                    $self->{nextComment}->{line} == $self->{lineNo} )
            {
                $tag .= $self->commentBody;
                $self->advanceComment;
            }
        }
    }
    elsif ( $word eq "td" || $word eq "a" )
    {
        my $spanCountList = $self->{spanDepth};
        my $spanPos = $#{$spanCountList};
        while ( $spanCountList->[$spanPos] > 0 )
        {
            $tag = "/span><" . $tag;
            $spanCountList->[$spanPos]--;
        }
        pop( @{ $spanCountList } );
    }
#    elsif ( $word eq "pre" )
#    {
#       while ( $self->{spansInPRE} > 0 )
#       {
#           $self->{spansInPRE}--;
#           if ( $self->{spanDepth} )
#           {
#               $self->{spanDepth}--;
#           }
#           $tag = "/span><" . $tag;
#       }
#       # print "</pre>: spansInPRE = ", $self->{spansInPRE},
#       #      " spanDepth = ", $self->{spanDepth}, "\n";
#    }

    # Uniform postprocessing for all tag types
    #--------------------------
    if ( !defined( $tag ) )
    {
        $self->{killNL} = 1;
    }
    if ( defined $tag && !$self->{killAll} )
    {
        # print "tag = $tag\n";
        $self->SUPER::output_endtag( $tag );
    }
}


#========================================================================
sub save
{
    my $self = shift;
    my $fileName = $_[0];
    my $fh;

    open( $fh, ">:utf8", $fileName ) ||
        die "Cannot open file for output '$fileName': $!";
    print $fh $self->get_output;
    close( $fh );
}


# ---------------------------------------------------------------------------
1;
# ---------------------------------------------------------------------------
