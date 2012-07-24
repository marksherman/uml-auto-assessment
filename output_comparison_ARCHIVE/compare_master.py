#! /usr/bin/env python 

### Import Libraries

import sys
import os
import config # config file must be in same directory 

### Literal Comparison Test 

def compare_literal(reference_file, student_file): 
    DEBUG = 1
    reference_output_string = reference_file.read()
    student_output_string = student_file.read()

    if DEBUG > 0 :
        print reference_output_string
        print student_output_string

    split_reference = reference_output_string.split(" ")
    split_student = student_output_string.split(" ")

    if split_reference == split_student : 
        return 1
    else: 
        return 0 
    
### Numeric Comparison Test

def str_is_digit(s): return s.isdigit()

def compare_numeric(reference_file, student_file): 

    DEBUG = 0
    reference_output_string = reference_file.read()
    student_output_string = student_file.read()
    
    reference_output = reference_output_string.split() 
    student_output = student_output_string.split()

    if DEBUG > 0 :
        print reference_output
        print student_output
        
    reference_output_sanitized = filter(str_is_digit, reference_output)
    student_output_sanitized = filter(str_is_digit, student_output)

    if DEBUG > 0 :
        print reference_output_sanitized
        print student_output_sanitized

    if reference_output_sanitized == student_output_sanitized :
        return 1
    else:
        return 0
    

### Substring Comparison Test

def compare_sub(reference_file, student_file): 
    def reference_search(sub_string): 
        if reference_output_string.find(sub_string) == -1 :
            return 0
        else :
            return 1
    def student_search(sub_string): 
        if student_output_string.find(sub_string) == -1 :
            return 0
        else :
            return 1
    DEBUG = 0 
    reference_output_string = reference_file.read().lower()
    student_output_string = student_file.read().lower()
    if DEBUG > 0 :
        print reference_output_string
        print student_output_string
    reference_substrings_found = map(reference_search, config.substrings)
    student_substrings_found = map(student_search, config.substrings)
    if reference_substrings_found  == student_substrings_found  :
        return 1
    else:
        return 0


### Main Program

count = 0
count_pass = 0
temp = 0
directory = os.getcwd() 
for root,dirs,files in os.walk(directory):
    for file in files:
       if file.endswith(config.student_name + ".out"): 
           count = count + 1
           count_str = str(count) 
           student_file = open( count_str + config.student_name + ".out" ) 
           instructor_file = open( count_str + config.instructor_name + ".out") 
           if config.mode == 1: 
              temp = compare_literal(instructor_file, student_file)
              count_pass = count_pass + temp
           if config.mode == 2: 
              temp = compare_numeric(instructor_file, student_file)
              count_pass = count_pass + temp
           if config.mode == 3: 
              temp = compare_sub(instructor_file, student_file)
              count_pass = count_pass + temp

### Display Results
     
if count_pass != count:           
    print 'Your submission succeeded for %d' % count_pass
    print 'of %d test cases.' % count
if count_pass == count: 
    print 'Congrats! Your submission successed for all %d test cases.' % count
