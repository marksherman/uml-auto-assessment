#!perl -W

use strict;

my $verbose         = 0;
print("args: ", join(" ", @ARGV), "\n") if ($verbose);

my $driver = shift @ARGV;
my $mods   = join(',', @ARGV);
$mods =~ s/\s+//g;
$mods =~ s/,,+/,/g;
my @modules = split(',', $mods);

open(DRIVER_IN, "$driver")
    || die "Cannot open $driver for reading: $!";
my @lines = <DRIVER_IN>;
close(DRIVER_IN) || print STDERR "Error closing $driver: $!";

open(DRIVER_OUT, ">", "$driver")
    || die "Cannot open $driver for writing: $!";
for (@lines)
{
    if (m/^\s*import ([\w\.]+)/o && $1 ne "Test.HUnit" && $1 ne "System.IO")
    {
        while ($#modules >= 0)
        {
            print DRIVER_OUT "import ", shift @modules, "\n";
        }
        next;
    }
    print DRIVER_OUT $_;
}
close(DRIVER_OUT) || print STDERR "Error closing $driver: $!";

exit(0);
