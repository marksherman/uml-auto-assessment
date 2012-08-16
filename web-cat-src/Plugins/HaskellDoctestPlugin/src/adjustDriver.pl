#!perl -W

# done: Failed compilation (doctest)
# done: Warnings in user code
# done: Warnings in driver?


use strict;

my $verbose         = 0;
print("args: ", join(" ", @ARGV), "\n") if ($verbose);

my $driver          = shift @ARGV;
my $compilerOut     = shift @ARGV;
my $studentFeedback = shift @ARGV;
my $otherArgs       = "";
if ($#ARGV >= 0)
{
    $otherArgs = " " . join(" ", @ARGV);
}
my $compileCount    = 0;
my $compileLimit    = 9;
my $warnLog         = "";

sub compileOnce
{
    # Snarf test driver and identify test cases:
    my $driverPrefix = "";
    my $driverPostfix = "";
    my $firstCaseLineNo = 1;
    my $errors = 0;
    my @caseLines = ();
    my @caseExprs = ();
    {
        open(DRIVER_IN, "$driver")
            || die "Cannot open $driver for reading: $!";
        $driverPrefix = <DRIVER_IN>;
        ++$firstCaseLineNo;
        if (defined $driverPrefix && $driverPrefix !~ m/module Main where/o)
        {
            # A compilation error happened in driver generation!
            print "Compilation error generating test driver\n";
            my $errorLog = $driverPrefix;
            while (<DRIVER_IN>)
            {
                if (m/module Main where/o)
                {
                    last;
                }
                $errorLog .= $_;
            }
            close(DRIVER_IN) || print STDERR "Error closing $driver: $!";
            open(COMPILERLOG, ">$compilerOut")
                || die "Cannot open $compilerOut for writing: $!";
            print COMPILERLOG $errorLog;
            close(COMPILERLOG) || print STDERR "Error closing $compilerOut: $!";
            exit(1);
        }
        while (<DRIVER_IN>)
        {
            last if (m/^    [\(,]/o);
            $driverPrefix .= $_;
            ++$firstCaseLineNo;
        }
        print "first case on line # $firstCaseLineNo\n" if ($verbose);
        while (defined($_) && m/^    [\(,]/o)
        {
            push(@caseLines, $_);
            m/\[show \((.*)\)\]\)\)\)$/o;
            my $case = $1;
            print "test case ", $#caseLines, " = ", $case, "\n" if ($verbose);
            push(@caseExprs, $case);
            $_ = <DRIVER_IN>;
        }
        if (defined $_)
        {
            $driverPostfix = $_;
            while (<DRIVER_IN>)
            {
                $driverPostfix .= $_;
            }
        }
        close(DRIVER_IN) || print STDERR "Error closing $driver: $!";
    }


    # Compile it
    my $cmd = "ghc$otherArgs -w $driver > $compilerOut 2>&1";
    print "compiling: $cmd\n" if ($verbose);
    system($cmd);


    # Snarf compiler log and generate messages:
    {
        open(COMPILERLOG, $compilerOut)
            || die "Cannot open $compilerOut for reading: $!";
        open(FEEDBACK, ">>$studentFeedback")
            || die "Cannot open $studentFeedback for writing: $!";

        if ($#caseExprs < 0)
        {
            if ($errors > 0)
            {
                print FEEDBACK "\nNo more test cases remain!\n";
            }
            else
            {
                print FEEDBACK "\nNo test cases found!\n";
            }
            exit(1);
        }


        $_ = <COMPILERLOG>;
        while (defined $_)
        {
        	print "checking: $_" if ($verbose);
            if (m/(^\[[0-9]+ of [0-9]+\])|(^\s*$)/o)
            {
                $_ = <COMPILERLOG>;
                next;
            }
            elsif (s/^\S+(\\|\/)__driver.hs:([0-9]+):([0-9]+):\s*//o)
            {
                my $caseNo = $2;
                my $index = $caseNo - $firstCaseLineNo;
                print ("error on line $caseNo, index = $index\n") if ($verbose);
                my $expr = $caseExprs[$index];
                $caseLines[$index] = "";
                if ($_ =~ m/^\s*$/o)
                {
                    $_ = <COMPILERLOG>;
                }
                else
                {
                     $_ = "    " . $_;
                }
                my $message = "";
                my $ignoreRest = 0;
                while (defined($_) && m/^\s+\S/o)
                {
                    if (m/(argument of `show')|(expression: show)/o)
                    {
                        ++$ignoreRest;
                    }
                    if (!$ignoreRest)
                    {
                        $message .= $_;
                    }
                    $_ = <COMPILERLOG>;
                }
                print FEEDBACK
                    "Cannot compile test expression: `$expr'\n$message\n";
                print "FEEDBACK => ",
                    "Cannot compile test expression: `$expr'\n$message\n";
                ++$errors;
                print "error $errors: $message" if ($verbose);
                next;
            }
            elsif (s/^\S+(\\|\/)(([^\\\/]*):([0-9]+):([0-9]+):\s*)/$2/)
            {
                # Found an error/warning, so collect for error log
                while (defined($_) && m/\S/o && ! m/^\[[0-9]+ of [0-9]+\]/o)
                {
                    $warnLog .= $_;
                    $_ = <COMPILERLOG>;
                }
                $warnLog .= "\n";
            }
            print "just checked: $_";
            $_ = <COMPILERLOG>;
        }

        if ($compileCount >= $compileLimit && $errors)
        {
            print FEEDBACK
                "\nGiving up attempting to compile test driver after ",
                $compileCount + 1,
                " passes!\n";
        }

        close(COMPILERLOG) || print STDERR "Error closing $compilerOut: $!";
        close(FEEDBACK) || print STDERR "Error closing $studentFeedback: $!";
    }


    # Generate new test driver
    {
        for my $i (0 .. $#caseLines)
        {
            if ($caseLines[$i] ne "")
            {
                $caseLines[$i] =~ s/^    ,\s+/    /o;
                last;
            }
        }

        open(DRIVER_OUT, ">$driver")
            || die "Cannot open $driver for writing: $!";
    	print DRIVER_OUT $driverPrefix;
	    for my $line (@caseLines)
	    {
            print DRIVER_OUT $line;
	    }
        print DRIVER_OUT $driverPostfix;
        close(DRIVER_OUT)  || print STDERR "Error closing $driver: $!";
    }

    return $errors;
}

my $lastErrs = 0;
while (($lastErrs = compileOnce()) > 0 && $compileCount < $compileLimit)
{
    ++$compileCount;
#    exit(0);
}

if (!$lastErrs)
{
    if ($warnLog ne "")
    {
        open(COMPILERLOG, ">$compilerOut")
            || print STDERR "Error opening $compilerOut for writing: $!";
        print COMPILERLOG $warnLog;
        close(COMPILERLOG) || print STDERR "Error closing $compilerOut: $!";
    }
    else
    {
        unlink($compilerOut);
    }
}

exit(0);



# stdout redirection code
# import System.IO
# import System.Posix.IO
#
# evilLibraryCall :: IO ()
# evilLibraryCall = putStr "Hello, posix" >> hFlush stdout
#
# main = do
#   (rd, wr) <- createPipe
#   savedStdout <- dup stdOutput
#   dupTo wr stdOutput
#   evilLibraryCall
#   (str, _) <- fdRead rd 1024
#   dupTo savedStdout stdOutput
#   putStrLn $ "redirected: " ++ str
