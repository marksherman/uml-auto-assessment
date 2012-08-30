substrings = ["95.033180"]

compile_command = "gcc -ansi -Wall -D_GNU_SOURCE p20.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("5.5", "./a.out", "95.033180", "", ""), ]

