include ../Make.config

SRCS	 = demo_main.cpp
SRCS	+= demo_framework.cpp

OBJS	 = $(patsubst %.cpp,%.o,$(SRCS))

RUNDEMO = run-demo

BUILD_LIBS_BFD = -lbfd -liberty -lintl

$(RUNDEMO): BUILD_OBJS = $(addprefix $(BUILD)/,$(OBJS))
$(RUNDEMO): BUILD_ARTIFACT = $(addprefix $(BUILD)/,$(RUNDEMO))
$(RUNDEMO): BUILD_LISTENER = $(BUILD)/$(LISTENER).o
$(RUNDEMO): BUILD_PLATFORM = $(BUILD)/$(PLATFORM).o
$(RUNDEMO): CPPFLAGS += -gstabs3 -finstrument-functions

$(RUNDEMO): $(OBJS)
ifeq ($(PLATFORM),gcc_bfd_platform)
	$(CC) -o $(BUILD_ARTIFACT) $(BUILD_OBJS) $(BUILD_LISTENER) $(BUILD_PLATFORM) $(BUILD)/libdereferee.a $(BUILD_LIBS_BFD)
else
	$(CC) -o $(BUILD_ARTIFACT) $(BUILD_OBJS) $(BUILD_LISTENER) $(BUILD_PLATFORM) $(BUILD)/libdereferee.a
endif
