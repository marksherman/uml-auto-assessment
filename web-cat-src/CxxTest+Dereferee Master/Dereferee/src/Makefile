include ../Make.config

SRCS	 = allocation_info_impl.cpp
SRCS	+= manager.cpp
SRCS	+= memtab.cpp
SRCS	+= usage_stats_impl.cpp

OBJS	 = $(patsubst %.cpp,%.o,$(SRCS))

LIBDEREFEREE = libdereferee.a

$(LIBDEREFEREE): BUILD_OBJS = $(addprefix $(BUILD)/,$(OBJS))
$(LIBDEREFEREE): BUILD_ARTIFACT = $(addprefix $(BUILD)/,$(LIBDEREFEREE))

ifdef LISTENER
$(LIBDEREFEREE): BUILD_LISTENER = $(BUILD)/$(LISTENER).o
else
$(LIBDEREFEREE): BUILD_LISTENER =
endif

ifdef PLATFORM
$(LIBDEREFEREE): BUILD_PLATFORM = $(BUILD)/$(PLATFORM).o
else
$(LIBDEREFEREE): BUILD_PLATFORM = $(BUILD)/empty_platform.o
endif

$(LIBDEREFEREE): $(OBJS)
	ar -cr $(BUILD_ARTIFACT) $(BUILD_OBJS) $(BUILD_LISTENER) $(BUILD_PLATFORM)
	ranlib $(BUILD_ARTIFACT)
