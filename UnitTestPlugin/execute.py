#! /usr/bin/env python

#-----------------------------------------
# UnitTestScript 
# Version Number: 1.0.0 
# Last Revision: 8/28/2012 
# by James DeFilippo (jms.defilippo@gmail.com) with Mark Sherman (msherman@cs.uml.edu)
# as part of a project under the supervision of Professor Fred Martin and Professor Sarita Bassil
#-----------------------------------------

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
     os.chdir(script_Home + "/" + assignment) 
     shutil.copy('configuration.py', working_Dir)
     os.chdir(script_Home)
     shutil.copy('minunit.h', working_Dir)
     os.chdir(working_Dir)

def copy_src_to_destination_file(): 
    destination_file = open("destinationfile.c", "w+")
    source_file = open(assignment + ".c", "r") 
    destination_file.write('#include "minunit.h"\n')
    # Prevent duplicate mains (see below). 
    for line in source_file: 
        line = line.replace('main', 'student_main')
        destination_file.write(line)
    source_file.close()
    return destination_file 

def create_case_functions(functions, arguments, return_values, messages, count, destination_file): 
        count = count + 1
        destination_file.write('static char * test' + str(count) + '() {\ntests_run++;\n')
        destination_file.write('mu_assert(\"') 
        destination_file.write(messages)
        destination_file.write('\", ')
        destination_file.write(functions)
        destination_file.write('(' + arguments + ') == ' + return_values + ');')
        destination_file.write('\ntests_passed++;')
        destination_file.write('\nreturn 0;}\n')
        destination_file.write('static char * run_test' + str(count) + '() { \n mu_run_test(test' + str(count) + '); \n return 0; }\n') 
        return count 

def create_test_functions(destination_file):
    import configuration 
    count = 0
    for functions, arguments, return_values, messages in configuration.tests:
        count = create_case_functions(functions, arguments, return_values, messages, count, destination_file)
    
    destination_file.write('int main() {\n FILE * fin; \n')
    for x in range(count): 
        y = x + 1
        destination_file.write('run_test')
        destination_file.write(str(y))
        destination_file.write('();\n\n')
    destination_file.write('printf("You passed %d out of %d tests run.\\n", tests_passed, tests_run);\n')
    destination_file.write('\n\nfin = fopen("log.txt", "w+");\n\n')
    destination_file.write('fprintf(fin, "[section]\\ncount_pass = %d\\ncount = %d", tests_passed, tests_run);\n')
    destination_file.write('fclose(fin);\n')
    destination_file.write('return 0;\n}')
    # Failure to close destination file will result in compilation errors. Closing all opened files is an obvious best practice.
    destination_file.close()


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
     # Muy importante.
     compile_log_handle.close()

def compile_student_code():
     import configuration
     from subprocess import call 
     global stdout_output
     compile_command = configuration.compile_command
     if (call(compile_command, shell=True)) == 0:
          return 0 
     else:
          stdout_output = 'Compile errors found. Tests not executed.'
          return 1

def run_unit_test(): 
    import configuration
    from subprocess import call
    # Execute the program and send the output to some file specified in configuration.py
    execute_command = configuration.execute_command
    call(execute_command, shell=True)
    
    output_file = configuration.output_file 
    # Open the file which contains output and read its contents into the current script in string form.
    output_file = open(output_file, "r")
    stdout_output = output_file.read() 
    return stdout_output 

def get_score(max_score_correctness): 
    import ConfigParser
    # Open a config files which contains scores generated by the Unit Test Module. 
    log = ConfigParser.RawConfigParser()
    log.read('log.txt')
    count_pass = log.get('section', 'count_pass')
    count = log.get('section', 'count')
    # Convert integer scores to floating-point numbers to prevent unnecessary truncation. 
    count_pass = Decimal(count_pass)
    count = Decimal(count)
    max_score_correctness = Decimal(max_score_correctness)
    if (count != 0): 
        score_correctness = (count_pass / count) * max_score_correctness
    else: 
        score_correctness = 0
    return score_correctness 

stdout_output = " "
error_message = " " 
(assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness) = read_configuration_properties()
get_assignment_data()

destination_file = copy_src_to_destination_file()
create_test_functions(destination_file)
compile_student_code()


stdout_output = run_unit_test()
score_correctness = get_score(max_score_correctness)

generate_feedback_file()
write_configuration_properties()

