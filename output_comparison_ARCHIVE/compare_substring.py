#! /usr/bin/env python 
import sys

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

def compare_sub(reference_file, student_file, substrings): 
    DEBUG = 0 
    reference_output_string = reference_file.read().lower()
    student_output_string = student_file.read().lower()
    if DEBUG > 0 :
        print reference_output_string
        print student_output_string
    reference_substrings_found = map(reference_search, substrings)
    student_substrings_found = map(student_search, substrings)
    if reference_substrings_found  == student_substrings_found  :
        print "PASS" 
    else:
        print "FAIL"
    return
