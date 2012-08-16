include ../Make.config

ifndef PLATFORM
SRCS     = empty_platform.cpp
else
SRCS	 = $(PLATFORM).cpp
endif

OBJS	 = $(patsubst %.cpp,%.o,$(SRCS))

all: $(OBJS)
