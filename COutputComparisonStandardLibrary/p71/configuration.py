substrings = ["1", "4", "3", "6", "3", "5"]

compile_command = "gcc -ansi -Wall p71.c"

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("6 1 4 3 6 3 5", "./a.out", "1 4 6 3 3 5", "", ""),]

