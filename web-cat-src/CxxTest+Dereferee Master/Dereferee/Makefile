include Make.config

DEMODIR			= demo
SRCDIR			= src
LISTENERSDIR	= listeners
PLATFORMSDIR	= platforms
TESTSDIR		= tests

#
# We don't make the demo by default, since it requires that a listener and
# a platform be specified as variables on the command line.
#
SUBDIRS = $(LISTENERSDIR) $(PLATFORMSDIR) $(SRCDIR) $(TESTSDIR)


all: build-dir listener platform libs tests


build-dir:
	@mkdir -p build

	
libs: build-dir listener platform
ifndef LISTENER
	@echo "WARNING: Creating Dereferee static library without a listener!"
else
	export LISTENER
endif
ifndef PLATFORM
	@echo "WARNING: Creating Dereferee static library without a platform!"
else
	export PLATFORM
endif
	@cd $(SRCDIR) ; $(MAKE)


demo: libs
ifndef LISTENER
	@echo "ERROR: Please define the LISTENER variable as the name of one of the"
	@echo "listener object files, without the extension (for example,"
	@echo "LISTENER=stdio_listener), when building the demo."
	exit
endif
ifndef PLATFORM
	@echo "ERROR: Please define the PLATFORM variable as the name of one of the"
	@echo "platform object files, without the extension (for example,"
	@echo "PLATFORM=gcc_macosx_platform), when building the demo."
	exit
endif
	export LISTENER
	export PLATFORM
	@cd $(DEMODIR) ; $(MAKE)
	

listener: build-dir
	@cd $(LISTENERSDIR) ; $(MAKE)


platform: build-dir
	@cd $(PLATFORMSDIR) ; $(MAKE)


tests: build-dir
	@cd $(PLATFORMSDIR) ; $(MAKE)
	@cd $(SRCDIR) ; $(MAKE)
	@cd ..
	@cd $(TESTSDIR) ; $(MAKE)
	

clean:
	rm -rf build


run-tests: tests
	@echo
	@echo Running tests...
	@echo ================
	@build/run-tests


run-demo: demo
	@echo
	@echo Running demo...
	@echo ================
	@build/run-demo
