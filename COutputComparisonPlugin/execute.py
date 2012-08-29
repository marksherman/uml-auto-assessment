#! /usr/bin/env python 

#---------------------------------
# OutputComparisonScript
# Version Number: 1.3.0 
# Last Revision: 8/27/2012
# by James DeFilippo (jms.defilippo@gmail.com) with Mark Sherman (msherman@cs.uml.edu)
# as part of a project under the supervision of Professor Fred Martin, and Professor Sarita Bassil 
#---------------------------------

import UMLFunctionLibrary

stdout_output = " " 

(assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness) = UMLFunctionLibrary.read_configuration_properties()

UMLFunctionLibrary.get_assignment_data(assignment, working_Dir, script_Home)

UMLFunctionLibrary.compile_student_code()

(score_correctness, error_message, stdout_output) = UMLFunctionLibrary.grade_submission(user_Name, max_score_correctness)

UMLFunctionLibrary.generate_feedback_file(result_Dir, stdout_output, error_message)

UMLFunctionLibrary.write_configuration_properties(score_correctness)


