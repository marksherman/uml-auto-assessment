include ../Make.config

SRCS	 = test_main.cpp
SRCS	+= test_oriented_listener.cpp
SRCS	+= test_runner.cpp
SRCS	+= test_suite.cpp

OBJS	 = $(patsubst %.cpp,%.o,$(SRCS))

RUNTESTS = run-tests

$(RUNTESTS): BUILD_OBJS = $(addprefix $(BUILD)/,$(OBJS))
$(RUNTESTS): BUILD_ARTIFACT = $(addprefix $(BUILD)/,$(RUNTESTS))

$(RUNTESTS): $(OBJS)
	$(CC) -o $(BUILD_ARTIFACT) $(BUILD_OBJS) $(BUILD)/libdereferee.a
