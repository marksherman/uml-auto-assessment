substrings = ["0"]

compile_command = "gcc -ansi -Wall p46.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("11 -5 -4 -3 -2 -1 0 1 2 3 4 5", "./a.out", "0", "", ""),]


