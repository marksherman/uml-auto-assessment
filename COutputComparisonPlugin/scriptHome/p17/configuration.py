substrings = ["1", "6"]

compile_command = "gcc -ansi -Wall p17.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "1", "", ""), ("hello", "./a.out", "6", "", ""),]

