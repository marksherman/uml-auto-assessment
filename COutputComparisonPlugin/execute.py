#! /usr/bin/env python 

#---------------------------------
# OutputComparisonScript
# Version Number: 1.3.1 
# Last Revision: 2012/09/10
# by James DeFilippo (jms.defilippo@gmail.com) with Mark Sherman (msherman@cs.uml.edu)
# as part of a project under the supervision of Professor Fred Martin, and Professor Sarita Bassil 
#---------------------------------

# v1.3.1 2012/09/10 msherman getting rid of lookup against standard library of assignments
# v1.3 jdefilippo up to 2012/08/27

import UMLFunctionLibrary 

stdout_output = "" 
error_message = "" 
score_correctness = 0
return_code = 0

configs = UMLFunctionLibrary.read_configuration_properties()

if( configs == -1 ):
    #TODO raise an error exception
    print "Problem with configuration properties. Exiting."
    exit()

(assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness) = configs

# The following line makes it so this plugin ONLY works with the built-in assignment lib
# UMLFunctionLibrary.get_assignment_data(assignment, working_Dir, script_Home)

(stdout_output, return_code) = UMLFunctionLibrary.compile_student_code(stdout_output)

if return_code == 0: 
    (score_correctness, error_message, stdout_output) = UMLFunctionLibrary.grade_submission(user_Name, max_score_correctness)
else: 
    stdout_output = "Your submission compiled with the following errors and/or warnings:<br/>\n" + stdout_output
    
UMLFunctionLibrary.generate_feedback_file(result_Dir, stdout_output, error_message)

UMLFunctionLibrary.write_configuration_properties(score_correctness)


