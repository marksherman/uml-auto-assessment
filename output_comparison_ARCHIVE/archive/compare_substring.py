#! /usr/bin/env python 
import sys

DEBUG = 0

# List of substrings to search for. Results will existence of said string in output.
substrings = ["is equal to zero", "is not equal to zero"]

reference_file = open(sys.argv[1], 'r')
student_file = open(sys.argv[2], 'r')

reference_output_string = reference_file.read().lower()
student_output_string = student_file.read().lower()

if DEBUG > 0 :
    print reference_output_string
    print student_output_string

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

reference_substrings_found = map(reference_search, substrings)
student_substrings_found = map(student_search, substrings)

if DEBUG > 0 :
    print reference_substrings_found 
    print student_substrings_found 

if reference_substrings_found  == student_substrings_found  :
    print "PASS" 
else:
    print "FAIL"
