#!/usr/bin/perl
#=============================================================================
#   @(#)$Id: execute.pl,v 1.30 2012/02/20 05:22:54 stedwar2 Exp $
#-----------------------------------------------------------------------------
#   Web-CAT: execute script for Java submissions
#
#   usage:
#       execute.pl <properties-file>
#=============================================================================

use strict;
use Carp qw(carp croak);
use Config::Properties::Simple;
use File::Basename;
use File::Copy;
use File::Spec;
use File::stat;
use Proc::Background;
use Web_CAT::Beautifier;
use Web_CAT::Clover::Reformatter;
use Web_CAT::FeedbackGenerator;
use Web_CAT::JUnitResultsReader;
use Web_CAT::Utilities
    qw(confirmExists filePattern copyHere htmlEscape addReportFile scanTo
       scanThrough linesFromFile addReportFileWithStyle);
use XML::Smart;
#use Data::Dump qw(dump);

my @beautifierIgnoreFiles = ('.java');


#=============================================================================
# Bring command line args into local variables for easy reference
#=============================================================================
my $propfile   = $ARGV[0];     # property file name
my $cfg        = Config::Properties::Simple->new(file => $propfile);

my $pid        = $cfg->getProperty('userName');
my $workingDir = $cfg->getProperty('workingDir');
my $pluginHome = $cfg->getProperty('pluginHome');
{
    # scriptHome is deprecated, but may still be used on older servers
    if (! defined $pluginHome)
    {
        $pluginHome = $cfg->getProperty('scriptHome');
    }
}
my $resultDir  = $cfg->getProperty('resultDir');
my $timeout    = $cfg->getProperty('timeout', 45);
my $publicDir  = "$resultDir/public";

my $maxToolScore          = $cfg->getProperty('max.score.tools', 20);
my $maxCorrectnessScore   = $cfg->getProperty('max.score.correctness',
                                              100 - $maxToolScore);


#=============================================================================
# Import local libs
#=============================================================================

my $localLib = "$pluginHome/perllib";
push @INC, $localLib;
eval "require JavaTddPlugin";


#my $instructorCases        = 0;
#my $instructorCasesPassed  = undef;
my $instructorCasesPercent = 0;
my $studentCasesPercent    = 0;
my $codeCoveragePercent    = 0;
#my $studentTestMsgs;
my $hasJUnitErrors         = 0;

my %status = (
    'antTimeout'         => 0,
    'studentHasSrcs'     => 0,
    'studentTestResults' => undef,
    'instrTestResults'   => undef,
    'toolDeductions'     => 0,
    'compileMsgs'        => "",
    'compileErrs'        => 0,
    'feedback'           =>
        new Web_CAT::FeedbackGenerator($resultDir, 'feedback.html'),
    'instrFeedback'      =>
        new Web_CAT::FeedbackGenerator($resultDir, 'staffFeedback.html')
);


#-------------------------------------------------------
# In addition, some local definitions within this script
#-------------------------------------------------------
Web_CAT::Utilities::initFromConfig($cfg);
if (defined($ENV{JAVA_HOME}))
{
    # Make sure selected Java is at the head of the path ...
    $ENV{PATH} =
        "$ENV{JAVA_HOME}" . $Web_CAT::Utilities::FILE_SEPARATOR . "bin"
        . $Web_CAT::Utilities::PATH_SEPARATOR . $ENV{PATH};
}

die "ANT_HOME environment variable is not set! (Should come from ANTForPlugins)"
    if !defined($ENV{ANT_HOME});
$ENV{PATH} =
    "$ENV{ANT_HOME}" . $Web_CAT::Utilities::FILE_SEPARATOR . "bin"
    . $Web_CAT::Utilities::PATH_SEPARATOR . $ENV{PATH};

my $ANT                 = "ant";
my $callAnt             = 1;
my $antLogRelative      = "ant.log";
my $antLog              = "$resultDir/$antLogRelative";
my $scriptLogRelative   = "script.log";
my $scriptLog           = "$resultDir/$scriptLogRelative";
my $markupPropFile      = "$pluginHome/markup.properties";
my $pdfPrintoutRelative = "$pid.pdf";
my $pdfPrintout         = "$resultDir/$pdfPrintoutRelative";
my $diagramsRelative    = "diagrams";
my $diagrams            = "$publicDir/$diagramsRelative";
my $can_proceed         = 1;
my $buildFailed         = 0;
my $antLogOpen          = 0;
my $postProcessingTime  = 20;


#-------------------------------------------------------
# In the future, these could be set via parameters set in Web-CAT's
# interface
#-------------------------------------------------------
my $debug             = $cfg->getProperty('debug',      0);
my $hintsLimit        = $cfg->getProperty('hintsLimit', 3);
my $maxRuleDeduction  = $cfg->getProperty('maxRuleDeduction', $maxToolScore);
my $expSectionId      = $cfg->getProperty('expSectionId', 0);
my $defaultMaxBeforeCollapsing = 100000;
my $toolDeductionScaleFactor =
    $cfg->getProperty('toolDeductionScaleFactor', 1);
my $coverageMetric    = $cfg->getProperty('coverageMetric', 0);
my $minCoverageLevel =
    $cfg->getProperty('minCoverageLevel', 0.0);

my $allStudentTestsMustPass =
    $cfg->getProperty('allStudentTestsMustPass', 0);
$allStudentTestsMustPass =
    ($allStudentTestsMustPass =~ m/^(true|on|yes|y|1)$/i);
my $studentsMustSubmitTests =
    $cfg->getProperty('studentsMustSubmitTests', 0);
$studentsMustSubmitTests =
    ($studentsMustSubmitTests =~ m/^(true|on|yes|y|1)$/i);
if (!$studentsMustSubmitTests) { $allStudentTestsMustPass = 0; }
my $requireSimpleExceptionCoverage =
    $cfg->getProperty('requireSimpleExceptionCoverage', 0);
$requireSimpleExceptionCoverage =
    ($requireSimpleExceptionCoverage =~ m/^(true|on|yes|y|1)$/i);
my $requireSimpleGetterSetterCoverage =
    $cfg->getProperty('requireSimpleGetterSetterCoverage', 0);
$requireSimpleGetterSetterCoverage =
    ($requireSimpleGetterSetterCoverage =~ m/^(true|on|yes|y|1)$/i);
my $junitErrorsHideHints =
    $cfg->getProperty('junitErrorsHideHints', 0);
$junitErrorsHideHints =
    ($junitErrorsHideHints =~ m/^(true|on|yes|y|1)$/i)
    && $studentsMustSubmitTests;
{
    # Suppress hints if another plug-in in the pipeline will be handling them
    my $hintProcessor = $cfg->getProperty('pipeline.hintProcessor');
    if (defined $hintProcessor &&
        $hintProcessor ne $cfg->getProperty('pluginName', 'JavaTddPlugin'))
    {
        $hintsLimit = 0;
    }
}


#=============================================================================
# Adjust hints limit, if needed
#=============================================================================
my $extraHintMsg = "";
if ($hintsLimit)
{
    my $hideHintsWithin = $cfg->getProperty('hideHintsWithin', 0);
    if ($hideHintsWithin > 0)
    {
        my $daysBeforeDeadline =
            ($cfg->getProperty('dueDateTimestamp', 0)
            - $cfg->getProperty('submissionTimestamp', 0))
            / (1000 * 60 * 60 * 24);
        if ($daysBeforeDeadline > 0 && $daysBeforeDeadline < $hideHintsWithin)
        {
            # Then we're within X days of deadline.  Check to see if we're
            # within the re-enable window
            my $showHintsWithin = $cfg->getProperty('showHintsWithin', 0);
            if ($daysBeforeDeadline > $showHintsWithin)
            {
                $hintsLimit = 0;
                my $days = "day";
                if ($hideHintsWithin != 1)
                {
                    $days .= "s";
                }
                $extraHintMsg = "Hints are not available within "
                    . "$hideHintsWithin $days of the deadline.";
                if ($showHintsWithin > 0)
                {
                    $days = "day";
                    if ($showHintsWithin != 1)
                    {
                        $days .= "s";
                    }
                    $extraHintMsg .= "  Hints will be available again "
                        . "$showHintsWithin $days before the deadline.";
                }
            }
        }
    }
}


#=============================================================================
# Transform simple java file patterns to
#=============================================================================
sub setClassPatternIfNeeded
{
    my $inProperty = shift || carp "incoming property name required";
    my $outProperty = shift || carp "outgoing property name required";
    my $useJavaExtension = shift;

    if (!defined $useJavaExtension)
    {
        $useJavaExtension = 0;
    }

    my $inExtension  = $useJavaExtension ? ".class" : ".java";
    my $outExtension = $useJavaExtension ? ".java"  : ".class";

    my $value = $cfg->getProperty($inProperty);
    if (defined $value && $value ne "")
    {
        my $wantAllDirs = ($value !~ m,/,);
        my $pattern = undef;
        foreach my $include (split(/[,\s]+/, $value))
        {
            if (defined($include) && $include ne "")
            {
                if ($include !~ m/^none$/io)
                {
                    if ($include =~ /\./)
                    {
                        $include =~ s/\Q$inExtension\E$/$outExtension/i;
                    }
                    else
                    {
                        $include .= $outExtension;
                    }
                    if ($wantAllDirs)
                    {
                        $include = "**/$include";
                    }
                }

                if (defined $pattern)
                {
                    $pattern .= " $include";
                }
                else
                {
                    $pattern = $include;
                }
            }
        }
        if (defined $pattern)
        {
            $cfg->setProperty($outProperty, $pattern);
        }
    }
}


#=============================================================================
# Generate derived properties for ANT
#=============================================================================
# testCases
my $scriptData = $cfg->getProperty('scriptData', '.');
$scriptData =~ s,/$,,;

# testCases (reference test location and/or file name).
# This first var needs to be visible for later pattern matching to remove
# internal path names from student messages.
my $testCasePathPattern;
{
    my $testCasePath = "${pluginHome}/tests";
    my $testCaseFileOrDir = $cfg->getProperty('testCases');
    if (defined $testCaseFileOrDir && $testCaseFileOrDir ne "")
    {
        my $target = confirmExists($scriptData, $testCaseFileOrDir);
        if (-d $target)
        {
            $cfg->setProperty('testCasePath', $target);
        }
        else
        {
            $cfg->setProperty('testCasePath', dirname($target));
            $cfg->setProperty('testCasePattern', basename($target));
            $cfg->setProperty('justOneTestClass', 'true');
        }
        $testCasePath = $target;
    }
    $testCasePathPattern = filePattern($testCasePath);
}

# Set up other test case filtering patterns
setClassPatternIfNeeded('refTestInclude', 'refTestClassPattern');
setClassPatternIfNeeded('refTestExclude', 'refTestClassExclusionPattern');
setClassPatternIfNeeded('studentTestInclude', 'studentTestClassPattern');
setClassPatternIfNeeded('studentTestExclude',
    'studentTestClassExclusionPattern');
setClassPatternIfNeeded('staticAnalysisInclude', 'staticAnalysisSrcPattern', 1);
setClassPatternIfNeeded('staticAnalysisExclude',
    'staticAnalysisSrcExclusionPattern', 1);


# useDefaultJar
{
    my $useDefaultJar = $cfg->getProperty('useDefaultJar');
    if (defined $useDefaultJar && $useDefaultJar =~ /false|\b0\b/i)
    {
        $cfg->setProperty('defaultJars', "$pluginHome/empty");
    }
}

# assignmentJar
{
    my $jarFileOrDir = $cfg->getProperty('assignmentJar');
    if (defined $jarFileOrDir && $jarFileOrDir ne "")
    {
        my $path = confirmExists($scriptData, $jarFileOrDir);
        $cfg->setProperty('assignmentClassFiles', $path);
        if (-d $path)
        {
            $cfg->setProperty('assignmentClassDir', $path);
        }
    }
}

# classpathJar
{
    my $jarFileOrDir = $cfg->getProperty('classpathJar');
    if (defined $jarFileOrDir && $jarFileOrDir ne "")
    {
        my $path = confirmExists($scriptData, $jarFileOrDir);
        $cfg->setProperty('instructorClassFiles', $path);
        if (-d $path)
        {
            $cfg->setProperty('instructorClassDir', $path);
        }
    }
}

# useAssertions
{
    my $useAssertions = $cfg->getProperty('useAssertions');
    if (defined $useAssertions && $useAssertions !~ m/^(true|yes|1|on)$/i)
    {
        $cfg->setProperty('enableAssertions', '-da');
    }
}

# checkstyleConfig
{
    my $checkstyle = $cfg->getProperty('checkstyleConfig');
    if (defined $checkstyle && $checkstyle ne "")
    {
        $cfg->setProperty(
            'checkstyleConfigFile', confirmExists($scriptData, $checkstyle));
    }
}

# pmdConfig
{
    my $pmd = $cfg->getProperty('pmdConfig');
    if (defined $pmd && $pmd ne "")
    {
        $cfg->setProperty(
            'pmdConfigFile', confirmExists($scriptData, $pmd));
    }
}

# policyFile
{
    my $policy = $cfg->getProperty('policyFile');
    if (defined $policy && $policy ne "")
    {
        $cfg->setProperty(
            'javaPolicyFile', confirmExists($scriptData, $policy));
    }
}

# security.manager
{
    if ($debug >= 5)
    {
        $cfg->setProperty(
            'security.manager',
            'java.security.manager=net.sf.webcat.plugins.javatddplugin.'
            . 'ProfilingSecurityManager');
    }
}

# markupProperties
{
    my $markup = $cfg->getProperty('markupProperties');
    if (defined $markup && $markup ne "")
    {
        $markupPropFile = confirmExists($scriptData, $markup);
    }
}


# wantPDF
{
    my $p = $cfg->getProperty('wantPDF');
    if (defined $p && $p !~ /false/i)
    {
        $cfg->setProperty('generatePDF', '1');
        $cfg->setProperty('PDF.dest', $pdfPrintout);
    }
}


# timeout
my $timeoutForOneRun = $cfg->getProperty('timeoutForOneRun', 30);
$cfg->setProperty('exec.timeout', $timeoutForOneRun * 1000);


$cfg->save();


#=============================================================================
# Script Startup
#=============================================================================
# Change to specified working directory and set up log directory
chdir($workingDir);

# try to deduce whether or not there is an extra level of subdirs
# around this assignment
{
    # Get a listing of all file/dir names, including those starting with
    # dot, then strip out . and ..
    my @dirContents = grep(!/^(\.{1,2}|META-INF)$/, <* .*>);

    # if this list contains only one entry that is a dir name != src, then
    # assume that the submission has been "wrapped" with an outter
    # dir that isn't actually part of the project structure.
    if ($#dirContents == 0 && -d $dirContents[0] && $dirContents[0] ne "src")
    {
        # Strip non-alphanumeric symbols from dir name
        my $dir = $dirContents[0];
        if ($dir =~ s/[^a-zA-Z0-9_]//g)
        {
            if ($dir eq "")
            {
                $dir = "dir";
            }
            rename($dirContents[0], $dir);
        }
        $workingDir .= "/$dir";
        chdir($workingDir);
    }
}

# Screen out any temporary files left around by BlueJ
{
    my @javaSrcs = < __SHELL*.java >;
    foreach my $tempFile (@javaSrcs)
    {
        unlink($tempFile);
    }
}


if ($debug)
{
    print "working dir set to $workingDir\n";
    print "JAVA_HOME = ", $ENV{JAVA_HOME}, "\n";
    print "ANT_HOME  = ", $ENV{ANT_HOME}, "\n";
    print "PATH      = ", $ENV{PATH}, "\n\n";
}

# localFiles
{
    my $localFiles = $cfg->getProperty('localFiles');
    if (defined $localFiles && $localFiles ne "")
    {
        my $lf = confirmExists($scriptData, $localFiles);
        print "localFiles = $lf\n" if $debug;
        if (-d $lf)
        {
            print "localFiles is a directory\n" if $debug;
            copyHere($lf, $lf, \@beautifierIgnoreFiles);
        }
        else
        {
            print "localFiles is a single file\n" if $debug;
            $lf =~ tr/\\/\//;
            my $base = $lf;
            $base =~ s,/[^/]*$,,;
            copyHere($lf, $base, \@beautifierIgnoreFiles);
        }
    }
}


#=============================================================================
# Run the ANT build file to get all the results
#=============================================================================
my $time1        = time;
#my $studentResults    = new Web_CAT::JUnitResultsReader();
#my $instructorResults = new Web_CAT::JUnitResultsReader();
#my $testsRun     = 0; #0
#my $testsFailed  = 0;
#my $testsErrored = 0;
#my $testsPassed  = 0;

if ($callAnt)
{
    if ($debug > 2) { $ANT .= " -d -v"; }
    my $cmdline = $Web_CAT::Utilities::SHELL
        . "$ANT -f \"$pluginHome/build.xml\" -l \"$antLog\" "
        . "-propertyfile \"$propfile\" \"-Dbasedir=$workingDir\" "
        . "2>&1 > " . File::Spec->devnull;

    print $cmdline, "\n" if ($debug);
    my ($exitcode, $timeout_status) = Proc::Background::timeout_system(
        $timeout - $postProcessingTime, $cmdline);
    if ($timeout_status)
    {
        $can_proceed = 0;
        $status{'antTimeout'} = 1;
        $buildFailed = 1;
        # FIXME: Move to end of $status{'feedback'} ...
        $status{'feedback'}->startFeedbackSection(
            "Errors During Testing", ++$expSectionId);
        $status{'feedback'}->print(<<EOF);
p><b class="warn">Testing your solution exceeded the allowable time
limit for this assignment.</b></p>
<p>Most frequently, this is the result of <b>infinite recursion</b>--when
a recursive method fails to stop calling itself--or <b>infinite
looping</b>--when a while loop or for loop fails to stop repeating.
</p>
<p>
As a result, no time remained for further analysis of your code.</p>
EOF
        $status{'feedback'}->endFeedbackSection;
    }
}

my $time2 = time;
if ($debug)
{
    print "\n", ($time2 - $time1), " seconds\n";
}
my $time3 = time;


#=============================================================================
# check for compiler error (or warnings)
#    report only the first file causing errors
#=============================================================================

#-----------------------------------------------
# Generate a script warning
sub adminLog
{
    open(SCRIPTLOG, ">>$scriptLog") ||
        die "Cannot open file for output '$scriptLog': $!";
    print SCRIPTLOG join("\n", @_), "\n";
    close(SCRIPTLOG);
}


#-----------------------------------------------
if ($can_proceed)
{
    open(ANTLOG, "$antLog") ||
        die "Cannot open file for input '$antLog': $!";
    $antLogOpen++;

    $_ = <ANTLOG>;
    scanTo(qr/^(syntax-check|BUILD FAILED)/);
    $buildFailed++ if defined($_)  &&  m/^BUILD FAILED/;
    $_ = <ANTLOG>;
    scanThrough(qr/^\s*\[(?!javac\])/);
    scanThrough(qr/^\s*($|\[javac\](?!\s+Compiling))/);
    if (!defined($_)  ||  $_ !~ m/^\s*\[javac\]\s+Compiling/)
    {
        # The student failed to include any source files!
        $status{'studentHasSrcs'} = 0;
        $status{'feedback'}->startFeedbackSection(
            "Compilation Produced Errors", ++$expSectionId);
        $status{'feedback'}->print(<<EOF);
<p>Your submission did not include any Java source files, so none
were compiled.
</p>
EOF
        $status{'feedback'}->endFeedbackSection;
        $can_proceed = 0;
    }
    else
    {
        $status{'studentHasSrcs'} = 1;
        $_ = <ANTLOG>;
        my $projdir  = $workingDir;
        $projdir .= "/" if ($projdir !~ m,/$,);
        $projdir = filePattern($projdir);
        my $compileMsgs    = "";
        my $compileErrs    = 0;
        my $firstFile      = "";
        my $collectingMsgs = 1;
        print "projdir = '$projdir'\n" if $debug;
        while (defined($_)  &&  s/^\s*\[javac\] //o)
        {
            # print "msg: $_";
            my $wrap = 0;
            if (s/^$projdir//io)
            {
                # print "trimmed: $_";
                if ($firstFile eq "" && m/^([^:]*):/o)
                {
                    $firstFile = $1;
                    $firstFile =~ s,\\,\\\\,g;
                    # print "firstFile='$firstFile'\n";
                }
                elsif ($_ !~ m/^$firstFile/)
                {
                    # print "stopping collection: $_";
                    $collectingMsgs = 0;
                }
                elsif ($_ =~ m/^$firstFile/ && !$collectingMsgs)
                {
                    # print "restarting collection: $_";
                    $collectingMsgs = 1;
                }
                chomp;
                $wrap = 1;
            }
            if (m/^[1-9][0-9]*\s.*error/o)
            {
                # print "err: $_";
                $compileErrs++;
                $collectingMsgs = 1;
                $can_proceed = 0;
                $buildFailed = 1;
            }
            if ($collectingMsgs)
            {
                $_ = htmlEscape($_);
                if ($wrap)
                {
                    $_ = "<b class=\"warn\">" . $_ . "</b>\n";
                }
                $compileMsgs .= $_;
            }
            $_ = <ANTLOG>;
        }
        if ($compileMsgs ne "")
        {
            $status{'feedback'}->startFeedbackSection(
                ($compileErrs)
                ? "Compilation Produced Errors"
                : "Compilation Produced Warnings",
                ++$expSectionId);
            $status{'feedback'}->print("<pre>\n");
            $status{'feedback'}->print($compileMsgs);
            $status{'feedback'}->print("</pre>\n");
            $status{'feedback'}->endFeedbackSection;
        }
    }


#=============================================================================
# collect JUnit testing stats from instructor-provided tests
#=============================================================================
    if ($can_proceed)
    {
        scanTo(qr/^(compile-instructor-tests:|BUILD FAILED)/);
        $buildFailed++ if defined($_)  &&  m/^BUILD FAILED/;
        $_ = <ANTLOG>;
        scanThrough(qr/^\s*($|\[javac\](?!\s+Compiling))/);
        if (!defined($_)  ||  $_ !~ m/^\s*\[javac\]\s+Compiling/)
        {
            adminLog("Failed to compile instructor test cases!\nCannot "
                      . "find \"[javac] Compiling <n> source files\" ... "
                      . "in line:\n$_");
        }
        else
        {
            $_ = <ANTLOG>;
        }
        my $instrHints     = "";
        my %instrHintCollection = ();
        my $collectingMsgs = 0;
        while (defined($_)  &&  s/^\s*\[javac\] //o)
        {
            # print "msg: $_\n";
            # print "tcp: $testCasePathPattern\n";
            if (/^$testCasePathPattern/o)
            {
                # print "    match\n";
                $collectingMsgs++;
                $_ =~ s/^\S*\s*//o;
            }
            elsif (/^location/o)
            {
                $_ = "";
            }
            if (m/^[1-9][0-9]*\s.*error/o)
            {
                # print "err: $_";
                $status{'compileErrs'}++;

            }
            if (m/^Compile failed;/o)
            {
                $collectingMsgs = 0;
            }
            if ($collectingMsgs)
            {
                $status{'compileMsgs'} .= htmlEscape($_);
            }
            $_ = <ANTLOG>;
        }

        scanTo(qr/^((instructor-)?test(.?):|BUILD FAILED)/);
        $buildFailed++ if defined($_)  &&  m/^BUILD FAILED/;
        if (m/^instructor-/)
        {
            # FIXME--anything to do here?
        }
    }

    $time3 = time;
    if ($debug)
    {
        print "\n", ($time3 - $time2), " seconds\n";
    }


#=============================================================================
# collect JUnit testing stats
#=============================================================================
    if ($can_proceed)
    {
        scanTo(qr/^(test:|BUILD FAILED)/);
        $buildFailed++ if defined($_)  &&  m/^BUILD FAILED/;
        # FIXME--anything to do here?
    }

    if ($can_proceed)
    {
        scanTo(qr/^BUILD FAILED/);
        if (defined($_)  &&  m/^BUILD FAILED/)
        {
            warn "ant BUILD FAILED unexpectedly.";
            $can_proceed = 0;
            $buildFailed++;
        }
    }

    $status{'studentTestResults'} =
        new Web_CAT::JUnitResultsReader("$resultDir/student.inc");
    $status{'instrTestResults'} =
        new Web_CAT::JUnitResultsReader("$resultDir/instr.inc");
}

if ($antLogOpen)
{
    close(ANTLOG);
}

my $time4 = time;
if ($debug)
{
    print "\n", ($time4 - $time3), " seconds\n";
}


#=============================================================================
# Load checkstyle and PMD reports into internal data structures
#=============================================================================

# The configuration file for scoring tool messages
my $ruleProps = Config::Properties::Simple->new(file => $markupPropFile);

# The message groups defined by the instructor
my @groups = split(qr/,\s*/, $ruleProps->getProperty("groups", ""));

# The same list, but as a hash, initialized by this for loop
my %groups = ();
foreach my $group (@groups)
{
    $groups{$group} = 1;
}

# We'll co-opt the XML::Smart structure to record the following
# info about messages:
# messageStats->group->rule->filename->{num, pts, collapse}
# messageStats->group->rule->{num, pts, collapse}
# messageStats->group->{num, pts, collapse}
# messageStats->group->file->filename->{num, pts, collapse}
# messageStats->{num, pts, collapse}
# messageStats->file->filename->{num, pts, collapse}
my $messageStats = XML::Smart->new();

# A hash of arrays of violation objects, keyed by file name (relative
# to $workingDir, using forward slashes).  Each violation object is
# a reference to an XML::Smart node:
# ... was a hash like this, but now ...
# {
#     group         => ...
#     category      => ...
#     message       => ...
#     deduction     => ...
#     limitExceeded => ...
#     lineNo        => ...
#     URL           => ...
#     source        => ...
# }
# Both the "to" and "fileName" fields are omitted, since "to" is
# always "all" and the fileName is the key mapping to (a list of)
# these.
my %messages = ();

#-----------------------------------------------
# ruleSetting(rule, prop [, default])
#
# Retrieves a rule parameter from the config file, tracing through the
# default hierarchy if necessary.  Parameters:
#
#     rule:    the string name of the rule to look for
#     prop:    the name of the setting to look up
#     default: value to use if no setting is recorded in the configuration
#              file (or undef, if omitted)
#
#  The search order is as follows:
#
#     <rule>.<prop>              = value
#     <group>.ruleDefault.<prop> = value
#     ruleDefault.<prop>         = value
#     <default> (if provided)
#
# Here, <group> is the group name for the given rule, as determined
# by <rule>.group (or ruleDefault.group).
#
sub ruleSetting
{
    croak "usage: ruleSetting(rule, prop [, default])"
        if ($#_ < 1 || $#_ > 2);
    my $rule    = shift;
    my $prop    = shift;
    my $default = shift;

    my $val = $ruleProps->getProperty("$rule.$prop");
    if (!defined($val))
    {
        my $group = $ruleProps->getProperty("$rule.group");
        if (!defined($group))
        {
            $group = $ruleProps->getProperty("ruleDefault.group");
        }
        if (defined($group))
        {
            if (!defined($groups{$group}))
            {
                warn "group name '$group' not in groups property.\n";
            }
            $val = $ruleProps->getProperty("$group.ruleDefault.$prop");
        }
        if (!defined($val))
        {
            $val = $ruleProps->getProperty("ruleDefault.$prop");
        }
        if (!defined($val))
        {
            $val = $default;
        }
    }
    if (defined($val) && $val eq '${maxDeduction}')
    {
        $val = $maxRuleDeduction;
    }
    return $val;
}


#-----------------------------------------------
# groupSetting(group, prop [, default])
#
# Retrieves a group parameter from the config file, tracing through the
# default hierarchy if necessary.  Parameters:
#
#     group:   the string name of the group to look for
#     prop:    the name of the setting to look up
#     default: value to use if no setting is recorded in the configuration
#              file (or undef, if omitted)
#
# The search order is as follows:
#
#     <group>.group.<prop> = value
#     groupDefault.<prop>  = value
#     <default> (if provided)
#
sub groupSetting
{
    croak "usage: groupSetting(group, prop [, default])"
        if ($#_ < 1 || $#_ > 2);
    my $group   = shift;
    my $prop    = shift;
    my $default = shift;

    if (!defined($groups{$group}))
    {
        carp "group name '$group' not in groups property.\n";
    }
    my $val = $ruleProps->getProperty("$group.group.$prop");
    if (!defined($val))
    {
        if (!defined($val))
        {
            $val = $ruleProps->getProperty("groupDefault.$prop");
        }
        if (!defined($val))
        {
            $val = $default;
        }
    }
    if (defined($val) && $val eq '${maxDeduction}')
    {
        $val = $maxRuleDeduction;
    }
    return $val;
}


#-----------------------------------------------
# markupSetting(prop [, default])
#
# Retrieves a top-level parameter from the config file.
sub markupSetting
{
    croak "usage: markupSetting(prop [, default])"
        if ($#_ < 0 || $#_ > 1);
    my $prop    = shift;
    my $default = shift;

    my $val = $ruleProps->getProperty($prop, $default);
    if (defined($val) && $val eq '${maxDeduction}')
    {
        $val = $maxRuleDeduction;
    }
    return $val;
}


#-----------------------------------------------
# countRemarks(listRef)
#
# Counts the number of non-killed remarks in %messages for the
# given file name.
#
sub countRemarks
{
    my $list  = shift;
    my $count = 0;
    foreach my $v (@{ $list })
    {
        if ($v->{kill}->null)
        {
            $count++;
        }
    }
    return $count;
}


#-----------------------------------------------
# trackMessageInstanceInContext(
#       context             => ...,
#     [ maxBeforeCollapsing => ..., ]
#       maxDeductions       => ...,
#       deduction           => ref ...,
#       overLimit           => ref ...,
#       fileName            => ...,
#       violation           => ... )
#
sub trackMessageInstanceInContext
{
    my %args = @_;
    my $context = $args{context};

    if (!($context->{num}->null))
    {
        $context->{num} += 1;
    }
    else
    {
        $context->{num}      = 1;
        $context->{pts}      = 0;
        $context->{collapse} = 0;
    }
    if (defined($args{maxBeforeCollapsing}) &&
         $context->{num}->content > $args{maxBeforeCollapsing})
    {
        $context->{collapse} = 1;
    }
    # check for pts in file overflow
    if ($context->{pts}->content + ${ $args{deduction} } >
         $args{maxDeductions})
    {
        ${ $args{overLimit} }++;
        ${ $args{deduction} } =
            $args{maxDeductions} - $context->{pts}->content;
        if (${ $args{deduction} } < 0)
        {
            carp "deduction underflow, file ", $args{fileName}, ":\n",
                $args{violation}->data_pointer(noheader  => 1, nometagen => 1);
        }
    }
}


#-----------------------------------------------
# trackMessageInstance(rule, fileName, violation)
#
# Updates the $messageStats structure with the information for a given
# rule violation.
#
#     rule:      the name of the rule violated
#     fileName:  the source file name where the violation occurred
#                (relative to $workingDir)
#     violation: the XML::Smart structure referring to the violation
#                (used for error message printing only)
#
sub trackMessageInstance
{
    croak "usage: recordPMDMessageStats(rule, fileName, violation)"
        if ($#_ != 2);
    my $rule      = shift;
    my $fileName  = shift;
    my $violation = shift;

    my $group     = ruleSetting($rule, 'group', 'defaultGroup');
    my $deduction = ruleSetting($rule, 'deduction', 0)
        * $toolDeductionScaleFactor;
    my $overLimit = 0;

    if ($debug > 1)
    {
        print "tracking $group, $rule, $fileName, ",
        $violation->{line}->content, "\n";
    }
    if ($group eq "testing")
    {
        $hasJUnitErrors++;
        if ($debug > 1)
        {
            print "found JUnit error!\n";
        }
    }

    # messageStats->group->rule->filename->{num, collapse} (pts later)
    trackMessageInstanceInContext(
            context           => $messageStats->{$group}->{$rule}->{$fileName},
            maxBeforeCollapsing => ruleSetting($rule, 'maxBeforeCollapsing',
                                               $defaultMaxBeforeCollapsing),
            maxDeductions       => ruleSetting($rule, 'maxDeductionsInFile',
                                               $maxRuleDeduction),
            deduction           => \$deduction,
            overLimit           => \$overLimit,
            fileName            => $fileName,
            violation           => $violation
       );

    # messageStats->group->rule->{num, collapse} (pts later)
    trackMessageInstanceInContext(
            context       => $messageStats->{$group}->{$rule},
            maxDeductions => ruleSetting($rule, 'maxDeductionsInAssignment',
                                         $maxToolScore),
            deduction     => \$deduction,
            overLimit     => \$overLimit,
            fileName      => $fileName,
            violation     => $violation
       );

    # messageStats->group->file->filename->{num, collapse} (pts later)
    trackMessageInstanceInContext(
            context       => $messageStats->{$group}->{file}->{$fileName},
            maxBeforeCollapsing => groupSetting($group, 'maxBeforeCollapsing',
                                                $defaultMaxBeforeCollapsing),
            maxDeductions => groupSetting($group, 'maxDeductionsInFile',
                                          $maxToolScore),
            deduction     => \$deduction,
            overLimit     => \$overLimit,
            fileName      => $fileName,
            violation     => $violation
       );

    # messageStats->group->{num, collapse} (pts later)
    trackMessageInstanceInContext(
            context       => $messageStats->{$group},
            maxDeductions => groupSetting($group, 'maxDeductionsInAssignment',
                                          $maxToolScore),
            deduction     => \$deduction,
            overLimit     => \$overLimit,
            fileName      => $fileName,
            violation     => $violation
       );

    # messageStats->file->filename->{num, collapse} (pts later)
    trackMessageInstanceInContext(
            context       => $messageStats->{file}->{$fileName},
            maxBeforeCollapsing =>
                markupSetting('maxBeforeCollapsing', 100000),
            maxDeductions =>
                markupSetting('maxDeductionsInAssignment', $maxToolScore),
            deduction     => \$deduction,
            overLimit     => \$overLimit,
            fileName      => $fileName,
            violation     => $violation
       );

    # messageStats->{num, collapse} (pts later)
    trackMessageInstanceInContext(
            context       => $messageStats,
            maxDeductions =>
                markupSetting('maxDeductionsInAssignment', $maxToolScore),
            deduction     => \$deduction,
            overLimit     => \$overLimit,
            fileName      => $fileName,
            violation     => $violation
       );

    # Recover overLimit in messageStats for collapsed rules
    if ($overLimit &&
        $messageStats->{$group}->{$rule}->{$fileName}->{collapse}->content)
    {
        $messageStats->{$group}->{$rule}->{$fileName}->{overLimit} = 1;
    }

    # Pts update in all locations:
    # ----------------------------
    #     messageStats->group->rule->filename->{pts}
    $messageStats->{$group}->{$rule}->{$fileName}->{pts} += $deduction;

    #     messageStats->group->rule->{pts}
    $messageStats->{$group}->{$rule}->{pts} += $deduction;

    #     messageStats->group->file->filename->{pts}
    $messageStats->{$group}->{file}->{$fileName}->{pts} += $deduction;

    #     messageStats->group->{pts}
    $messageStats->{$group}->{pts} += $deduction;

    #     messageStats->file->filename->{pts}
    $messageStats->{file}->{$fileName}->{pts} += $deduction;

    #     messageStats->{pts}
    $messageStats->{pts} += $deduction;

    # print "before: ", $violation->data_pointer(noheader  => 1,
    #                                           nometagen => 1);
    $violation->{deduction} = $deduction;
    $violation->{overLimit} = $overLimit;
    $violation->{group}     = $group;
    $violation->{category}  = ruleSetting($rule, 'category');
    $violation->{url}       = ruleSetting($rule, 'URL'     );
    if (!defined($messages{$fileName}))
    {
        $messages{$fileName} = [ $violation ];
    }
    else
    {
        push(@{ $messages{$fileName} }, $violation);
    }
    # print "after: ", $violation->data_pointer(noheader  => 1,
    #                                          nometagen => 1);
    # print "messages for '$fileName' =\n\t",
    #     join("\n\t", @{ $messages{$fileName} }), "\n";
}


#-----------------------------------------------
# Some testing code left in place (but disabled  by the if test)
#
if (0)    # For testing purposes only
{
    # Some tests for properties
    # -------
    print "ShortVariable.group                     = ",
        ruleSetting('ShortVariable', 'group', 'zzz'), "\n";
    print "ShortVariable.deduction                 = ",
        ruleSetting('ShortVariable', 'deduction', 'zzz'), "\n";
    print "ShortVariable.category                  = ",
        ruleSetting('ShortVariable', 'category', 'zzz'), "\n";
    print "ShortVariable.maxBeforeCollapsing       = ",
        ruleSetting('ShortVariable', 'maxBeforeCollapsing', 'zzz'), "\n";
    print "ShortVariable.maxDeductionsInFile       = ",
        ruleSetting('ShortVariable', 'maxDeductionsInFile', 'zzz'), "\n";
    print "ShortVariable.maxDeductionsInAssignment = ",
      ruleSetting('ShortVariable', 'maxDeductionsInAssignment', 'zzz'), "\n";
    print "ShortVariable.URL                       = ",
        ruleSetting('ShortVariable', 'URL', 'zzz'), "\n";

    print "\n";
    print "naming.maxDeductionsInFile = ",
        groupSetting('naming', 'maxDeductionsInFile', 'zzz'), "\n";
    print "naming.maxDeductionsInAssignment = ",
        groupSetting('naming', 'maxDeductionsInAssignment', 'zzz'), "\n";
    print "naming.fooBar = ",
        groupSetting('naming', 'fooBar', 'zzz'), "\n";


    # Some tests for the messageStats structure
    # -------
    $messageStats->{naming}->{ShortVariable}->{num} = 1;
    $messageStats->{naming}->{ShortVariable}->{pts} = -1;
    $messageStats->{naming}->{ShortVariable}->{collapse} = 0;
    $messageStats->{documentation}->{JavaDocMethod}->{num} = 1;
    $messageStats->{documentation}->{JavaDocMethod}->{pts} = -1;
    $messageStats->{documentation}->{JavaDocMethod}->{collapse} = 0;
    print $messageStats->data(noheader  => 1, nometagen => 1);
    exit(0);
}


#-----------------------------------------------
# A useful subroutine for processing the ant log
if (!$buildFailed) # $can_proceed)
{
    my $checkstyleLog = "$resultDir/checkstyle_report.xml";
    if (-f $checkstyleLog)
    {
        my $cstyle = XML::Smart->new($checkstyleLog);
        foreach my $file (@{ $cstyle->{checkstyle}->{file} })
        {
            my $fileName = $file->{name}->content;
            $fileName =~ s,\\,/,go;
            $fileName =~ s,^\Q$workingDir/\E,,i;
            if (exists $file->{error})
            {
                foreach my $violation (@{ $file->{error} })
                {
                    my $rule = $violation->{source}->content;
                    $rule =~
                        s/^com\.puppycrawl\.tools\.checkstyle\.checks\.//o;
                    $rule =~ s/Check$//o;
                    $violation->{rule} = $rule;
                    delete $violation->{source};
                    trackMessageInstance(
                        $violation->{rule}->content, $fileName, $violation);
                }
            }
        }
    }

    my $pmdLog = "$resultDir/pmd_report.xml";
    if (-f $pmdLog)
    {
        my $pmd = XML::Smart->new($pmdLog);
        foreach my $file (@{ $pmd->{pmd}->{file} })
        {
            my $fileName = $file->{name}->content;
            $fileName =~ s,\\,/,go;
            $fileName =~ s,^\Q$workingDir/\E,,i;
            if (exists $file->{violation})
            {
                foreach my $violation (@{ $file->{violation} })
                {
                    trackMessageInstance(
                        $violation->{rule}->content, $fileName, $violation);
                }
            }
        }
    }

    if ($debug > 1)
    {
        my $msg = $messageStats->data(noheader  => 1, nometagen => 1);
        if (defined $msg)
        {
            print $msg;
        }
    }
    foreach my $f (keys %messages)
    {
        print "$f:\n" if ($debug > 1);
        foreach my $v (@{ $messages{$f} })
        {
            if ($debug > 1)
            {
                print "\t", $v->{line}, ": -", $v->{deduction}, ": ",
                    $v->{rule}, " ol=", $v->{overLimit},
                    " kill=", $v->{kill}, "\n";
            }
            if ($messageStats->{ $v->{group} }->{ $v->{rule} }->{$f}
                ->{collapse}->content > 0)
            {
                if ($debug > 1)
                {
                    print "$f(", $v->{line}, "): -", $v->{deduction}, ": ",
                        $v->{rule}, ", collapsing\n";
                }
                if ($messageStats->{ $v->{group} }->{ $v->{rule} }->{$f}
                    ->{kill}->null())
                {
                    $v->{line} = 0;
                    if (!$v->{overLimit}->content &&
                        !$messageStats->{ $v->{group} }->{ $v->{rule} }->{$f}
                            ->{overLimit}->null)
                    {
                        $v->{overLimit} = 1;
                    }
                    $v->{deduction} =
                    $messageStats->{ $v->{group} }->{ $v->{rule} }->{$f}
                        ->{pts}->content;
                    $messageStats->{ $v->{group} }->{ $v->{rule} }->{$f}
                        ->{kill} = 1;
                }
                else
                {
                    $v->{kill} = 1;
                }
            }
        }
    }
    $status{'toolDeductions'} = $messageStats->{pts}->content;
}
else
{
    $status{'toolDeductions'} = $maxToolScore;
}

# If no files were submitted at all, then no credit for static
# analysis
if (!$status{'studentHasSrcs'})
{
    $status{'toolDeductions'} = $maxToolScore;
}


#=============================================================================
# translate html
#=============================================================================
my %cloveredClasses    = ();
my %classToFileNameMap = ();
my %classToMarkupNoMap = ();
my %fileToMarkupNoMap  = ();

#---------------------------------------------------------------------------
# Translate one HTML file from clover markup to what Web-CAT expects
sub translateHTMLFile
{
    my $file = shift;
    my $stripEmptyCoverage = shift;
    my $cloverData = shift;
    # print "translating $file\n";

    # Record class name
    my $className = $file;
    $className =~ s/\.html$//o;
    $className =~ s,^$resultDir/clover/(default-pkg/)?,,o;
    my $sourceName = $className . ".java";
    $className =~ s,/,.,go;
    if (defined($classToFileNameMap{$className}))
    {
        $sourceName = $classToFileNameMap{$className};
    }
    # print "class name = $className\n";
    $cloveredClasses{$className} = 1;

    my @comments = ();
    if (defined $messages{$sourceName})
    {
        @comments = sort { $b->{line}->content  <=>  $a->{line}->content }
            @{ $messages{$sourceName} };
    }
    $messageStats->{file}->{$sourceName}->{remarks} = countRemarks(\@comments);
#    if (defined($classToMarkupNoMap{$className}))
#    {
#        $cfg->setProperty('codeMarkup' . $classToMarkupNoMap{$className}
#                           . '.remarks',
#                $messageStats->{file}->{$sourceName}->{remarks}->content);
#    }
#    else
#    {
#       my $lcClassName = $className;
#       $lcClassName =~ tr/A-Z/a-z/;
#        if (defined($classToMarkupNoMap{$lcClassName}))
#        {
#            $cfg->setProperty('codeMarkup' . $classToMarkupNoMap{$lcClassName}
#                               . '.remarks',
#                    $messageStats->{file}->{$sourceName}->{remarks}->content);
#        }
#        else
#        {
#            print(STDERR "Cannot locate code markup number for $className "
#               . "in $sourceName\n");
#        }
#    }
    if ($debug > 1)
    {
        print "$sourceName: ", $#comments + 1, "\n";
        foreach my $c (@comments)
        {
            print "\t", $c->{group}, " '", $c->{line}, "'\n";
        }
    }

    open(HTML, $file) || die "Cannot open file for input '$file': $!";
    my @html = <HTML>;  # Slurp in the whole file
    close(HTML);
    my $allHtml = join("", @html);

    # Look for @author tags
    my @partnerExcludePatterns = ();
    my $partnerExcludePatterns_raw =
        $cfg->getProperty('grader.partnerExcludePatterns', "");
    if ($partnerExcludePatterns_raw ne "")
    {
        @partnerExcludePatterns =
            split(/(?<!\\),/, $partnerExcludePatterns_raw);
    }
    my $userName = $cfg->getProperty('userName', "");
    if ($userName ne "")
    {
        push(@partnerExcludePatterns, $userName);
    }
    my $potentialPartners = $cfg->getProperty('grader.potentialpartners', "");
    while ($allHtml =~
      m/<span[^<>]*class="javadoc"[^<>]*>\@author<\/span>\s*([^<>]*)<\/span>/g)
    {
        my $authors = $1;
        $authors =~ s/\@[a-zA-Z][a-zA-Z0-9\.]+[a-zA-Z]/ /g;
        $authors =~
        s/your-pid [\(]?and if in lab[,]? partner[']?s pid on same line[\)]?//;
        $authors =~ s/Partner [1-9][' ]?s name [\(]?pid[\)]?//;
        $authors =~ s/[,;:\(\)\]\]\{\}=!\@#%^&\*<>\/\\\`'"]/ /g;
        foreach my $pat (@partnerExcludePatterns)
        {
            $authors =~ s/(?<!\S)$pat(?!\S)//g;
        }
        $authors =~ s/^\s+//;
        $authors =~ s/\s+$//;
        $authors =~ s/\s\s+/ /g;
        if ($authors ne "")
        {
            if ($potentialPartners ne "")
            {
                $potentialPartners .= " ";
            }
            $potentialPartners .= $authors;
        }
    }
    $cfg->setProperty('grader.potentialpartners', $potentialPartners);

    # count the number of assertions that were not fully covered, in order
    # to remove them from the coverage stats
    my $preCount = $allHtml;
    my $conditionCount = ($preCount =~ s|(<tr>
        <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage(CountHilight">\s*)
        <a[^<>]*>([^<>]*)</a>
        (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
        Hilight(">\s*)<a[^<>]*>
        (\s*<span\s+class="keyword">assert</span>([^<>]\|<(/?)span[^<>]*>)*)
        \s*</a>
        |$1"line$2$3$4$5$6|ixsg);

    # Now, "unhighlight" all those that were only executed true (leave those
    # That were never executed at all marked, even though they won't be
    # counted against the student)
    my $executedConditionCount = ($allHtml =~ s|(<tr>
        <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage(CountHilight">\s*)
        <a[^<>]*>([^<>]*)</a>
        (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
        Hilight(">\s*)<a[^<>]*title="[^<>]*true\s[1-9][0-9]*\stime(s?),
        \sfalse\s0\stimes[^<>]*"[^<>]*>
        (\s*<span\s+class="keyword">assert</span>([^<>]\|<(/?)span[^<>]*>)*)
        \s*</a>
        |$1"line$2$3$4$5$7|ixsg);

    # Now, "unhighlight" all fail() method calls in test cases that weren't
    # executed.
    my $unexecutedFailCount = ($allHtml =~ s|(<tr>
        <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage(CountHilight">\s*)
        <a[^<>]*>([^<>]*)</a>
        (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
        Hilight(">\s*)<a[^<>]*title="[^<>]*statement\snot\sexecuted[^<>]*"[^<>]*>
        (\s*fail\s*\((<span [^<>]*>[^<>]*</span>)?\)\s*;)
        \s*</a>
        |$1"line$2$3$4$5$6|ixsg);

    # Now, "unhighlight" all the preventative null checks that were only
    # executed true
    my $executedNullCheckCount = ($allHtml =~ s|(<tr>
        <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage(CountHilight">\s*)
        <a[^<>]*>([^<>]*)</a>
        (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
        Hilight(">\s*)<a[^<>]*title="[^<>]*true\s[1-9][0-9]*\stime(s?),
        \sfalse\s0\stimes[^<>]*"[^<>]*>
        (((?!</a>)[^\?])*
        ([a-zA-Z_][a-zA-Z0-9_\.]*)\s*!=\s*
        <span\sclass="keyword">null</span>\s*\?\s*\g{-1}[a-zA-Z0-9_\.]*
        \s*:\s*<span\sclass="keyword">null</span>
        ((?!</a>)[^\?])*)</a>
        |$1"line$2$3$4$5$7|ixsg);

    # Now, handle simple exception handlers, if needed.
    my $simpleCatchBlocks = 0;
    if (!$requireSimpleExceptionCoverage)
    {
        $simpleCatchBlocks = ($allHtml =~ s|(<tr>
            ((?!</tr>).)*<span\sclass="keyword">catch</span>((?!</tr>).)*
            (</tr>\s*<tr>((?!</tr>).)*){((?!</tr>).)*</tr>\s*
            (<tr>((?!</tr>).)*<td\sclass="srcCell">\s*
                <span\s+class="srcLine">\s*
                    (<span\s+class="comment">((?!</span>).)*</span>\s*)?
                </span>\s*</td>\s*</tr>\s*)*
            <tr>\s*
            <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*statement\snot\sexecuted
            [^<>]*"[^<>]*>
            (\s*([a-zA-Z_][a-zA-Z0-9_]*\s*.\s*printStackTrace\s*\([^<>()]*\)\|
            <span\sclass="keyword">throw</span>\s+
            <span\sclass="keyword">new</span>\s+
            [A-Z][a-zA-Z0-9_]*\s*\([^<>()]*\))\s*;)
            \s*</a>
            |$1"line$11$12$13$14$15|ixsg);
    }

    my $simpleGetters = 0;
    my $simpleSetters = 0;
    if (!$requireSimpleGetterSetterCoverage)
    {
        # First, handle 3-line getters
        $simpleGetters = ($allHtml =~ s|(<tr>((?!</tr>).)*
            <td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*method\snot\sentered
            [^<>]*"[^<>]*>
            (\s*<span\sclass="keyword">public</span>\s+
            (<span\sclass="keyword">[a-zA-Z]+</span>\|[A-Za-z][a-zA-Z0-9_]*)
            (?:\s*<[^<>]*>)?(?:\s*\[\s*\])*\s+
            (get[A-Z][a-zA-Z0-9_]*)\s*\(\s*\))(?:\s*</a>
            (((?!</tr>).)*</tr>\s*<tr>((?!</tr>).)*{)\|(\s*{)\s*</a>)
            (((?!</tr>).)*</tr>\s*<tr>\s*
            <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*statement\snot\sexecuted
            [^<>]*"[^<>]*>
            (\s*<span\sclass="keyword">return</span>\s+
            (?:[A-Za-z_][A-Za-z0-9_\.]+\|
            <span\sclass="string">"[^"]*"</span>\|
            <span\sclass="keyword">new</span>\s+Parser\s*\[\]\s*{}
            );)\s*</a>
            ((((?!</tr>).)*</tr>\s*(<tr>((?!</tr>).)*))?
            })|$1"line$3$4$5$6$7$10$13$14"line$16$17$18$19$20$21|ixsg);

        # Now 1-line getters
        $simpleGetters += ($allHtml =~ s|(<tr>((?!</tr>).)*
            <td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*method\snot\sentered
            [^<>]*"[^<>]*>
            (\s*<span\sclass="keyword">public</span>\s+
            (<span\sclass="keyword">[a-zA-Z]+</span>\|[A-Za-z][a-zA-Z0-9_]*)
            (?:\s*<[^<>]*>)?(?:\s*\[\s*\])*\s+
            (get[A-Z][a-zA-Z0-9_]*)\s*\(\s*\)\s*{
            \s*<span\sclass="keyword">return</span>\s+
            (?:[A-Za-z_][A-Za-z0-9_\.]+\|
            <span\sclass="string">"[^"]*"</span>);\s*})\s*</a>
            |$1"line$3$4$5$6$7|ixsg);

        # Now 3-line setters
        $simpleSetters = ($allHtml =~ s|(<tr>((?!</tr>).)*
            <td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*method\snot\sentered
            [^<>]*"[^<>]*>
            (\s*<span\sclass="keyword">public</span>\s+
            <span\sclass="keyword">void</span>\s+
            (set[A-Z][a-zA-Z0-9_]*)\s*\(\s*
            (<span\sclass="keyword">[a-zA-Z]+</span>\|[A-Za-z][a-zA-Z0-9_]*)
            (?:\s*<[^<>]*>)?(?:\s*\[\s*\])*\s+
            [a-zA-Z_][a-zA-Z0-9_]*\s*\))
            (?:\s*</a>
            (((?!</tr>).)*</tr>\s*<tr>((?!</tr>).)*{)\|(\s*{)\s*</a>)
            (((?!</tr>).)*</tr>\s*<tr>\s*
            <td[^<>]*>[^<>]*</td>\s*<td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*statement\snot\sexecuted
            [^<>]*"[^<>]*>
            (\s*[A-Za-z_][A-Za-z0-9_\.]+\s*=\s*
            [A-Za-z_][A-Za-z0-9_]+;)\s*</a>
            ((((?!</tr>).)*</tr>\s*(<tr>((?!</tr>).)*))?
            })
            |$1"line$3$4$5$6$7$10$13$14"line$16$17$18$19$20$21|ixsg);

        # Now 1-line setters
        $simpleSetters += ($allHtml =~ s|(<tr>((?!</tr>).)*
            <td[^<>]*\s+class=)"coverage
            (CountHilight">\s*)
            <a[^<>]*>([^<>]*)</a>
            (\s*</td>\s*<td[^<>]*>\s*<span\s+class="srcLine)
            Hilight(">\s*)<a[^<>]*title="[^<>]*method\snot\sentered
            [^<>]*"[^<>]*>
            (\s*<span\sclass="keyword">public</span>\s+
            <span\sclass="keyword">void</span>\s+
            (set[A-Z][a-zA-Z0-9_]*)\s*\(\s*
            (<span\sclass="keyword">[a-zA-Z]+</span>\|[A-Za-z][a-zA-Z0-9_]*)
            (?:\s*<[^<>]*>)?(?:\s*\[\s*\])*\s+
            [a-zA-Z_][a-zA-Z0-9_]*\s*\)\s*{
            \s*[A-Za-z_][A-Za-z0-9_\.]+\s*=\s*
            [A-Za-z_][A-Za-z0-9_]+;\s*})\s*</a>
            |$1"line$3$4$5$6$7|ixsg);
    }

    if ($debug)
    {
        print "\tFound $conditionCount uncovered assertions, with ",
            "$executedConditionCount partially executed.\n";
        print "\tFound $unexecutedFailCount unexecuted fail() statements.\n";
        print "\tFound $simpleCatchBlocks simple catch blocks.\n";
        print "\tFound $simpleGetters simple getters.\n";
        print "\tFound $simpleSetters simple setters.\n";
    }

    if ($conditionCount || $unexecutedFailCount || $simpleCatchBlocks
        || $simpleGetters || $simpleSetters || $executedNullCheckCount)
    {
        if ($debug)
        {
            print $cloverData->data, "\n";
        }
        $cloverData->{coverage}{project}{metrics}{conditionals} -=
            2 * $conditionCount;
        $cloverData->{coverage}{project}{metrics}{coveredconditionals} -=
            $executedConditionCount;
        $cloverData->{coverage}{project}{metrics}{elements} -=
            2 * $conditionCount;
        $cloverData->{coverage}{project}{metrics}{coveredelements} -=
            $executedConditionCount;

        $cloverData->{coverage}{project}{metrics}{conditionals} -=
            2 * $executedNullCheckCount;
        $cloverData->{coverage}{project}{metrics}{coveredconditionals} -=
            $executedNullCheckCount;
        $cloverData->{coverage}{project}{metrics}{elements} -=
            2 * $executedNullCheckCount;
        $cloverData->{coverage}{project}{metrics}{coveredelements} -=
            $executedNullCheckCount;

        $cloverData->{coverage}{project}{metrics}{elements} -=
            $unexecutedFailCount;
        $cloverData->{coverage}{project}{metrics}{statements} -=
            $unexecutedFailCount;

        $cloverData->{coverage}{project}{metrics}{elements} -=
            $simpleCatchBlocks;
        $cloverData->{coverage}{project}{metrics}{statements} -=
            $simpleCatchBlocks;

        $cloverData->{coverage}{project}{metrics}{elements} -=
            2 * ($simpleGetters + $simpleSetters);
        $cloverData->{coverage}{project}{metrics}{methods} -=
            $simpleGetters + $simpleSetters;
        $cloverData->{coverage}{project}{metrics}{statements} -=
            $simpleGetters + $simpleSetters;

        foreach my $pkg (@{ $cloverData->{coverage}{project}{package} })
        {
            foreach my $file (@{ $pkg->{file} })
            {
                my $fileName = $file->{name}->content;
                if ($debug)
                {
                    print "    clover patch: checking $fileName against ",
                        "$sourceName\n";
                }
                $fileName =~ s,\\,/,go;
                my $Uprojdir = $workingDir . "/";
                $fileName =~ s/^\Q$Uprojdir\E//io;
                print "    ... pruned file name = $fileName\n" if ($debug);
                if ($fileName eq $sourceName)
                {
                    print "    ... clover element found!\n" if ($debug);
                    $file->{metrics}{conditionals} -= 2 * $conditionCount;
                    $file->{metrics}{coveredconditionals} -=
                        $executedConditionCount;
                    $file->{metrics}{elements} -= 2 * $conditionCount;
                    $file->{metrics}{coveredelements} -=
                        $executedConditionCount;

                    $file->{metrics}{elements} -= $unexecutedFailCount;
                    $file->{metrics}{statements} -= $unexecutedFailCount;

                    $file->{metrics}{elements} -= $simpleCatchBlocks;
                    $file->{metrics}{statements} -= $simpleCatchBlocks;

                    $file->{metrics}{elements} -=
                        2 * ($simpleGetters + $simpleSetters);
                    $file->{metrics}{methods} -=
                        $simpleGetters + $simpleSetters;
                    $file->{metrics}{statements} -=
                        $simpleGetters + $simpleSetters;
                }
            }
        }
        if ($debug)
        {
            print "\nafter correction:\n";
            print $cloverData->data;
            print "\n";
        }
    }

    my $reformatter = new Web_CAT::Clover::Reformatter(
        \@comments, $stripEmptyCoverage, $allHtml);
    $reformatter->save($file);
}


#---------------------------------------------------------------------------
# Walk a dir, deleting unneeded clover-generated files and translating others
sub processCloverDir
{
    my $path = shift;
    my $stripEmptyCoverage = shift;
    my $cloverData = shift;

    # print "processing $path, strip = $stripEmptyCoverage\n";

    if (-d $path)
    {
        for my $file (<$path/*>)
        {
            processCloverDir($file, $stripEmptyCoverage, $cloverData);
        }

        # is the dir empty now?
        my @files = (<$path/*>);
        if ($#files < 0)
        {
            # print "deleting empty dir $path\n";
            if (!rmdir($path))
            {
                adminLog("cannot delete empty directory '$path': $!");
            }
        }
    }
    elsif ($path !~ m/\.html$/io || $path =~
    m/(^|\/)(all-classes|all-pkgs|index|pkg-classes|pkg(s?)-summary)\.html$/io)
    {
        # print "deleting $path\n";
        if (unlink($path) != 1)
        {
            adminLog("cannot delete file '$path': $!");
        }
    }
    else
    {
        # An HTML file to keep!
        translateHTMLFile($path, $stripEmptyCoverage, $cloverData);
    }
}


my $time5 = time;
if ($debug)
{
    print "\n", ($time5 - $time4), " seconds\n";
}


#=============================================================================
# convert clover.xml to properties (and record html files)
#=============================================================================
my $gradedElements        = 0;
my $gradedElementsCovered = 0;
my $runtimeScoreWithoutCoverage = 0;
if (defined $status{'instrTestResults'} && $status{'studentHasSrcs'})
{
    if ($status{'compileErrs'})
    {
        # If there was a compilation error, don't count any instructor
        # tests as passed to force an "unknown" result
        $status{'instrTestResults'}->addTestsExecuted(
            -$status{'instrTestResults'}->testsExecuted);
    }
    $runtimeScoreWithoutCoverage =
        $maxCorrectnessScore * $status{'instrTestResults'}->testPassRate;
}

print "score with ref tests: $runtimeScoreWithoutCoverage\n" if ($debug > 2);

if (defined $status{'studentTestResults'}
    && $status{'studentTestResults'}->testsExecuted > 0)
{
    if ($studentsMustSubmitTests)
    {
        if ($allStudentTestsMustPass
            && $status{'studentTestResults'}->testsFailed > 0)
        {
            $runtimeScoreWithoutCoverage = 0;
        }
        else
        {
            $runtimeScoreWithoutCoverage *=
                $status{'studentTestResults'}->testPassRate;
        }
    }
    $studentCasesPercent =
        int($status{'studentTestResults'}->testPassRate * 100.0 + 0.5);
    if ($status{'studentTestResults'}->testsFailed > 0
        && $studentCasesPercent == 100)
    {
        # Don't show 100% if some cases failed
        $studentCasesPercent--;
    }
}
elsif ($studentsMustSubmitTests)
{
    $runtimeScoreWithoutCoverage = 0;
}

print "score with student tests: $runtimeScoreWithoutCoverage\n"
    if ($debug > 2);


#=============================================================================
# post-process generated HTML files
#=============================================================================
my $clover = XML::Smart->new("$resultDir/clover.xml");
if (!$buildFailed) # $can_proceed)
{
    # Figure out mapping from class names to file names
    my $Uprojdir = $workingDir . "/";
    foreach my $pkg (@{ $clover->{coverage}{project}{package} })
    {
        my $pkgName = $pkg->{name}->content;
        # print "package: ", $pkg->{name}->content, "\n";
        foreach my $file (@{ $pkg->{file} })
        {
            # print "\tclass: ", $file->{class}->{name}->content, "\n";
            my $className = $file->{class}->{name}->content;
            if (!defined($className) || $className eq "") { next; }
            my $fqClassName = $className;
            my $fileName = $file->{name}->content;
            $fileName =~ s,\\,/,go;
            $fileName =~ s/^\Q$Uprojdir\E//io;
            if ($pkgName ne "default-pkg")
            {
                $fqClassName = $pkgName . ".$className";
            }
            $classToFileNameMap{$fqClassName} = $fileName;
        }
    }

    # Delete unneeded files from the clover/ html dir
    if (-d "$resultDir/clover")
    {
        processCloverDir("$resultDir/clover",
            !defined($status{'studentTestResults'})
                || !$status{'studentTestResults'}->hasResults
                || !$status{'studentTestResults'}->testsExecuted,
            $clover);
    }

    # If any classes in the default package, move them to correct place
    my $defPkgDir = "$resultDir/clover/default-pkg";
    if (-d $defPkgDir)
    {
        for my $file (<$defPkgDir/*>)
        {
            my $newLoc = $file;
            if ($newLoc =~ s,/default-pkg/,/,o)
            {
                rename($file, $newLoc);
            }
        }
        if (!rmdir($defPkgDir))
        {
            adminLog("cannot delete empty directory '$defPkgDir': $!");
        }
    }

    if ($debug > 1)
    {
        print "Clover'ed classes (from HTML):\n";
        foreach my $class (keys %cloveredClasses)
        {
            print "\t$class\n";
        }
        print "\n";
    }
}

my $time6 = time;
if ($debug)
{
    print "\n", ($time6 - $time5), " seconds\n";
}


if (!$buildFailed) # $can_proceed)
{
    my $numCodeMarkups = $cfg->getProperty('numCodeMarkups', 0);
    my $ptsPerUncovered = 0.0;
    if ($runtimeScoreWithoutCoverage > 0 &&
        $clover->{coverage}{project}{metrics}{methods} > 0)
    {
        my $topLevelGradedElements = 1;
        if ($coverageMetric == 1)
        {
            $topLevelGradedElements =
                $clover->{coverage}{project}{metrics}{statements};
            $cfg->setProperty("statElementsLabel", "Statements Executed");
        }
        elsif ($coverageMetric == 2)
        {
            $topLevelGradedElements =
                $clover->{coverage}{project}{metrics}{methods}
                + $clover->{coverage}{project}{metrics}{conditionals};
            $cfg->setProperty("statElementsLabel",
                              "Methods and Conditionals Executed");
        }
        elsif ($coverageMetric == 3)
        {
            $topLevelGradedElements =
                $clover->{coverage}{project}{metrics}{statements}
                + $clover->{coverage}{project}{metrics}{conditionals};
            $cfg->setProperty("statElementsLabel",
                              "Statements and Conditionals Executed");
        }
        elsif ($coverageMetric == 4)
        {
            $topLevelGradedElements =
                $clover->{coverage}{project}{metrics}{elements};
            $cfg->setProperty("statElementsLabel",
                              "Methods/Statements/Conditionals Executed");
        }
        else
        {
            $topLevelGradedElements =
                $clover->{coverage}{project}{metrics}{methods};
            $cfg->setProperty("statElementsLabel", "Methods Executed");
        }

        if ($studentsMustSubmitTests)
        {
            $ptsPerUncovered = -1.0 /
                $topLevelGradedElements * $runtimeScoreWithoutCoverage;
        }
    }
    my $Uprojdir = $workingDir . "/";
    foreach my $pkg (@{ $clover->{coverage}{project}{package} })
    {
        my $pkgName = $pkg->{name}->content;
        # print "package: ", $pkg->{name}->content, "\n";
        foreach my $file (@{ $pkg->{file} })
        {
            # print "\tclass: ", $file->{class}->{name}->content, "\n";
            my $className = $file->{class}->{name}->content;
            if (!defined($className) || $className eq "") { next; }
            my $fqClassName = $className;
            my $fileName = $file->{name}->content;
            $fileName =~ s,\\,/,go;
            $fileName =~ s/^\Q$Uprojdir\E//io;
            if ($pkgName ne "default-pkg")
            {
                $fqClassName = $pkgName . ".$className";
            }
#           if (!defined(delete($cloveredClasses{$fqClassName})))
#           {
#               # Instead of just an admin e-mail report, force grading
#               # to halt until the problem is fixed.
#               if ($fqClassName ne ".")
#               {
#                   print STDOUT
#                   "clover stats for $fqClassName have no corresponding "
#                   . "HTML file!\nTool comments for the student will not "
#                   . "be merged into the HTML output as a result.\n";
#               }
#               next;
#           }
            $numCodeMarkups++;
            $classToMarkupNoMap{$fqClassName} = $numCodeMarkups;
            $fileToMarkupNoMap{$fileName} = $numCodeMarkups;
            if ($pkgName ne "default-pkg")
            {
                $cfg->setProperty("codeMarkup${numCodeMarkups}.pkgName",
                                  $pkgName);
            }
            $cfg->setProperty("codeMarkup${numCodeMarkups}.className",
                              $className);
            my $metrics = $file->{metrics};
            $cfg->setProperty("codeMarkup${numCodeMarkups}.loc",
                              $metrics->{loc}->content);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.ncloc",
                              $metrics->{ncloc}->content);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.methods",
                              $metrics->{methods}->content);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.methodsCovered",
                              $metrics->{coveredmethods}->content);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.statements",
                              $metrics->{statements}->content);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.statementsCovered",
                              $metrics->{coveredstatements}->content);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.conditionals",
                              $metrics->{conditionals}->content);
            $cfg->setProperty(
                "codeMarkup${numCodeMarkups}.conditionalsCovered",
                $metrics->{coveredconditionals}->content);

            my $element1Type = 'methods';
            my $element2Type;
            if ($coverageMetric == 1)
            {
                $element1Type = 'statements';
            }
            elsif ($coverageMetric == 2)
            {
                $element1Type = 'methods';
                $element2Type = 'conditionals';
            }
            elsif ($coverageMetric == 3)
            {
                $element1Type = 'statements';
                $element2Type = 'conditionals';
            }
            elsif ($coverageMetric == 4)
            {
                $element1Type = 'elements';
            }
            my $myElements = $metrics->{$element1Type}->content;
            my $myElementsCovered =
                $metrics->{'covered' . $element1Type}->content;
            if (defined($element2Type))
            {
                $myElements +=  $metrics->{$element2Type}->content;
                $myElementsCovered +=
                $metrics->{'covered' . $element2Type}->content;
            }
            $gradedElements += $myElements;
            $gradedElementsCovered += $myElementsCovered;

            $cfg->setProperty("codeMarkup${numCodeMarkups}.elements",
                              $myElements);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.elementsCovered",
                              $myElementsCovered);
            #my $fileName = $fqClassName;
            #$fileName =~ s,\.,/,go;
            #$fileName .= ".java";
            $cfg->setProperty("codeMarkup${numCodeMarkups}.souceFileName",
                              $fileName);
            $cfg->setProperty("codeMarkup${numCodeMarkups}.deductions",
                ($myElements - $myElementsCovered) * $ptsPerUncovered
                - $messageStats->{file}->{$fileName}->{pts}->content);
        }
    }

    # Check that all HTML files were covered
#    if (0) # $can_proceed)
#    {
#        foreach my $class (keys %cloveredClasses)
#        {
#            # Instead of just an admin e-mail report, force grading
#            # to halt until the problem is fixed.
#            print STDOUT
#            "HTML file for $class has no corresponding clover stats!\n"
#            . "This will result in incorrect scoring, so processing of this "
#            . "submission is being paused.\n";
#            $cfg->setProperty("halt", 1);
#        }
#    }
#    else
#    {
#        # Force nasty grade?
#        # $gradedElementsCovered = 0;
#        foreach my $class (keys %cloveredClasses)
#        {
#            $numCodeMarkups++;
#            my $className = $class;
#            if ($class =~ m/^(.+)\.([^.])+$/o)
#            {
#                my $pkgName = $1;
#                $className = $2;
#                $cfg->setProperty("codeMarkup${numCodeMarkups}.pkgName",
#                                   $pkgName);
#            }
#            $cfg->setProperty("codeMarkup${numCodeMarkups}.className",
#                               $className);
#            $cfg->setProperty("codeMarkup${numCodeMarkups}.elements",
#                               1);
#            $cfg->setProperty("codeMarkup${numCodeMarkups}.elementsCovered",
#                               0);
#            my $fileName = $class;
#            $fileName =~ s,\.,/,go;
#            $fileName .= ".java";
#            $cfg->setProperty("codeMarkup${numCodeMarkups}.deductions",
#                               $ptsPerUncovered
#                - $messageStats->{file}->{$fileName}->{pts}->content);
#            $cfg->setProperty("codeMarkup${numCodeMarkups}.remarks",
#                $messageStats->{file}->{$fileName}->{remarks}->content);
#        }
#    }
    $cfg->setProperty("numCodeMarkups", $numCodeMarkups);

    foreach my $file (@{ $clover->{coverage}{project}{package}{file} })
    {
        my $fn = $file->{name}->content;
            $fn =~ s,\\,/,go;
            $fn =~ s/^\Q$Uprojdir\E//io;
        $cfg->setProperty(
            'codeMarkup' . $fileToMarkupNoMap{$fn} . '.remarks',
            (0 + $messageStats->{file}->{$fn}->{remarks}->content));
    }
}

my $time7 = time;
if ($debug)
{
    print "\n", ($time7 - $time6), " seconds\n";
}


#=============================================================================
# generate HTML version of student testing results
#=============================================================================
if ($status{'studentHasSrcs'}
    && ($studentsMustSubmitTests
        || (defined $status{'studentTestResults'}
            && $status{'studentTestResults'}->hasResults)))
{
    my $sectionTitle = "Results from Running Your Tests ";
    if (!defined $status{'studentTestResults'})
    {
        $sectionTitle .= "<b class=\"warn\">(No Test Results!)</b>";
    }
    elsif ($status{'studentTestResults'}->testsExecuted == 0)
    {
        $sectionTitle .= "<b class=\"warn\">(No Tests Submitted!)</b>";
    }
    elsif ($status{'studentTestResults'}->allTestsPass)
    {
        $sectionTitle .= "(100%)";
    }
    else
    {
        $sectionTitle .= "<b class=\"warn\">($studentCasesPercent%)</b>";
    }

    $status{'feedback'}->startFeedbackSection(
        $sectionTitle,
        ++$expSectionId,
        $status{'studentTestResults'}->allTestsPass);

    if ($allStudentTestsMustPass
        && $status{'studentTestResults'}->testsFailed > 0)
    {
        $status{'feedback'}->print(
            "<p><b class=\"warn\">All of your tests "
            . "must pass for you to get further feedback.</b>\n");
    }

    # Transform the plain text JUnit results to an interactive HTML view.
    JavaTddPlugin::transformTestResults("student_",
        "$resultDir/student-results.txt",
        "$resultDir/student-results.html");

    open(STUDENTRESULTS, "$resultDir/student-results.html");
    my @lines = <STUDENTRESULTS>;
    close(STUDENTRESULTS);
    if ($#lines >= 0)
    {
        $status{'feedback'}->print(<<EOF);
<p>The results of running your own test cases are shown below. Click on a
failed test to see the reason for the failure and an execution trace that
shows where the error occurred.</p>
EOF
        $status{'feedback'}->print(@lines);
    }
    unlink "$resultDir/student-results.html";

    @lines = linesFromFile("$resultDir/student-out.txt");
    if ($#lines >= 0)
    {
        $status{'feedback'}->startFeedbackSection(
            "Output from Your Tests", ++$expSectionId, 1, 2,
            "<pre>", "</pre>");
        $status{'feedback'}->print(@lines);
        $status{'feedback'}->endFeedbackSection;
    }

    $status{'feedback'}->endFeedbackSection;

    if ($gradedElements > 0
        || (defined $status{'studentTestResults'}
            && $status{'studentTestResults'}->testsExecuted > 0))
    {
        $codeCoveragePercent = ($gradedElements == 0)
            ? 0
            : int(($gradedElementsCovered / $gradedElements) * 100.0 + 0.5);
        if ($gradedElementsCovered < $gradedElements
            && $codeCoveragePercent == 100)
        {
            # Don't show 100% if some cases failed
            $codeCoveragePercent--;
        }

        $sectionTitle = "Code Coverage from Your Tests ";
        if ($gradedElements == 0)
        {
            $sectionTitle .= "<b class=\"warn\">(No Coverage!)</b>";
        }
        elsif ($gradedElements == $gradedElementsCovered)
        {
            $sectionTitle .= "(100%)";
        }
        else
        {
            $sectionTitle .= "<b class=\"warn\">($codeCoveragePercent%)</b>";
        }

        $status{'feedback'}->startFeedbackSection(
            $sectionTitle, ++$expSectionId, 1);

        $status{'feedback'}->print("<p><b>Code Coverage: ");
        if ($codeCoveragePercent < 100)
        {
            $status{'feedback'}->print(
                "<b class=\"warn\">$codeCoveragePercent%</b>");
        }
        else
        {
            $status{'feedback'}->print("$codeCoveragePercent%");
        }
        my $descr = $cfg->getProperty("statElementsLabel", "Methods Executed");
        $descr =~ tr/A-Z/a-z/;
        $descr =~ s/\s*executed\s*$//;
        $status{'feedback'}->print(<<EOF);
</b> (percentage of $descr exercised by your tests)</p>
<p>You can improve your testing by looking for any
<span style="background-color:#F0C8C8">lines highlighted in this color</span>
in your code listings above.  Such lines have not been sufficiently
tested--hover your mouse over them to find out why.
</p>
EOF
        $status{'feedback'}->endFeedbackSection;
    }
}

if (defined $status{'studentTestResults'}
    && $status{'studentTestResults'}->hasResults)
{
    $status{'studentTestResults'}->saveToCfg($cfg, 'student.test');
}
if (defined $status{'instrTestResults'}
    && $status{'instrTestResults'}->hasResults)
{
    $status{'instrTestResults'}->saveToCfg($cfg, 'instructor.test');
}
if (defined $messageStats)
{
    my $staticResults = '';
    foreach my $grp (keys %{$messageStats})
    {
        if (   $grp eq 'file'
            || $grp eq 'num'
            || $grp eq 'pts'
            || $grp eq 'collapse')
        {
            next;
        }

        foreach my $rule (keys(%{$messageStats->{$grp}}))
        {
            if (   $rule eq 'file'
                || $rule eq 'num'
                || $rule eq 'pts'
                || $rule eq 'collapse')
            {
                next;
            }
            my $thisRule = '{'
                . '"name"="' . $rule . '";'
                . '"group"="' . $grp . '";'
                . '"count"="' . $messageStats->{$grp}->{$rule}->{num} . '";'
                . '"pts"="' . $messageStats->{$grp}->{$rule}->{pts} . '";'
                . '}';
            if ($staticResults eq '')
            {
                $staticResults = $thisRule;
            }
            else
            {
               $staticResults .= ',' . $thisRule;
            }
        }
    }
    $cfg->setProperty('static.analysis.results', '(' . $staticResults . ')');
}
$cfg->setProperty('outcomeProperties',
    '("instructor.test.results", "student.test.results", '
    . '"static.analysis.results")');


#=============================================================================
# generate reference test results
#=============================================================================
if (defined $status{'instrTestResults'})
{
    my $sectionTitle = "Estimate of Problem Coverage ";
    if ($status{'instrTestResults'}->testsExecuted == 0
        || ($studentsMustSubmitTests
            && !$status{'studentTestResults'}->hasResults))
    {
        $sectionTitle .=
            "<b class=\"warn\">(Unknown!)</b>";
        $instructorCasesPercent = "unknown";
    }
    elsif ($status{'instrTestResults'}->allTestsPass)
    {
        $sectionTitle .= "(100%)";
        $instructorCasesPercent = 100;
    }
    else
    {
        $instructorCasesPercent =
            int($status{'instrTestResults'}->testPassRate * 100.0 + 0.5);
        if ($instructorCasesPercent == 100)
        {
            # Don't show 100% if some cases failed
            $instructorCasesPercent--;
        }
        $sectionTitle .= "<b class=\"warn\">($instructorCasesPercent%)</b>";
    }

    $status{'feedback'}->startFeedbackSection(
        $sectionTitle, ++$expSectionId, ($instructorCasesPercent >= 100));
    $status{'feedback'}->print("<p><b>Problem coverage: ");
    if ($instructorCasesPercent == 100)
    {
        $status{'feedback'}->print("100%");
    }
    else
    {
        $status{'feedback'}->print(
            "<b class=\"warn\">$instructorCasesPercent");
        if ($instructorCasesPercent ne "unknown")
        {
            $status{'feedback'}->print("%");
        }
        $status{'feedback'}->print("</b>");
    }
    $status{'feedback'}->print("</b></p>");


    if ($status{'compileErrs'}) # $instructorCases == 0
    {
        $status{'feedback'}->print(<<EOF);
<p><b class="warn">Web-CAT was unable to assess your test
cases.</b></p>
<p>For this assignment, the proportion of the problem that is covered by your
EOF
        if ($studentsMustSubmitTests)
        {
            $status{'feedback'}->print(<<EOF);
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
EOF
        }
        else
        {
            $status{'feedback'}->print(<<EOF);
solution is being assessed by running a suite of reference tests.</p>
EOF
        }
        $status{'feedback'}->print(<<EOF);
<p><b class="warn">Your code failed to compile correctly against
the reference tests.</b></p>
<p>This is most likely because you have not named your class(es)
as required in the assignment, have failed to provide one or more required
methods, or have failed to use the required signature for a method.</p>
<p>Failure to follow these constraints will prevent the proper assessment
of your solution and your tests.</p>
EOF
        if ($status{'compileMsgs'} ne "")
        {
            $status{'feedback'}->print(<<EOF);
<p>The following specific error(s) were discovered while compiling
reference tests against your submission:</p>
</p>
<pre>
EOF
            $status{'feedback'}->print($status{'compileMsgs'});
            $status{'feedback'}->print("</pre>\n");
        }
    }
    elsif ($studentsMustSubmitTests
        && !$status{'studentTestResults'}->hasResults)
    {
        $status{'feedback'}->print(<<EOF);
<p><b class="warn">You are required to write your own software tests
for this assignment.  You must provide your own tests
to get further feedback.</b></p>
EOF
    }
    elsif ($status{'instrTestResults'}->allTestsFail)
    {
        $status{'feedback'}->print(<<EOF);
<p><b class="warn">Your problem setup does not appear to be
consistent with the assignment.</b></p>
EOF
        if ($studentsMustSubmitTests)
        {
            $status{'feedback'}->print(<<EOF);
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
EOF
        }
        else
        {
            $status{'feedback'}->print(<<EOF);
<p>For this assignment, the proportion of the problem that is covered by your
solution is being assessed by running a suite of reference tests against
your solution.</p>
EOF
        }
        $status{'feedback'}->print(<<EOF);
<p>In this case, <b>none of the reference tests pass</b> on your solution,
which may mean that your solution (and your tests) make incorrect assumptions
about some aspect of the required behavior.
This discrepancy prevented Web-CAT from properly assessing the thoroughness
of your solution or your test cases.</p>
<p>Double check that you have carefully followed all initial conditions
requested in the assignment in setting up your solution.</p>
EOF
    }
    elsif ($status{'instrTestResults'}->allTestsPass)
    {
        $status{'feedback'}->print(<<EOF);
<p>Your solution appears to cover all required behavior for this assignment.
EOF
        if ($studentsMustSubmitTests)
        {
            $status{'feedback'}->print(<<EOF);
Make sure that your tests cover all of the behavior required.</p>
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
EOF
        }
        else
        {
            $status{'feedback'}->print(<<EOF);
</p><p>For this assignment, the proportion of the problem that is covered by
your solution is being assessed by running a suite of reference tests against
your solution.</p>
EOF
        }
    }
    else
    {
        if ($studentsMustSubmitTests)
        {
            $status{'feedback'}->print(<<EOF);
<p>For this assignment, the proportion of the problem that is covered by your
test cases is being assessed by running a suite of reference tests against
your solution, and comparing the results of the reference tests against the
results produced by your tests.</p>
<p>Differences in test results indicate that your code still contains bugs.
Your code appears to cover
<b class="warn">only $instructorCasesPercent%</b>
of the behavior required for this assignment.</p>
<p>
Your test cases are not detecting these defects, so your testing is
incomplete--covering at most <b class="warn">only
$instructorCasesPercent%</b>
of the required behavior, possibly even less.</p>
EOF
        }
        else
        {
            $status{'feedback'}->print(<<EOF);
<p>For this assignment, the proportion of the problem that is covered by your
solution is being assessed by running a suite of reference tests against
your solution.</p>
<p>
Test results indicate that your code still contains bugs.
Your code appears to cover
<b class="warn">only $instructorCasesPercent%</b>
of the behavior required for this assignment.</p>
EOF
        }
        $status{'feedback'}->print(<<EOF);
<p>Double check that you have carefully followed all initial conditions
requested in the assignment in setting up your solution, and that you
have also met all requirements for a complete solution in the final
state of your program.</p>
EOF
    }
    if ($hintsLimit != 0)
    {
        if ($studentsMustSubmitTests
            && $hasJUnitErrors
            && $junitErrorsHideHints)
        {
            $status{'feedback'}->print(<<EOF);
<p>Your JUnit test classes contain <b class="warn">problems that must be
fixed</b> before you can receive any more specific feedback.  Be sure that
all of your test classes contain test methods, and that all of your test
methods include appropriate assertions to check for expected behavior.
You must fix these problems with your own tests to get further feedback.</p>
EOF
        }
        elsif ($studentsMustSubmitTests
            && (!$status{'studentTestResults'}->hasResults
                || $gradedElementsCovered / $gradedElements * 100.0 <
                   $minCoverageLevel))
        {
            $status{'feedback'}->print(<<EOF);
<p>Your JUnit test cases <b class="warn">do not exercise enough of your
solution</b> for you to receive any more specific feedback.  Improve your
testing by writing more test cases that exercise more of your solution's
features.  Be sure to write <b>meaningful tests</b> that include appropriate
assertions to check for expected behavior.  You must improve your testing
to get further feedback.</p>
EOF
        }
        else
        {
            my $hints = $status{'instrTestResults'}->formatHints(
                0, $hintsLimit);
            if (defined $hints && $hints ne "")
            {
                my $extra = "";
                if ($studentsMustSubmitTests)
                {
                    $extra = "and your testing ";
                }
                $status{'feedback'}->print(<<EOF);
<p>The following hint(s) may help you locate some ways in which your solution
$extra may be improved:</p>
$hints
EOF
            }
        }
    }
    elsif ($extraHintMsg ne "")
    {
        $status{'feedback'}->print("<p>$extraHintMsg</p>");
    }

    # Generate staff-targeted info
    {
        $status{'instrFeedback'}->startFeedbackSection(
            "Detailed Reference Test Results", ++$expSectionId, 1);
        my $hints = $status{'instrTestResults'}->formatHints(2);
        if (defined $hints && $hints ne "")
        {
            $status{'instrFeedback'}->print($hints);
            $status{'instrFeedback'}->print("\n");
        }

        # Transform the plain text JUnit results into an interactive HTML
        # view.
        JavaTddPlugin::transformTestResults("instr_",
            "$resultDir/instr-results.txt",
            "$resultDir/instr-results.html");

        open(INSTRRESULTS, "$resultDir/instr-results.html");
        my @lines = <INSTRRESULTS>;
        close(INSTRRESULTS);
        if ($#lines >= 0)
        {
            $status{'instrFeedback'}->print(<<EOF);
<p>The results of running the instructor's reference test cases are shown
below. Click on a failed test to see the reason for the failure and an
execution trace that shows where the error occurred.</p>
EOF
            $status{'instrFeedback'}->print(@lines);
        }
        unlink "$resultDir/instr-results.html";

        @lines = linesFromFile("$resultDir/instr-out.txt");
        if ($#lines >= 0)
        {
            $status{'instrFeedback'}->startFeedbackSection(
                "Output from Reference Tests", ++$expSectionId, 1, 2,
                "<pre>", "</pre>");
            $status{'instrFeedback'}->print(@lines);
            $status{'instrFeedback'}->endFeedbackSection;
        }
        $status{'instrFeedback'}->endFeedbackSection;
    }
}


#=============================================================================
# generate HTML versions of any other source files
#=============================================================================

my $beautifier = new Web_CAT::Beautifier;
$beautifier->beautifyCwd($cfg, \@beautifierIgnoreFiles);


#=============================================================================
# generate score
#=============================================================================

# First, the static analysis, tool-based score
my $staticScore  = $maxToolScore - $status{'toolDeductions'};

# Second, the coverage/testing/correctness component
my $runtimeScore = $runtimeScoreWithoutCoverage;
if ($studentsMustSubmitTests)
{
    if ($gradedElements > 0)
    {
        $runtimeScore *= $gradedElementsCovered / $gradedElements;
    }
    else
    {
        $runtimeScore = 0;
    }
}

print "score with coverage: $runtimeScore ($gradedElementsCovered "
    . "elements / $gradedElements covered)\n" if ($debug > 2);

# Total them up
# my $rawScore = $can_proceed
#     ? ($staticScore + $runtimeScore)
#     : 0;


#=============================================================================
# generate score explanation for student
#=============================================================================
if ($can_proceed && $studentsMustSubmitTests)
{
    my $scoreToTenths = int($runtimeScore * 10 + 0.5) / 10;
    my $possible = int($maxCorrectnessScore * 10 + 0.5) / 10;
    $status{'feedback'}->startFeedbackSection(
        "Interpreting Your Correctness/Testing Score "
        . "($scoreToTenths/$possible)",
        ++$expSectionId,
        1);
    $status{'feedback'}->print(<<EOF);
<table style="border:none">
<tr><td><b>Results from running your tests:</b></td>
<td class="n">$studentCasesPercent%</td></tr>
<tr><td><b>Code coverage from your tests:</b></td>
<td class="n">$codeCoveragePercent%</td></tr>
<tr><td><b>Estimate of problem coverage:</b></td>
<td class="n">$instructorCasesPercent%</td></tr>
<tr><td colspan="2">score =
$studentCasesPercent%
* $codeCoveragePercent%
* $instructorCasesPercent%
* $maxCorrectnessScore
points possible = $scoreToTenths</p>
</table>
<p>Full-precision (unrounded) percentages are used to calculate
your score, not the rounded numbers shown above.</p>
EOF
    $status{'feedback'}->endFeedbackSection;
}


#=============================================================================
# generate collapsible section for class diagrams
#=============================================================================
if (-d $diagrams && scalar <$diagrams/*>)
{
    $status{'feedback'}->startFeedbackSection(
        "Graphical Representation of Your Class Design",
        ++$expSectionId);
    $status{'feedback'}->print(<<EOF);
<p>The images below present a graphical representation of your
solution's class design. An arrow pointing from <b>B</b> to
<b>A</b> means that <b>B</b> extends/implements <b>A</b>. These
diagrams are provided for your benefit as well as for the course
staff to refer to when grading.</p>
<div style="border: 1px solid gray; background-color: white; padding: 1em">
EOF
    opendir my($dirhandle), $diagrams;
    my @diagramFiles = readdir $dirhandle;
    closedir $dirhandle;

    for my $diagramFile (@diagramFiles)
    {
        if ($diagramFile !~ /^\..*/)
        {
            my $url = "\${publicResourceURL}/diagrams/$diagramFile";
            $status{'feedback'}->print(
                "<span style=\"margin-right: 1em\">"
                . "<img src=\"$url\"/></span>\n");
        }
    }

    $status{'feedback'}->print('</div>');
    $status{'feedback'}->endFeedbackSection;
}


#=============================================================================
# Update and rewrite properties to reflect status
#=============================================================================

# Student feedback
# -----------
{
    my $rptFile = $status{'feedback'};
    if (defined $rptFile)
    {
        $rptFile->close;
        if ($rptFile->hasContent)
        {
            addReportFileWithStyle($cfg, $rptFile->fileName, 'text/html', 1);
        }
        else
        {
            $rptFile->unlink;
        }
    }
}

# Instructor feedback
# -----------
{
    my $rptFile = $status{'instrFeedback'};
    if (defined $rptFile)
    {
        $rptFile->close;
        if ($rptFile->hasContent)
        {
            addReportFileWithStyle(
                $cfg, $rptFile->fileName, 'text/html', 1, 'staff');
        }
        else
        {
            $rptFile->unlink;
        }
    }
}

# PDF printout
# -----------
if (-f $pdfPrintout)
{
    addReportFileWithStyle(
        $cfg,
        $pdfPrintoutRelative,
        "application/pdf",
        1,
        undef,
        "false",
        "PDF code printout");
}

# Script log
# ----------
if (-f $scriptLog && stat($scriptLog)->size > 0)
{
    addReportFileWithStyle($cfg, $scriptLogRelative, "text/plain", 0, "admin");
    addReportFileWithStyle($cfg, $antLogRelative,    "text/plain", 0, "admin");
}

$cfg->setProperty('score.correctness', $runtimeScore);
$cfg->setProperty('score.tools',       $staticScore );
$cfg->setProperty('expSectionId',      $expSectionId);
$cfg->save();

if ($debug)
{
    my $lasttime = time;
    print "\n", ($lasttime - $time1), " seconds total\n";
    print "\nFinal properties:\n-----------------\n";
    my $props = $cfg->getProperties();
    while ((my $key, my $value) = each %{$props})
    {
        print $key, " => ", $value, "\n";
    }
}


#-----------------------------------------------------------------------------
exit(0);
#-----------------------------------------------------------------------------
