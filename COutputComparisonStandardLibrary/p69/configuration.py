substrings = ["Yes"]

compile_command = "gcc -ansi -Wall p69.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("is \n isotope \n notatrope \n is you aint my baby \n" "./a.out", "Yes Yes", "", ""),]

