#! /usr/bin/env python 

#---------------------------------
# OutputComparisonScript
# Version Number: 1.3.0 
# Last Revision: 8/27/2012
# by James DeFilippo (jms.defilippo@gmail.com) with Mark Sherman (msherman@cs.uml.edu)
# as part of a project under the supervision of Professor Fred Martin, and Professor Sarita Bassil 
#---------------------------------

import UMLFunctionLibrary 

stdout_output = "" 
error_message = "" 
score_correctness = 0
return_code = 0

(assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness) = UMLFunctionLibrary.read_configuration_properties()

UMLFunctionLibrary.get_assignment_data(assignment, working_Dir, script_Home)

(stdout_output, return_code) = UMLFunctionLibrary.compile_student_code(stdout_output)

if return_code == 0: 
    (score_correctness, error_message, stdout_output) = UMLFunctionLibrary.grade_submission(user_Name, max_score_correctness)
else: 
    stdout_output = "Your submission compiled with the following errors and/or warnings:" + stdout_output
    
UMLFunctionLibrary.generate_feedback_file(result_Dir, stdout_output, error_message)

UMLFunctionLibrary.write_configuration_properties(score_correctness)


