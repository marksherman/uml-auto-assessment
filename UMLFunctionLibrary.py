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

def get_assignment_data(assignment, working_Dir, script_Home):  
     import os 
     import glob
     import shutil
     os.chdir(script_Home + "/" + assignment) 
     for each_file in glob.glob('*testdata*'): 
         if os.path.exists(each_file): 
             shutil.copy(each_file, working_Dir)
     for each_file in glob.glob('*testdata'): 
         if os.path.exists(each_file): 
             shutil.copy(each_file, working_Dir)
     for each_file in glob.glob('*.h'): 
         if os.path.exists(each_file): 
             shutil.copy(each_file, working_Dir)
     shutil.copy('configuration.py', script_Home)
     shutil.copy('configuration.py', working_Dir)
     os.chdir(script_Home)
     if os.path.exists('minunit.h'): 
         shutil.copy('minunit.h', working_Dir)
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


def get_execute_command (args): 
     execute_command = [] 
     execute_command = args.split()
     return execute_command


def run_comparison_test(stdin, args, reference_output_string, files, messages, count, count_pass, error_list, user_Name): 
     from subprocess import Popen, PIPE, STDOUT, call

     execute_command_string = ""
     symlink_command = "" 
     symlink_command_split = []
     count = count + 1
       
     # Create symbolic links for data files. 
     #if files != "" : 
     #      symlink_commmand = "ln -fs -T " + files
     #      symlink_comand_split = symlink_command.split()
     #      Popen(symlink_command_split)

     if files != "": 
          call("ln -fs -T " + files, shell=True)
          
     student_output = open(str(count) + user_Name + ".out", "w")

     execute_command_string = get_execute_command(args) 
     p = Popen(execute_command_string, stdout=student_output, stdin=PIPE, stderr=STDOUT)
     p.communicate(input=stdin)

     student_output = open(str(count) + user_Name + ".out", "r")

     student_output_string = student_output.read()
       
     if ((compare_substring(reference_output_string, student_output_string)) == 0): 
           count_pass = count_pass + 1
     else:
           error_list.append(messages)

     return (count, count_pass, error_list)

def grade_submission(user_Name, max_score_correctness):
     import configuration

     number_of_test_cases = configuration.number_of_test_cases
     tests = configuration.tests
     count_pass = 0
     temp = 0
     count = 0

     error_list = [] 

     for stdin, args, reference_output_string, files, messages in tests:
       (count, count_pass, error_list) = run_comparison_test(stdin, args, reference_output_string, files, messages, count, count_pass, error_list, user_Name)
     error_message = " "
     error_message = " ".join(error_list)
            
     if (count_pass != number_of_test_cases): 
        stdout_output = "Your submission succeeded for " + str(count_pass) + " of " + str(configuration.number_of_test_cases) + " test cases." 
     else:
        stdout_output = "Congrats! Your submission succeeded for all " + str(count_pass) + " test cases."
     score_correctness = ( Decimal(count_pass) / Decimal(number_of_test_cases) ) * Decimal(max_score_correctness)

     return (score_correctness, error_message, stdout_output)







def copy_src_to_destination_file(assignment): 
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

def write_configuration_properties(score_correctness):  
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

def generate_feedback_file(result_Dir, stdout_output, error_message): 
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

def compile_student_code(stdout_output): 
     import configuration
     from subprocess import Popen, PIPE, STDOUT


     compile_command = configuration.compile_command
     compile_command_split = compile_command.split()

     p = Popen(compile_command_split, stdout=PIPE, stderr=STDOUT)
     (x, y) = p.communicate()
     

     return (x, p.returncode)
          

def run_unit_test(stdout_output): 
    stdout_output = ""
    import configuration
    from subprocess import Popen, PIPE, STDOUT 

    execute_command = configuration.execute_command
    execute_command_split = execute_command.split()

    p = Popen(execute_command_split, stdout=PIPE, stderr=STDOUT)
    (stdout_output, y) = p.communicate()
    
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

