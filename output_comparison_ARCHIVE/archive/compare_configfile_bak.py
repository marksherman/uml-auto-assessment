#! /usr/bin/env python 


import sys
import config 
#import compare_substring_functional
#import compare_literal_functional 
#import compare_numeric_functional 



if config.mode == 1: 
    compare_literal(config.reference_file, config.student_file) 
if config.mode == 2: 
    compare_numeric(config.reference_file, config.student_file)
if config.mode == 3: 
    compare_substring(config.reference_file, config.student_file)

#if ((mode != "1") or (mode != "2") or (more != "3")): 
#    print "Check that you have entered a correct value for mode." 


