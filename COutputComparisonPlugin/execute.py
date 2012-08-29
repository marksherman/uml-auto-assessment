#! /usr/bin/env python 

#---------------------------------
# OutputComparisonScript
# Version Number: 1.3.0 
# Last Revision: 8/27/2012
# by James DeFilippo (jms.defilippo@gmail.com) with Mark Sherman (msherman@cs.uml.edu)
# as part of a project under the supervision of Professor Fred Martin, and Professor Sarita Bassil 
#---------------------------------

from decimal import * 
DEBUG = 0

def read_configuration_properties():
     import StringIO
     import ConfigParser
     import sys
     # A sectionless configuration file is passed to WebCAT as the second argument to the command line. ConfigParser can only read configuration files with section headers. To get aroudn this problem, the configuration is read in as a string with an arbitrary section header prepended to it.
     initial_string = '[section]\n' + open(sys.argv[1], 'r').read()
     # The string is transformed into a string buffer which serves as a file-like object.
     initial_filepointer = StringIO.StringIO(initial_string) 
     #A raw config parser object of class ConfigParser is initialized under the name of config. 
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
     return (assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness)
     
     
def get_assignment_data():  
     import os 
     import glob
     import shutil
     os.chdir(script_Home + "/" + assignment) 
     for each_file in glob.glob('*testdata*'):
          shutil.copy(each_file, working_Dir)

     for each_file in glob.glob('*testdata'): 
          shutil.copy(each_file, working_Dir)
     # TODO One of these shutil commands is unnecessary which one?
     shutil.copy('configuration.py', working_Dir)
     shutil.copy('configuration.py', script_Home)
     os.chdir(working_Dir)

def element_is_substring(s):
     import configuration
     # Create a null list for storing new values created by function.
     s_filtered = []
     # Cycle through s element-by-element. If any element happens to be in the substring list as defined in the substring module, append to the new list we just created. Once cycling is complete, return the new list. 
     for element in s:
         if element in configuration.substrings:
            s_filtered.append(element)
     return s_filtered


def sanitize_string(s):
    s = s.lower()
    s = s.strip()
    s = s.strip("?!.,:;")
    s_split = s.split()
    s_list = [] 
    for each_element in s_split: 
        each_element = str(each_element)
        tmp = each_element.strip("?!.,:;")
        s_list.append(tmp)
    return s_list

def compare_substring(reference_output_string, student_output_string): 
    reference_list = sanitize_string(reference_output_string)
    student_list = sanitize_string(student_output_string)
     
    if DEBUG > 0 :
        print "Reference Output:"
        print reference_list
        print "Student Output:"
        print student_list
    
    reference_output_filtered = element_is_substring(reference_list)
    student_output_filtered = element_is_substring(student_list)

    if DEBUG > 0 :
        print "Reference Output Filtered:"
        print reference_output_filtered
        print "Student Output Filtered:"
        print student_output_filtered

    #  Compare the two ordered lists of elements.
    if reference_output_filtered == student_output_filtered :
        if DEBUG > 0 :            
            print "Success!" 
        return 0
    else:
        if DEBUG > 0:                       
            print "Failure!"
        return 1

def get_execute_command (stdin, args, count): 
    execute_command = []
    execute_command.append("echo")
    execute_command.append(stdin)
    execute_command.append("|")
    execute_command.append(args)
    execute_command.append(">>")
    execute_command.append(str(count) + user_Name + ".out")
    return " ".join(execute_command)



def run_comparison_test(stdin, args, reference_output_string, files, messages, count, count_pass, error_list): 
       from subprocess import call
       execute_command_string = " "
       count = count + 1
       
       # Create symbolic links for data files. 
       if ((files != " ") and (files != "")): 
           call("ln -fs -T " + files, shell=True)

       execute_command_string = get_execute_command(stdin, args, count)
       call(execute_command_string, shell=True)
     

       student_output = open(str(count) + user_Name + ".out")
       student_output_string = student_output.read()
       
       if ((compare_substring(reference_output_string, student_output_string)) == 0): 
           count_pass = count_pass + 1
       else:
           error_list.append(messages)

       return (count, count_pass, error_list)

def grade_submission():
   import configuration

   number_of_test_cases = configuration.number_of_test_cases
   tests = configuration.tests
   count_pass = 0
   temp = 0
   count = 0

   error_list = [] 

   for stdin, args, reference_output_string, files, messages in tests:
       (count, count_pass, error_list) = run_comparison_test(stdin, args, reference_output_string, files, messages, count, count_pass, error_list)

   error_message = " "
   error_message = " ".join(error_list)
            
   if (count_pass != number_of_test_cases): 
        stdout_output = "Your submission succeeded for " + str(count_pass) + " of " + str(configuration.number_of_test_cases) + " test cases." 
   else:
        stdout_output = "Congrats! Your submission succeeded for all " + str(count_pass) + " test cases."
   score_correctness = ( Decimal(count_pass) / Decimal(number_of_test_cases) ) * Decimal(max_score_correctness)

   return (score_correctness, error_message, stdout_output)

def compile_student_code():
     import configuration
     from subprocess import call 
     global stdout_output
     compile_command = configuration.compile_command
     if (call(compile_command)) == 0:
          return 0 
     else:
          stdout_output = 'Compile errors found. Tests not executed.'
          return 1

#def generate_feedback_file(result_Dir, error_message): 
def generate_feedback_file(): 
     # Create a compile.log which contains arbitrarary XML as part of a feedback. If it does not exist, touch the file and then open it with read-write permissions.
     compile_log = result_Dir + "/" + "compile.log"
     compile_log_handle = open(compile_log, 'w') 
     compile_log_handle = open(compile_log, 'r+')

     # Create a table which contains feedback information. 
     compile_log_handle.write('<div class="shadow"><table><tbody>\n<tr><th>\nFeedback</th></tr>\n<tr><td><pre>\n')
     compile_log_handle.write(stdout_output + '\n')
     compile_log_handle.write(error_message)
     compile_log_handle.write('</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>')

def write_configuration_properties():  
     import sys
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


          
stdout_output = " " 

(assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness) = read_configuration_properties()

get_assignment_data()

compile_student_code()

(score_correctness, error_message, stdout_output) = grade_submission()

generate_feedback_file()
write_configuration_properties()


