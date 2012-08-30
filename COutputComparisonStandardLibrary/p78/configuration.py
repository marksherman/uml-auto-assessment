substrings = ["B", "E"]

compile_command = "gcc -ansi -Wall p78.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("B 12 -1", "./a.out", "B", "", ""), ("A 4 2 0 -1", "./a.out", "E", "", "")]
