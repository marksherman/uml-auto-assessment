substrings = ["4.899"] 

compile_command = "gcc -ansi -Wall p53.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out 24 .001", "4.899", "", ""),]

