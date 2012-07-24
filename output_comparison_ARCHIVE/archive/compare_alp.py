#! /usr/bin/env python 
import sys

DEBUG = 1

reference_file = open(sys.argv[1], 'r')
student_file = open(sys.argv[2], 'r')

reference_output_string = reference_file.read().lower()
student_output_string = student_file.read().lower()

if DEBUG > 0 :
    print reference_output_string
    print student_output_string

def str_is_alphanum(s): return s.isalpha() or s.isdigit()

# Filter out non-alpha-numeric characters. Gets rid of punctuation.
reference_output_sanitized = filter(str_is_alphanum, reference_output_string)
student_output_sanitized = filter(str_is_alphanum, student_output_string)

if DEBUG > 0 :
    print reference_output_sanitized
    print student_output_sanitized

if reference_output_sanitized == student_output_sanitized :
    print "PASS" 
else:
    print "FAIL"
