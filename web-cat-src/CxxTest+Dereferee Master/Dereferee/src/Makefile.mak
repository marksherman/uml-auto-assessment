!include ../Nmake.config

OBJS	 = $(BUILD)/allocation_info_impl.obj
OBJS	 = $(OBJS) $(BUILD)/manager.obj
OBJS	 = $(OBJS) $(BUILD)/memtab.obj
OBJS	 = $(OBJS) $(BUILD)/usage_stats_impl.obj

PDB_ARTIFACT = $(BUILD)/dereferee.pdb
BUILD_ARTIFACT = $(BUILD)/dereferee.lib
BUILD_LISTENER = $(BUILD)/$(LISTENER).obj
BUILD_PLATFORM = $(BUILD)/$(PLATFORM).obj

CPPFLAGS = $(CPPFLAGS) /Zi /Fd$(PDB_ARTIFACT)

dereferee.lib: $(OBJS)
	$(LIBEXE) $(LIBFLAGS) /out:$(BUILD_ARTIFACT) $(OBJS) $(BUILD_LISTENER) $(BUILD_PLATFORM)
