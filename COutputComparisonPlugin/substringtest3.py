#! /usr/bin/env python 

#---------------------------------
# execute.py 
# Execute.py is a
# Programmer: James DeFilippo
# As part of a project under the supervision of Mark Sherman, Professor Fred Martin, and Professor Sarita Bassil. 

import os
import sys 
from subprocess import call 
import StringIO 
import ConfigParser
import glob
import shutil 
import re
from decimal import * 
DEBUG = 1
DEBUG_ADDITIONAL = 1
stdout_output = " " 

def get_assignment_Number ( assignment ): 
    p = re.compile( 'p' )
    assignment_Number = p.sub('', assignment)
    assignment_Number = str(assignment_Number)
    return assignment_Number

def element_is_substring(s):
     s_sanitized = []
     #s_sanitized.remove("0")
     for element in s:
         if element in substring.substring:
            s_sanitized.append(element)
     return s_sanitized

def compare_numeric(reference_file, student_file, substring): 
    DEBUG = 0
    reference_output_string = reference_file.read()
    student_output_string = student_file.read()
    
    reference_output = reference_output_string.split() 
    student_output = student_output_string.split()

    if DEBUG > 0 :
        print reference_output
        print student_output
        
    #reference_output_sanitized = filter(str_is_substring, reference_output)
    #student_output_sanitized = filter(str_is_substring, student_output)

    reference_output_sanitized = element_is_substring(reference_output)
    student_output_sanitized = element_is_substring(student_output)

    if DEBUG > 0 :
        print reference_output_sanitized
        print student_output_sanitized

    if reference_output_sanitized == student_output_sanitized :
        if DEBUG_ADDITIONAL > 0 : 
            print reference_output_sanitized
            print student_output_sanitized
            print "Success!" 
        return 1
    else:
        if DEBUG_ADDITIONAL > 0: 
            print reference_output_sanitized
            print student_output_sanitized
            print "Failure!"
        return 0
    

#---
# SECTION

initial_string = '[section]\n' + open(sys.argv[1], 'r').read()
initial_filepointer = StringIO.StringIO(initial_string) 
config = ConfigParser.RawConfigParser()
config.readfp(initial_filepointer)


assignment = config.get('section', 'assignment')
user_Name = config.get('section', 'userName')
result_Dir = config.get('section', 'resultDir')
working_Dir = config.get('section', 'workingDir')
script_Home = config.get('section', 'scriptHome')
max_score_correctness = config.get('section', 'max.score.correctness')

assignment_Number = get_assignment_Number ( assignment )

count = 0 
count_pass = 0 
temp = 0 


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

shutil.copy('substring.py', working_Dir)
import substring


os.chdir(working_Dir)      
compile_command = ["gcc", "-ansi", "-Wall", assignment + ".c"] 
call(compile_command) 



for each_file in glob.glob('*reference.out'):
    if each_file.endswith("reference.out"): 
           count = count + 1
           count_str = str(count) 
           if os.path.exists( count_str + user_Name + ".out" ):
               student_file = open(count_str + user_Name + ".out", 'r+')
           else: 
               student_file = open(count_str + user_Name + ".out", 'w')
               student_file = open(count_str + user_Name + ".out", 'r+')
           instructor_file = open( count_str + "reference.out", 'r')      
           if ((os.path.exists( count_str + "testdata" + assignment_Number )) and (os.path.exists( count_str + "arguments" ))): 
               print "Signal1!"
               case_temp = open(count_str + "casedata", "r") 
               case_temp_string = case_temp.read() 
               os.system("ln -fs -T " + count_str + "testdata" + assignment_Number + " testdata" + assignment_Number)
               arguments_temp = open(count_str + "arguments", "r")
               arguments_temp_string = arguments_temp.read()
               os.system("echo \"" + case_temp_string + "\" | ./a.out " + arguments_temp_string + ">>" + count_str + user_Name + ".out")
           elif ((os.path.exists( count_str + "testdata" + assignment_Number )) and (not (os.path.exists( count_str + "arguments" )))): 
               print "Signal2!"
               case_temp = open( count_str + "casedata", "r")
               case_temp_string = case_temp.read() 
               os.system("ln -fs -T " + count_str + "testdata" + assignment_Number + " testdata" + assignment_Number)
               os.system("echo \"" + case_temp_string + "\" | ./a.out >> " + count_str + user_Name + ".out")
               #os.system("./a.out >> " + count_str + user_Name + ".out")
           elif ((not (os.path.exists( count_str + "testdata" + assignment_Number ))) and (os.path.exists( count_str + "arguments" ))): 
               print "Signal3!"
               case_temp = open( count_str + "casedata", "r")
               case_temp_string = case_temp.read()    
               arguments_temp = open(count_str + "arguments", "r")
               arguments_temp_string = arguments_temp.read()    
               # These lines need rigorous testing.
               #os.system("echo \"" + case_temp_string + "\" | ./a.out " + arguments_temp_string + " >> " + count_str + user_Name + ".out")
               os.system("echo " + case_temp_string + " | ./a.out " + arguments_temp_string + " >> " + count_str + user_Name + ".out")
           else:
               print "Signal4!"
               case_temp = open( count_str + "casedata", "r") 
               case_temp_string = case_temp.read()
               print case_temp_string
               #os.system("echo \"" + case_temp_string + "\" | ./a.out >> " + count_str + user_Name + ".out")
               os.system("echo " + case_temp_string + " | ./a.out >> " + count_str + user_Name + ".out")
           temp = compare_numeric(instructor_file, student_file, substring)
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
    score_correctness = ( Decimal(count_pass) / Decimal(count) ) * Decimal(max_score_correctness)

compile_log = result_Dir + "/" + "compile.log"
compile_log_handle = open(compile_log, 'w') 
compile_log_handle = open(compile_log, 'r+')

compile_log_handle.write('<div class="shadow"><table><tbody>\n') 
compile_log_handle.write('<tr><th>\n')
compile_log_handle.write('Feedback</th></tr>\n')
compile_log_handle.write('<tr><td><pre>\n')
compile_log_handle.write(stdout_output)
compile_log_handle.write('</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>')

config_file = open(sys.argv[1]).read()
config_file = config_file.replace('numReports=0', 'numReports=1')
config_file_write = open(sys.argv[1], 'w')
config_file_write.write(config_file)
config_file_write.close()


config_file = open(sys.argv[1], 'a+b').write('disableCodeCoverage=1\nexec.timeout=6000\nreport1.file=compile.log\nreport1.mimeType=text/html\nnumCodeMarkups=0\nscore.correctness=')
config_file = open(sys.argv[1], 'a+b').write(str(max_score_correctness))
config_file = open(sys.argv[1], 'a+b').write('\nscore.tools=0')
