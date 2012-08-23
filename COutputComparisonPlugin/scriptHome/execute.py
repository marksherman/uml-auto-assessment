#! /usr/bin/env python 

#---------------------------------
# substringcomparisonmodule.py
# Version Number: 1.0.0 
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
from decimal import * 


DEBUG = 0
DEBUG_ADDITIONAL = 1

stdout_output = " " 

# Assignments come in the form 'px' where x is some number. The function extracts x from the compound expression.
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
         if element in substring.substring:
            s_sanitized.append(element)
     return s_sanitized

def compare_numeric(reference_file, student_file, substring): 

    # Create strings which represent instructor and student output respectively.    
    reference_output_string = reference_file.read()
    student_output_string = student_file.read()
    
    # Make all characters in the strings lowercase.
    reference_output_string = reference_output_string.lower()
    student_output_string = student_output_string.lower()

    # Perform white-space reduction on the ends of the string.
    reference_output_string = reference_output_string.strip()
    student_output_string = student_output_string.strip()

    #reference_output_string = reference_output_string.strip("?!.")
    #student_output_string = student_output_string.strip("!?.")
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
        if DEBUG_ADDITIONAL > 0 :            
            print "Success!" 
        return 1
    else:
        if DEBUG_ADDITIONAL > 0:                       
            print "Failure!"
        return 0

# A sectionless configuration file is passed to WebCAT as the second argument to the command line. ConfigParser can only read configuration files with section headers. To get aroudn this problem, the configuration is read in as a string with an arbitrary section header prepended to it.
initial_string = '[section]\n' + open(sys.argv[1], 'r').read()
# The string is transformed into a string buffer which serves as a file-like object.
initial_filepointer = StringIO.StringIO(initial_string) 
# A raw config parser object of class ConfigParser is initialized under the name of config. 
config = ConfigParser.RawConfigParser()
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

# Jump to assignment directory and extract relevant files from that directory to the working directory.

#os.chdir(script_Home)
#shutil.copy('substring.py', working_Dir)

os.chdir(script_Home + "/" + assignment) 
for each_file in glob.glob('*reference.out'): 
    shutil.copy(each_file, working_Dir)
for each_file in glob.glob('*casedata'): 
    shutil.copy(each_file, working_Dir)
for each_file in glob.glob('*testdata*'):
    shutil.copy(each_file, working_Dir)
for each_file in glob.glob('*testdata'): 
    shutil.copy(each_file, working_Dir)
for each_file in glob.glob('*arguments'): 
    shutil.copy(each_file, working_Dir)
for each_file in glob.glob('*.h'): 
    shutil.copy(each_file, working_Dir)
#shutil.copy('substring.py', working_Dir)
shutil.copy('substring.py', script_Home)
# Jump back to working_dir to continue compilation.
os.chdir(working_Dir)      
import substring


# Math flag is contained in the substring module just imported. Options need to be altered to request compilation.
if (substring.math_flag == 1): 
        compile_command = ["gcc", "-Wall", "-lm", assignment + ".c"]
else: 
        compile_command = ["gcc", "-ansi", "-Wall", assignment + ".c"] 
call(compile_command) 

count = 0
count_pass = 0
temp = 0


# The number of reference.out exclusively determines the number of test cases.
for each_file in glob.glob('*reference.out'):
    if each_file.endswith("reference.out"): 
           # Increment count for each instance of *reference.out. Convert the numeric object into string for later use in filenames. 
           count = count + 1
           count_str = str(count) 
           # For each reference test file, create a student test file.
           if os.path.exists( count_str + user_Name + ".out" ):
               student_file = open(count_str + user_Name + ".out", 'r+')
           else: 
               student_file = open(count_str + user_Name + ".out", 'w')
               student_file = open(count_str + user_Name + ".out", 'r+')
           instructor_file = open( count_str + "reference.out", 'r')  
           # There are four possibilities. Each problem has a CaseData file. Not every problem, however, has arguments or testdata. 
           if ((os.path.exists( count_str + "testdata" + assignment_Number )) and (os.path.exists( count_str + "arguments" ))): 
               print "Signal1!"
               # Open the casedata file and read its contents into a string.
               case_temp = open(count_str + "casedata", "r") 
               case_temp_string = case_temp.read() 
               # The file likely has an command to open "testdata28." But all testdata files are enumerated by their relation to their respective reference.out. So we must create a symbolic link to testdata28. 
               os.system("ln -fs -T " + count_str + "testdata" + assignment_Number + " testdata" + assignment_Number)
               # Open the arguments file and reads its contents into a string.
               arguments_temp = open(count_str + "arguments", "r")
               arguments_temp_string = arguments_temp.read()
               # Echo any input values to the executable file generated by compilation, applying any arguments and piping all output of program executation to the student file.
               os.system("echo \"" + case_temp_string + "\" | ./a.out " + arguments_temp_string + ">>" + count_str + user_Name + ".out")
           elif ((os.path.exists( count_str + "testdata" + assignment_Number )) and (not (os.path.exists( count_str + "arguments" )))): 
               print "Signal2!"
               # Open the casedata file and read its contents into a string.
               case_temp = open( count_str + "casedata", "r")
               case_temp_string = case_temp.read() 
               os.system("ln -fs -T " + count_str + "testdata" + assignment_Number + " testdata" + assignment_Number)
               # Echo any input values to the executable file generated by compilation, applying any arguments and piping all output of program executation to the student file.
               os.system("echo \"" + case_temp_string + "\" | ./a.out >> " + count_str + user_Name + ".out")
           elif ((not (os.path.exists( count_str + "testdata" + assignment_Number ))) and (os.path.exists( count_str + "arguments" ))): 
               print "Signal3!"
               # Open the casedata file and read its contents into a string.
               case_temp = open( count_str + "casedata", "r")
               case_temp_string = case_temp.read()    
               # Open the arguments file and reads its contents into a string.
               arguments_temp = open(count_str + "arguments", "r")
               arguments_temp_string = arguments_temp.read()    
               os.system("echo " + case_temp_string + " | ./a.out " + arguments_temp_string + " >> " + count_str + user_Name + ".out")
           else:
               print "Signal4!"
               # Open the casedata file and read its contents into a string.
               case_temp = open( count_str + "casedata", "r") 
               case_temp_string = case_temp.read()
               print case_temp_string
               #os.system("echo \"" + case_temp_string + "\" | ./a.out >> " + count_str + user_Name + ".out")
               os.system("echo " + case_temp_string + " | ./a.out >> " + count_str + user_Name + ".out")
           temp = compare_numeric(instructor_file, student_file, substring)
           # temp will return 1 if the the two files generate equivalent substring lists, otherwise zero. count_pass is incremented in the case of a successful match.
           count_pass = count_pass + temp
           student_file.close()
           instructor_file.close() 

if count_pass != count: 
    stdout_output = "Your submission succeeded for " + str(count_pass) + " of " + str(count) + " test cases."
if count_pass == count: 
    stdout_output = "Congrats! Your submission succeeded for all " + str(count) + " test cases."

if DEBUG > 0: 
    print stdout_output

if (count == 0): 
    print "No tests were performed." 
    score_correctness = 0
else: 
    # Convert to Decimal form to prevent truncation.
    score_correctness = ( Decimal(count_pass) / Decimal(count) ) * Decimal(max_score_correctness)

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

