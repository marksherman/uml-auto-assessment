substrings = ["1", "-1"]

compile_command = "gcc -ansi -Wall p70.c"

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("2" "./a.out", "1 1 1 -1", "", ""), ("4" "./a.out", "1 1 1 1 -1 1 -1 1 -1 -1 1 1 1 -1 -1 1", "", "")]

