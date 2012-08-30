substrings = ["-0.977530"]

compile_command = "gcc -ansi -Wall -lm p16.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out 4.5", "-0.977530", "", ""),]

