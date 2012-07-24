## /usr/bin/env python 

# unit_test.py 
# James DeFilippo 
# Automated Assessment Project 

#import os 

from subprocess import call
from subprocess import check_call

###preformat dst_file 
dst_file = open('dst_file.c','r+')
dst_file.truncate()
### create a new file called 'copy data'
dst_file.write('#include "minunit.h"\n')

###read in src_file one line at a time, replace main with student_main and write to dst_file
### rename student main to prevent duplicate mains
src_file = open('src_file.c', 'r+')
for line in src_file: 
    line = line.replace('main', 'student_main')
    dst_file.write(line)


###read in specific unit test for that module 
dst_file.write('\nint tests_run = 0;\n') 
test_modules = open('test_modules', 'r') 
test_modules_content = test_modules.read()
dst_file.write(test_modules_content) 

###read in minunit template to execute unit tests 
### templates provides standard setup functions for testing system, including the new main
template_file = open('template_file', 'r+')
template_file_content = template_file.read()
dst_file.write(template_file_content)


### compile the dst_file generated from src_file.c with unit tests attached
#build_command = ['gcc','-Wall','-ansi', 'dst_file.c']
#call(build_command)
#call('./a.out') 

check_call('make', shell=True)
check_call('./a.out',shell=True)

