#! /usr/bin/env python

#-----------------------------------------
# UnitTestScript 
# Version Number: 1.0.0 
# Last Revision: 8/28/2012 
# by James DeFilippo (jms.defilippo@gmail.com) with Mark Sherman (msherman@cs.uml.edu)
# as part of a project under the supervision of Professor Fred Martin and Professor Sarita Bassil
#-----------------------------------------
import UMLFunctionLibrary

stdout_output = " "
error_message = " " 

(assignment, user_Name, result_Dir, working_Dir, script_Home, max_score_correctness) = UMLFunctionLibrary.read_configuration_properties()

UMLFunctionLibrary.get_assignment_data(assignment, working_Dir, script_Home)

destination_file = UMLFunctionLibrary.copy_src_to_destination_file(assignment)

UMLFunctionLibrary.create_test_functions(destination_file)

UMLFunctionLibrary.compile_student_code()

stdout_output = UMLFunctionLibrary.run_unit_test()

score_correctness = UMLFunctionLibrary.get_score(max_score_correctness)

UMLFunctionLibrary.generate_feedback_file(result_Dir, stdout_output, error_message)

UMLFunctionLibrary.write_configuration_properties(score_correctness)

