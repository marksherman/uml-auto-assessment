!include Nmake.config

DEMODIR			= demo
SRCDIR			= src
LISTENERSDIR	= listeners
PLATFORMSDIR	= platforms
TESTSDIR		= tests

#
# We don't make the demo by default, since it requires that a listener and
# a platform be specified as variables on the command line.
#

all: libs listeners platforms tests

build-dir:
	@if not exist "build-msvc" mkdir "build-msvc"
	
libs: build-dir listeners platforms
!ifndef LISTENER
	set LISTENER=msvc_debugger_listener
!endif
!ifndef PLATFORM
	set PLATFORM=msvc_win32_platform
!endif
	cd $(SRCDIR)
	$(MAKE) /nologo /F Makefile.mak
	cd ..

demo: libs listeners platforms build-dir
	cd $(DEMODIR)
	$(MAKE) /nologo /F Makefile.mak
	cd ..
	
listeners: build-dir
	cd $(LISTENERSDIR)
	$(MAKE) /nologo /F Makefile.mak
	cd ..

platforms: build-dir
	cd $(PLATFORMSDIR)
	$(MAKE) /nologo /F Makefile.mak
	cd ..

tests: build-dir
	cd $(TESTSDIR)
	$(MAKE) /nologo /F Makefile.mak
	cd ..
	
clean:
	erase /Q build-msvc

run-tests: all
	@echo
	@echo Running tests...
	@echo ================
	@cd build-msvc
	@run-tests.exe
	@cd ..

run-demo: demo
	@echo
	@echo Running demo...
	@echo ================
	@cd build-msvc
	@run-demo.exe
	@cd ..
