!include ../Nmake.config

OBJS	 = $(BUILD)/demo_main.obj
OBJS	 = $(OBJS) $(BUILD)/demo_framework.obj

RUNDEMO = run-demo

BUILD_ARTIFACT = $(BUILD)/$(RUNDEMO).exe
PDB_ARTIFACT = $(BUILD)/$(RUNDEMO).pdb

CPPFLAGS = $(CPPFLAGS) /Zi /Fd$(PDB_ARTIFACT)

$(RUNDEMO).exe: $(OBJS)
	$(LINK) $(LINKFLAGS) /DEBUG /pdb:$(PDB_ARTIFACT) /out:$(BUILD_ARTIFACT) $(OBJS) $(BUILD)/dereferee.lib
