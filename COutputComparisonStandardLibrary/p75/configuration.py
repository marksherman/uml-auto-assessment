substrings = ["0", "-9", "2"]

compile_command = "gcc -ansi -Wall p75.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("5 3 -7 -6 -5 -4 -3 -2 -1 0 1 2 3 4 5 6 7 1 3", "./a.out", "-9 2 0", "", ""),]

