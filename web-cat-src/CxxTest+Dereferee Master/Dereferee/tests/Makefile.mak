!include ../Nmake.config

OBJS	= $(BUILD)/test_main.obj
OBJS	= $(OBJS) $(BUILD)/test_oriented_listener.obj
OBJS	= $(OBJS) $(BUILD)/test_runner.obj
OBJS	= $(OBJS) $(BUILD)/test_suite.obj

RUNTESTS = run-tests

BUILD_ARTIFACT = $(BUILD)/$(RUNTESTS).exe
PDB_ARTIFACT = $(BUILD)/$(RUNTESTS).pdb

CPPFLAGS = $(CPPFLAGS) /DDEBUG /Zi /Fd$(PDB_ARTIFACT)

$(RUNTESTS).exe: $(OBJS)
	$(LINK) $(LINKFLAGS) /DEBUG /pdb:$(PDB_ARTIFACT) /out:$(BUILD_ARTIFACT) $(OBJS) $(BUILD_LISTENER) $(BUILD)/dereferee.lib
