#! /usr/bin/env python 

import sys
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
        print "PASS"
    else: 
        print "FAIL"
    
    return 
