#!/usr/bin/env python

# unit_test.py 
# Part of Automated Assessment Project. 
# This script takes a student's submitted
#  c program file (src_file.c) and modifies it to add unit test capability, storing
#  the resulting c file in dst_file.c. 
# This script then adds unit tests from another file, compiles, and runs.
#
# by James DeFilippo (jms.defilippo@gmail.com)
# with Mark Sherman (msherman@cs.uml.edu)

from subprocess import call
from subprocess import check_call
import os.path

# Check for the existance of the destination file 
# TODO There may be a better (safer) way to do this

if os.path.exists('dst_file'): 
    dst_file = open('dst_file.c','r+')
else: 
    dst_file = open('dst_file.c', 'w')

# Prepare destination file: clear it, add unit test include
dst_file.truncate()
dst_file.write('#include "minunit.h"\n')

# Read in src_file one line at a time, copying to dst_file
# Replaces 'main' with 'student_main' as main func will be provided my testing system

src_file = open('src_file.c', 'r+')
for line in src_file: 
    line = line.replace('main', 'student_main')
    dst_file.write(line)

# Read in specific unit test for that module 
dst_file.write('\nint tests_run = 0;\n') 
test_modules = open('test_modules', 'r') 
test_modules_content = test_modules.read()
dst_file.write(test_modules_content) 

# Read in test template, copy to dst_file
# Template provides standard setup functions for testing system, including the new main
template_file = open('template_file', 'r+')
template_file_content = template_file.read()
dst_file.write(template_file_content)

# Done using files, close them up.
dst_file.close()
src_file.close()
test_modules.close()
template_file.close()

# Compile and run, running all unit tests.
build_command = ['gcc','-Wall','-ansi', 'dst_file.c']
run_command = ['./a.out']
check_call(build_command)
check_call(run_command) 

