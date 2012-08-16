To add the appropriate CxxTest/Dereferee distribution to this plug-in,
you need to perform the following steps:

1) Check out the "CxxTest+Dereferee Master" module from the Web-CAT
   CVS repository.
   
2) Run the Ant build.xml script at the top-level of that module to
   generate the "cxxtest-combined" distribution, which is designed to
   be dropped into this plugin.
   
3) Move the "cxxtest-combined" folder into this project's "src"
   folder, and rename it to "cxxtest".  Your directory tree should
   look like this:
   
   CppTddPlugin project
     |- src/
     .  |- cxxtest/
     .  .  |- include/
     .  .  |  |-- cxxtest/...
        .  |  |-- dereferee/...
           |  |-- chkptr.h
           |  \-- dereferee.h
           \- cxxtestgen.jar
