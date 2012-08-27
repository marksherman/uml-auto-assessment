#! /usr/bin/env python 

#---------------------------------
# substringcomparisonmodule.py
# Version Number: 1.1.0 
# Last Revision: 8/8/2012
# by James DeFilippo (jms.defilippo@gmail.com)
# as part of a project under the supervision of Mark Sherman, Professor Fred Martin, and Professor Sarita Bassil 
#---------------------------------


 

import os
import sys
from subprocess import call
import StringIO 
import ConfigParser
import glob
import shutil 
import re
from subprocess import call
from decimal import *

DEBUG = 1


stdout_output = " " 
error_msg = []
execute_command = []
execute_command_string = " "


def get_assignment_Number ( assignment ): 
    # Transforms p as a regular expression into an object that represents a regular expression.
    p = re.compile( 'p' ) 
    # Substitute '' for any instance of the regular expression found in assignment.
    assignment_Number = p.sub('', assignment)
    # Make sure assignment_Number is a string.
    assignment_Number = str(assignment_Number)
    return assignment_Number

def element_is_substring(s):
     # Create a null list for storing new values created by function.
     s_sanitized = []
     # Cycle through s element-by-element. If any element happens to be in the substring list as defined in the substring module, append to the new list we just created. Once cycling is complete, return the new list. 
     for element in s:
         if element in configuration.substrings:
            s_sanitized.append(element)
     return s_sanitized

def compare_substring(reference_output_string, student_output_string, substring): 
    
    # Make all characters in the strings lowercase.
    reference_output_string = reference_output_string.lower()
    student_output_string = student_output_string.lower()

    # Perform white-space reduction on the ends of the string.
    reference_output_string = reference_output_string.strip()
    student_output_string = student_output_string.strip()

    reference_output_string = reference_output_string.strip("?!.")
    student_output_string = student_output_string.strip("!?.")
    # Split the string into a list of substrings using the blank space as a delimiter (the default option).
    reference_output_split = reference_output_string.split()
    student_output_split = student_output_string.split()

    # Initialize two empty lists.
    reference_output = []
    student_output = []

   
    # Process each element of the list and strip it of punctuation. 
    for each_element in reference_output_split: 
        each_element = str(each_element)
        tmp = each_element.strip(":?!.")
        reference_output.append(tmp)
    
    # Process each element of the list and strip it of punctuation. 
    for each_element in student_output_split: 
        each_element = str(each_element)
        tmp = each_element.strip(":?!.")
        student_output.append(tmp)
    

    if DEBUG > 0 :
        print "Reference Output:"
        print reference_output
        print "Student Output:"
        print student_output
    
    reference_output_sanitized = element_is_substring(reference_output)
    student_output_sanitized = element_is_substring(student_output)

    if DEBUG > 0 :
        print "Reference Output Sanitized:"
        print reference_output_sanitized
        print "Student Output Sanitized:"
        print student_output_sanitized

    #  Compare the two ordered lists of elements.
    if reference_output_sanitized == student_output_sanitized :
        if DEBUG > 0 :            
            print "Success!" 
        return 0
    else:
        if DEBUG > 0:                       
            print "Failure!"
        return 1

# A sectionless configuration file is passed to WebCAT as the second argument to the command line. ConfigParser can only read configuration files with section headers. To get aroudn this problem, the configuration is read in as a string with an arbitrary section header prepended to it.
initial_string = '[section]\n' + open(sys.argv[1], 'r').read()
# The string is transformed into a string buffer which serves as a file-like object.
initial_filepointer = StringIO.StringIO(initial_string) 
# A raw config parser object of class ConfigParser is initialized under the name of config. 
config = ConfigParser.ConfigParser()
# The config parser object is fed the string-buffer. 
config.readfp(initial_filepointer)

# Read in relevant values from the WebCAT configuration file.
assignment = config.get('section', 'assignment')
user_Name = config.get('section', 'userName')
result_Dir = config.get('section', 'resultDir')
working_Dir = config.get('section', 'workingDir')
script_Home = config.get('section', 'scriptHome')
max_score_correctness = config.get('section', 'max.score.correctness')
# Extract assignment number from the assignment of form p[number]
assignment_Number = get_assignment_Number ( assignment )


os.chdir(script_Home + "/" + assignment)
shutil.copy('configuration.py', working_Dir)
shutil.copy('configuration.py', script_Home)
os.chdir(working_Dir)
import configuration



score_correctness = 0

#if configuration.file_flag: 
#   call("ln -fs -T " + count_str + "testdata" + assignment_Number + " testdata" + assignment_Number, shell=True)

def get_execute_command (stdin, args, count): 
    execute_command.append("echo")
    execute_command.append(stdin)
    execute_command.append("|")
    execute_command.append(args)
    execute_command.append(">>")
    execute_command.append(str(count) + user_Name + ".out")
    return " ".join(execute_command)


def run_tests():
   # All references to stdout_output in the function refere to the variable defined at the beginning of the script. 
   global stdout_output 
   global execute_command
   global execute_command_string 
   number_of_test_cases = configuration.number_of_test_cases
   tests = configuration.tests
   reference_output_string = configuration.reference_output_string
   count_pass = 0
   temp = 0
   count = 0

   for stdin, args, reference_output_string in tests:

       execute_command = []
       execute_command_string = " "
       count = count + 1
       
       #if os.path.exists( str(count) + user_Name + ".out" ):
       #     student_file = open(str(count) + user_Name + ".out", 'r+')
       #else: 
       #     student_file = open(str(count) + user_Name + ".out", 'w')
       #     student_file = open(str(count) + user_Name + ".out", 'r+')

       execute_command_string = get_execute_command(stdin, args, count)
       call(execute_command_string, shell=True)
       

       student_output = open(str(count) + user_Name + ".out")
       student_output_string = student_output.read()
       
       temp = compare_substring(reference_output_string, student_output_string, configuration.substrings)
       if (temp == 0):
           count_pass = count_pass + 1
       else:
           pass

   if (count_pass != number_of_test_cases): 
        stdout_output = "Your submission succeeded for " + str(count_pass) + " of " + str(configuration.number_of_test_cases) + " test cases." 
   else:
        stdout_output = "Congrats! Your submission succeeded for all " + str(count_pass) + " test cases."
   score_correctness = ( Decimal(count_pass) / Decimal(number_of_test_cases) ) * Decimal(max_score_correctness)











compile_command = configuration.compile_command

if (call(compile_command)) == 0:
    run_tests()
else:
    stdout_output = 'Compile errors found. Tests not executed.'







# Create a compile.log which contains arbitrarary XML as part of a feedback. If it does not exist, touch the file and then open it with read-write permissions.
compile_log = result_Dir + "/" + "compile.log"
compile_log_handle = open(compile_log, 'w') 
compile_log_handle = open(compile_log, 'r+')

# Create a table which contains feedback information. 
compile_log_handle.write('<div class="shadow"><table><tbody>\n<tr><th>\nFeedback</th></tr>\n<tr><td><pre>\n')
compile_log_handle.write(stdout_output)
compile_log_handle.write('</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>')

# ---
# Write changes to the config file. This config file will then write these changes to the grading.properties file found in the Results section.
# ---

# Read the contents of the configuration file into a string.
config_file = open(sys.argv[1]).read()
# Find the instance numReports and change its value as specified.
config_file = config_file.replace('numReports=0', 'numReports=1')
# Open the configuration file for writing.
config_file_write = open(sys.argv[1], 'w')
# Write the contents of the newly-created string into the configuration file.
config_file_write.write(config_file)
# Close the configuration file.
config_file_write.close()

# Open the file for appending. 
config_file = open(sys.argv[1], 'a+b').write('disableCodeCoverage=1\nexec.timeout=6000\nreport1.file=compile.log\nreport1.mimeType=text/html\nnumCodeMarkups=0\nscore.correctness=')
config_file = open(sys.argv[1], 'a+b').write(str(score_correctness))
config_file = open(sys.argv[1], 'a+b').write('\nscore.tools=0')

