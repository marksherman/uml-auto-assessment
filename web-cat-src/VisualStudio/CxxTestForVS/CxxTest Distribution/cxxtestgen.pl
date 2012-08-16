#!/usr/bin/perl -w
use strict;
use Getopt::Long;

my $actualCmdLine = join(" ", @ARGV);

sub usage() {
  print STDERR "Usage: $0 [OPTIONS] <input file(s)>\n";
  print STDERR "Generate test source file for CxxTest.\n";
  print STDERR "\n";
  print STDERR "  -v, --version        Write CxxTest version\n";
  print STDERR "  -o, --output=NAME    Write output to file NAME\n";
  print STDERR "  --runner=CLASS       Create a main() function that runs CxxTest::CLASS\n";
  print STDERR "  --gui=CLASS          Like --runner, with GUI component\n";
  print STDERR "  --error-printer      Same as --runner=ErrorPrinter\n";
  print STDERR "  --abort-on-fail      Abort tests on failed asserts (like xUnit)\n";
  print STDERR "  --have-eh            Use exception handling (even if not found in tests)\n";
  print STDERR "  --no-eh              Don't use exception handling (even if found in tests)\n";
  print STDERR "  --longlong=[TYPE]    Use TYPE as `long long' (defaut = long long)\n";
  print STDERR "  --template=TEMPLATE  Use TEMPLATE file to generate the test runner\n";
  print STDERR "  --include=HEADER     Include \"HEADER\" in test runner before other headers\n";
  print STDERR "  --root               Write CxxTest globals\n";
  print STDERR "  --part               Don't write CxxTest globals\n";
  print STDERR "  --no-static-init     Don't rely on static initialization\n";
  print STDERR "  --track-heap         Track heap usage\n";
  print STDERR "  --trap-signals       Trap SIGSEGV and other signals\n";
  print STDERR "  --trace-stack        Include stack traces in error messages\n";
  exit -1;
}

main();

sub main {
  parseCommandline();
  scanInputFiles();
  writeOutput();
}

#
# Handling the command line
#

my ($output, $runner, $gui, $template, $abortOnFail, $haveStd, $haveEh, $noEh);
my ($root, $part, $noStaticInit, $longlong, $factor, $forceMain, $trackHeap);
my ($trapSignals, $traceStack, $errorPrinterFile);
my @headers = ();

sub parseCommandline() {
  @ARGV = expandWildcards(@ARGV);
  GetOptions( 'version'        => \&printVersion,
              'output=s'       => \$output,
              'template=s'     => \$template,
              'runner=s'       => \$runner,
              'gui=s',         => \$gui,
              'error-printer'  => sub { $runner = 'ErrorPrinter'; $haveStd = 1; },
              'ErrorPrinterFile=s'       => \$errorPrinterFile,
              'abort-on-fail'  => \$abortOnFail,
              'have-eh'        => \$haveEh,
              'no-eh'          => \$noEh,
              'include=s'      => \@headers,
              'root'           => \$root,
              'part'           => \$part,
              'no-static-init' => \$noStaticInit,
              'track-heap'     => \$trackHeap,
              'trap-signals'   => \$trapSignals,
              'trace-stack'    => \$traceStack,
              'factor'         => \$factor,
              'force-main'     => \$forceMain,
              'longlong:s'     => \$longlong
            ) or usage();
  scalar @ARGV or $root or usage();

  if ( defined($noStaticInit) && (defined($root) || defined($part)) ) {
    die "--no-static-init cannot be used with --root/--part\n";
  }

  if ( $gui && !$runner ) {
    $runner = 'StdioPrinter';
  }

  if ( defined($longlong) && !$longlong ) {
    $longlong = 'long long';
  }

  foreach my $header (@headers) {
    if ( !($header =~ m/^["<].*[>"]$/) ) {
      $header = "\"$header\"";
    }
  }
}

sub printVersion() {
  print "This is CxxTest version 3.9.1.\n";
  exit 0;
}

sub expandWildcards() {
  my @result = ();
  while( my $fn = shift @_ ) {
    push @result, glob($fn);
  }
  return @result;
}

#
# Reading the input files and scanning for test cases
#

my (@suites, $suite, $test, $inBlock);
my $numTotalTests = 0;

sub scanInputFiles() {
  foreach my $file (@ARGV) {
    scanInputFile( $file );
  }
  scalar @suites or $root or die('No tests defined\n');
}

sub scanInputFile($) {
  my ($file) = @_;
  open FILE, "<$file" or die("Cannot open input file \"$file\"\n");

  my $line;
  while (defined($line = <FILE>)) {
    scanLineForExceptionHandling( $line );
    scanLineForStandardLibrary( $line );

    # Just in case the superclass starts on the next line ...
    my $lineNo = $.;
    if ( $line =~ m/\bclass\s+(\w+)\s*:\s*(\/\/.*)?$/ ) {
        chomp( $line );
        $line .= <FILE>;
    }
    scanLineForSuiteStart( $file, $lineNo, $line );

    if ( $suite ) {
      if ( lineBelongsToSuite( $suite, $., $line ) ) {
        scanLineForTest( $., $line );
        scanLineForCreate( $., $line );
        scanLineForDestroy( $., $line );
      }
    }
  }
  closeSuite();
  close FILE;
}

sub lineBelongsToSuite($$$) {
  my ($suite, $lineNo, $line) = @_;
  if ( !$suite->{'generated'} ) {
    return 1;
  }

  if ( !$inBlock ) {
    $inBlock = lineStartsBlock( $line );
  }
  if ( $inBlock ) {
    addLineToBlock( $suite->{'file'}, $lineNo, $line );
  }
  return $inBlock;
}

sub scanLineForExceptionHandling($) {
  my ($line) = @_;
  if ( $line =~ m/\b(try|throw|catch|TSM?_ASSERT_THROWS|TSM?_ASSERT_THROWS_ANYTHING|TSM?_ASSERT_THROWS_NOTHING)\b/ ) {
    addExceptionHandling();
  }
}

sub scanLineForStandardLibrary($) {
  my ($line) = @_;
  if ( $line =~ m/\b(std\s*::|CXXTEST_STD|using\s+namespace\s+std\b|^\s*\#\s*include\s+<[a-z0-9]+>)/ ) {
    addStandardLibrary();
  }
}

sub scanLineForSuiteStart($$$) {
  my ($fileName, $lineNo, $line) = @_;
  if ( $line =~ m/\bclass\s+(\w+)\s*:\s*public\s+((::)?\s*CxxTest\s*::\s*)?TestSuite\b/ ) {
    startSuite( $1, $fileName, $lineNo, 0 );
  }
  if ( $line =~ m/\bCXXTEST_SUITE\s*\(\s*(\w*)\s*\)/ ) {
    print "$fileName:$lineNo: Warning: Inline test suites are deprecated.\n";
    startSuite( $1, $fileName, $lineNo, 1 );
  }
}

sub startSuite($$$$) {
  my ($name, $file, $line, $generated) = @_;
  closeSuite();
  $suite = { 'name' => $name,
             'file' => $file,
             'line' => $line,
             'generated' => $generated,
             'create' => 0,
             'destroy' => 0,
             'tests' => [],
             'lines' => [] };
}

sub lineStartsBlock($) {
  my ($line) = @_;
  return $line =~ m/\bCXXTEST_CODE\s*\(/;
}

sub scanLineForTest($$) {
  my ($lineNo, $line) = @_;
  if ( $line =~ m/^\s*\bvoid\s+([Tt]est\w+)\s*\(\s*(void)?\s*\)/ ) {
    addTest( $1, $lineNo );
  }
}

sub addTest($$$) {
  my ($name, $line) = @_;
  $test = { 'name' => $name,
            'line' => $line };
  push @{suiteTests()}, $test;
}

sub addLineToBlock($$$) {
  my ($fileName, $lineNo, $line) = @_;
  $line = fixBlockLine( $fileName, $lineNo, $line );
  $line =~ s/^.*\{\{//;
  my $end = ($line =~ s/\}\}.*//s);
  push @{$suite->{'lines'}}, $line;
  if ( $end ) {
    $inBlock = 0;
  }
}

sub fixBlockLine($$$) {
  my ($fileName, $lineNo, $line) = @_;
  my $fileLine = cstr($fileName) . "," . $lineNo;
  $line =~ s/\b(E?TSM?_(ASSERT[A-Z_]*|FAIL))\s*\(/_$1($fileLine,/g;
  return $line;
}

sub scanLineForCreate($$) {
  my ($lineNo, $line) = @_;
  if ( $line =~ m/\bstatic\s+\w+\s*\*\s*createSuite\s*\(\s*(void)?\s*\)/ ) {
    addCreateSuite( $lineNo );
  }
}

sub scanLineForDestroy($$) {
  my ($lineNo, $line) = @_;
  if ( $line =~ m/\bstatic\s+void\s+destroySuite\s*\(\s*\w+\s*\*\s*\w*\s*\)/ ) {
    addDestroySuite( $lineNo );
  }
}

sub closeSuite() {
  if ( $suite && scalar @{suiteTests()} ) {
    verifySuite();
    rememberSuite();
  }
  undef $suite;
}

sub addCreateSuite($) {
  $suite->{'createSuite'} = $_[0];
}

sub addDestroySuite($) {
  $suite->{'destroySuite'} = $_[0];
}

sub addExceptionHandling() {
  $haveEh = 1 unless defined($noEh);
}

sub addStandardLibrary() {
  $haveStd = 1;
}

sub verifySuite() {
  if (suiteCreateLine() || suiteDestroyLine()) {
    die("Suite ", suiteName(), "  must have both createSuite() and destroySuite()\n")
      unless (suiteCreateLine() && suiteDestroyLine());
  }
}

sub rememberSuite() {
  push @suites, $suite;
  $numTotalTests += scalar @{$suite->{'tests'}};
}

sub suiteName() { return $suite->{'name'}; }
sub suiteTests() { return $suite->{'tests'}; }
sub suiteCreateLine() { return $suite->{'createSuite'}; }
sub suiteDestroyLine() { return $suite->{'destroySuite'}; }
sub fileName() { return $suite->{'file'}; }
sub fileString() { return cstr(fileName()); }
sub testName() { return $test->{'name'}; }
sub testLine() { return $test->{'line'}; }

sub suiteObject() { return "suite_".suiteName(); }

sub cstr($) {
  my $file = $_[0];
  $file =~ s/\\/\\\\/g;
  return "\"".$file."\"";
}

#
# Writing the test source file
#

sub writeOutput() {
  $template ? writeTemplateOutput() : writeSimpleOutput();
}

sub startOutputFile() {
  if ( !standardOutput() ) {
    open OUTPUT_FILE,">$output" or die("Cannot create output file \"$output\"\n");
    select OUTPUT_FILE;
  }
  print "/* Generated file, do not edit */\n\n";
}

sub standardOutput() {
  return !$output;
}

sub writeSimpleOutput() {
  startOutputFile();
  writePreamble();
  writeWorld();
  writeMain();
}

my ($didPreamble, $didWorld);

sub writeTemplateOutput() {
  openTemplateFile();
  startOutputFile();
  my $line;
  while (defined($line = <TEMPLATE_FILE>)) {
    if ( $line =~ m/^\s*\#\s*include\s*<cxxtest\// ) {
      writePreamble();
      print $line;
    } elsif ( $line =~ m/^\s*<CxxTest\s+preamble>\s*$/ ) {
      writePreamble();
    } elsif ( $line =~ m/^\s*<CxxTest\s+world>\s*$/ ) {
      writeWorld();
    } else {
      print $line;
    }
  }
}

sub openTemplateFile() {
  open TEMPLATE_FILE, "<$template" or die("Cannot open template file \"$template\"\n");
}

sub writePreamble() {
  return if $didPreamble;
  print "#ifndef CXXTEST_RUNNING\n";
  print "#define CXXTEST_RUNNING\n";
  print "#endif\n";
  print "\n";
  if ( $haveStd ) {
    print "#define _CXXTEST_HAVE_STD\n";
  }
  if ( $haveEh ) {
    print "#define _CXXTEST_HAVE_EH\n";
  }
  if ( $abortOnFail ) {
    print "#define _CXXTEST_ABORT_TEST_ON_FAIL\n";
  }
  if ( $longlong ) {
    print "#define _CXXTEST_LONGLONG $longlong\n";
  }
  if ( $factor ) {
    print "#define _CXXTEST_FACTOR\n";
  }
  if ( $trapSignals )
  {
      print "#define CXXTEST_TRAP_SIGNALS\n";
  }
  if ( defined $traceStack )
  {
      print "#define CXXTEST_TRACE_STACK\n";
	  print "#include <symreader.c>\n";
  }
  foreach my $header (@headers) {
    print "#include $header\n";
  }
  print "#include <cxxtest/TestListener.h>\n";
  print "#include <cxxtest/TestTracker.h>\n";
  print "#include <cxxtest/TestRunner.h>\n";
  print "#include <cxxtest/RealDescriptions.h>\n";
  print "#include <cxxtest/$runner.h>\n" if $runner;
  print "#include <cxxtest/$gui.h>\n" if $gui;
  print "\n";
  print "typedef const CxxTest::SuiteDescription *SuiteDescriptionPtr;\n";
  print "typedef const CxxTest::TestDescription *TestDescriptionPtr;\n";
  print "\n";
  $didPreamble = 1;
}

sub writeWorld() {
  return if $didWorld;
  writePreamble();
  writeSuites();
  ($root or !$part) and writeRoot();
  $noStaticInit and writeInitialize();
  $didWorld = 1;
}

sub writeSuites() {
  foreach (@suites) {
    $suite = $_;
    writeInclude(fileName());
    if ( $suite->{'generated'} ) { generateSuite(); }
    dynamicSuite() ? writeSuitePointer() : writeSuiteObject();
    writeTestList();
    writeSuiteDescription();
    writeTestDescriptions();
  }
}

sub dynamicSuite() {
  return suiteCreateLine();
}

my $lastIncluded;

sub writeInclude($) {
  my $file = $_[0];
  return if $lastIncluded && ($file eq $lastIncluded);
  print "#include \"$file\"\n\n";
  $lastIncluded = $file;
}

sub generateSuite() {
  print "class ", suiteName(), " : public CxxTest::TestSuite {\n";
  print "public:\n";
  foreach my $line (@{$suite->{'lines'}}) {
    print $line;
  }
  print "};\n\n";
}

sub writeTestDescriptionsBase() {
  my $class = "TestDescriptionBase_" . suiteName();
  print "class $class : public CxxTest::TestDescription {\n";
  print "public:\n";
  print " const char *file() const { return ", fileString(), "; }\n";
  print " const char *suiteName() const { return \"", suiteName(), "\"; }\n";
  print "};\n\n";
}

sub writeTestPointers() {
  print "static const TestDescriptionPtr testDescriptions_", suiteName(), "[] = {\n";
  foreach (@{suiteTests()}) {
    $test = $_;
    print " &testDescription_", suiteName(), "_", testName(), ",\n";
  }
  print "};\n\n";
}

sub writeSuitePointer() {
  if ( $noStaticInit ) {
    print "static ", suiteName(), " *", suiteObject(), ";\n\n";
  } else {
    print "static ", suiteName(), " *", suiteObject(), " = 0;\n\n";
  }
}

sub writeSuiteObject() {
  print "static ", suiteName(), " ", suiteObject(), ";\n\n";
}

sub testList() {
  return "Tests_" . suiteName();
}

sub writeTestList() {
  if ( $noStaticInit ) {
    printf "static CxxTest::List %s;\n", testList();
  } else {
    printf "static CxxTest::List %s = { 0, 0 };\n", testList();
  }
}

sub writeTestDescriptions() {
  foreach (@{suiteTests()}) {
    $test = $_;
    writeTestDescription();
  }
}

sub suiteDescription() {
  return "suiteDescription_" . suiteName();
}

sub writeTestDescription() {
  my $class = "TestDescription_" . suiteName() . "_" . testName();
  printf "static class $class : public CxxTest::RealTestDescription {\n";
  printf "public:\n";
  $noStaticInit or
    printf " $class() : CxxTest::RealTestDescription( %s, %s, %s, \"%s\" ) {}\n",
      testList(), suiteDescription(), testLine(), testName();
  printf " void runTest() { %s }\n", dynamicSuite() ? dynamicRun() : staticRun();
  printf "} testDescription_%s_%s;\n\n", suiteName(), testName();
}

sub dynamicRun() {
  return sprintf( "if ( %s ) %s->%s();", suiteObject(), suiteObject(), testName() );
}

sub staticRun() {
  return sprintf( "%s.%s();", suiteObject(), testName() );
}

sub writeSuiteDescription() {
  dynamicSuite() ? writeDynamicDescription() : writeStaticDescription();
}

sub writeDynamicDescription() {
  printf "CxxTest::DynamicSuiteDescription<%s> %s", suiteName(), suiteDescription();
  if ( !$noStaticInit ) {
    printf "( %s, %s, \"%s\", %s, %s, %s, %s )",
      fileString(), $suite->{'line'}, suiteName(), testList(),
        suiteObject(), suiteCreateLine(), suiteDestroyLine();
  }
  print ";\n\n";
}

sub writeStaticDescription() {
  printf "CxxTest::StaticSuiteDescription %s", suiteDescription();
  if ( !$noStaticInit ) {
    printf "( %s, %s, \"%s\", %s, %s )", fileString(), $suite->{'line'}, suiteName(), suiteObject(), testList();
  }
  print ";\n\n";
}

sub writeRoot() {
  if ( $trackHeap ) { print "#define CHKPTR_BASIC_HEAP_CHECK\n"; }
  print "#include <cxxtest/Root.cpp>\n";
}

sub writeInitialize() {
  print "namespace CxxTest {\n";
  print " void initialize()\n";
  print " {\n";
  foreach (@suites) {
    $suite = $_;
    printf "  %s.initialize();\n", testList();
    if ( dynamicSuite() ) {
      printf "  %s = 0;\n", suiteObject();
      printf "  %s.initialize( %s, %s, \"%s\", %s, %s, %s, %s );\n",
        suiteDescription(), fileString(), $suite->{'line'}, suiteName(), testList(),
          suiteObject(), suiteCreateLine(), suiteDestroyLine();
    } else {
      printf "  %s.initialize( %s, %s, \"%s\", %s, %s );\n",
        suiteDescription(), fileString(), $suite->{'line'}, suiteName(), suiteObject(), testList();
    }

    foreach (@{suiteTests()}) {
      $test = $_;
      printf "  testDescription_%s_%s.initialize( %s, %s, %s, \"%s\" );\n",
        suiteName(), testName(), testList(), suiteDescription(), testLine(), testName();
    }
  }
  print " }\n";
  print "}\n";
}

sub writeChkPtrErrorHandler()
{
    print <<EOF;
void __cxxtest_chkptr_error_handler(bool fatal, const char* msg)
     _CXXTEST_NO_INSTR;

void __cxxtest_chkptr_error_handler(bool fatal, const char* msg)
{
    char text[256];

    if(fatal)
        sprintf(text, "Pointer error: %s", msg);
    else
        strncpy(text, msg, 256);

    if(fatal)
    {
#ifdef CXXTEST_TRAP_SIGNALS
        CxxTest::__cxxtest_assertmsg = text;
#else
        printf("%s\\n", text);
#endif
        abort();
    }
    else
    {
        std::string finalMsg = text;

#ifdef CXXTEST_TRACE_STACK
    {
        std::string trace =
            CxxTest::getStackTrace(CxxTest::__cxxtest_runCompleted);
        if ( trace.length() )
        {
            finalMsg += "\\n";
            finalMsg += trace;
        }
    }
#endif

        if(!CxxTest::__cxxtest_runCompleted)
        {
            CxxTest::doWarn("", 0, finalMsg.c_str());
        }
        else
        {
            printf("Warning: %s\\n", finalMsg.c_str());
        }
    }
}

EOF
}


sub writeSigHandler()
{
    if ( !$trapSignals ) { return; }
    print <<EOF;
#include <signal.h>     // for siginfo_t and signal constants
#include <setjmp.h>     // for siglongjmp()
#include <stdlib.h>     // for exit()
void __cxxtest_sig_handler( int, siginfo_t*, void* ) _CXXTEST_NO_INSTR;
void __cxxtest_sig_handler( int signum, siginfo_t* /*info*/, void* /*arg*/ )
{
    const char* msg = "run-time exception";
    switch ( signum )
    {
        case SIGFPE:
            msg = "SIGFPE: floating point exception (div by zero?)";
            // Currently, can't get cygwin g++ to pass in info,
            // so we can't be more specific.
            break;
        case SIGSEGV:
            msg = "SIGSEGV: segmentation fault (null pointer dereference?)";
            break;
        case SIGILL:
            msg = "SIGILL: illegal instruction "
                "(dereference uninitialized or deleted pointer?)";
            break;
        case SIGTRAP:
            msg = "SIGTRAP: trace trap";
            break;
#ifdef SIGEMT
        case SIGEMT:
            msg = "SIGEMT: EMT instruction";
            break;
#endif
        case SIGBUS:
            msg = "SIGBUS: bus error "
                "(dereference uninitialized or deleted pointer?)";
            break;
        case SIGSYS:
            msg = "SIGSYS: bad argument to system call";
            break;
        case SIGABRT:
            msg = "SIGABRT: execution aborted "
                "(failed assertion, corrupted heap, or other problem?)";
            break;
    }
    if ( !CxxTest::__cxxtest_assertmsg.empty() )
    {
        CxxTest::__cxxtest_sigmsg = CxxTest::__cxxtest_assertmsg;
        CxxTest::__cxxtest_assertmsg = "";
    }
    else if ( CxxTest::__cxxtest_sigmsg.empty() )
    {
        CxxTest::__cxxtest_sigmsg = msg;
    }
    else
    {
        CxxTest::__cxxtest_sigmsg = std::string(msg)
            + ", maybe related to " + CxxTest::__cxxtest_sigmsg;
    }
#ifdef CXXTEST_TRACE_STACK
    {
        std::string trace = CxxTest::getStackTrace();
        if ( trace.length() )
        {
            CxxTest::__cxxtest_sigmsg += "\\n";
            CxxTest::__cxxtest_sigmsg += trace;
        }
    }
#endif
    if ( CxxTest::__cxxtest_jmppos >= 0 )
    {
        siglongjmp( CxxTest::__cxxtest_jmpbuf[CxxTest::__cxxtest_jmppos], 1 );
    }
    else
    {
        std::cout << "\\nError: untrapped signal:\\n"
            << CxxTest::__cxxtest_sigmsg
            << "\\n"; // std::endl;
        exit(1);
    }
}

EOF
}

sub writeSigRegistration()
{
    if ( !$trapSignals ) { return; }
    print <<EOF;
    struct sigaction act;
    // act.sa_handler = __cxxtest_sig_handler;
    // act.sa_flags = 0;
    act.sa_sigaction = __cxxtest_sig_handler;
    act.sa_flags = SA_SIGINFO;
    sigaction( SIGSEGV, &act, 0 );
    sigaction( SIGFPE,  &act, 0 );
    sigaction( SIGILL,  &act, 0 );
    sigaction( SIGBUS,  &act, 0 );
    sigaction( SIGABRT, &act, 0 );
    sigaction( SIGTRAP, &act, 0 );
#ifdef SIGEMT
    sigaction( SIGEMT,  &act, 0 );
#endif
    sigaction( SIGSYS,  &act, 0 );
EOF
#    for ( int sig = 1; sig < NSIG; sig++ )
#    {
#        if (    sig != SIGABRT
#             && sig != SIGTERM
#             && sig != SIGINT
#             && sig != SIGHUP )
#            sigaction( sig, &act, 0 );
#    }
}

sub writeMain() {
  # Set up the g++-style error handling for signals
  writeSigHandler();
  if ( defined $errorPrinterFile )
  {
      print "#include <fstream>\n";
  }

  writeChkPtrErrorHandler();

  if ( $gui ) {
    print "int main( int argc, char *argv[] ) {\n";
    writeSigRegistration();
    $noStaticInit &&
      print " CxxTest::initialize();\n";
    print " return CxxTest::GuiTuiRunner<CxxTest::$gui, CxxTest::$runner>( argc, argv ).run();\n";
    print "}\n";
  }
  elsif ( $runner ) {
    if (  !$forceMain &&
          ( -f "main.cpp" || -f "Main.cpp" || -f "MAIN.cpp"
            || -f "main.CPP" || -f "Main.CPP" || -f "MAIN.CPP" ) )
    {
      print "class CxxTestMain {\npublic:\n    CxxTestMain() {\n";
      writeSigRegistration();
      print "    std::cout << ";
      print "\"Running all tests before main() ...\\n\"\n";
      print "\"----------------------------------------\\n\";\n";
      $noStaticInit &&
        print "      CxxTest::initialize();\n";

      print " ChkPtr::__manager.setErrorHandler(&__cxxtest_chkptr_error_handler);\n";
      print " ChkPtr::__manager.setReportAtEnd(true);\n";

      if ( defined $errorPrinterFile )
      {
          print " std::ofstream resultFile(\"$errorPrinterFile\");\n";
          print " int result = CxxTest::$runner(resultFile).run();\n";
          print " resultFile.close();\n";
          print " return result;\n";
      }
      else
      {
          print " return CxxTest::$runner().run();\n";
      }
      print "      std::cout << \"\\n... now starting main()\\n\"\n";
      print "\"----------------------------------------\\n\";\n";
      print "    }\n};CxxTestMain cxxTestMain __attribute__((init_priority(65535)));\n";
    }
    else
    {
      print "int main() {\n";
      writeSigRegistration();
      $noStaticInit &&
        print " CxxTest::initialize();\n";

      print " ChkPtr::__manager.setErrorHandler(&__cxxtest_chkptr_error_handler);\n";
      print " ChkPtr::__manager.setReportAtEnd(true);\n";

      if ( defined $errorPrinterFile )
      {
          print " std::ofstream resultFile(\"$errorPrinterFile\");\n";
          print " int result = CxxTest::$runner(resultFile).run();\n";
          print " resultFile.close();\n";
          print " return result;\n";
      }
      else
      {
          print " return CxxTest::$runner().run();\n";
      }
      print "}\n";
    }
  }
}
