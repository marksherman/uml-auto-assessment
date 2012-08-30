substrings = ["15", "30", "7"]

compile_command = "gcc -ansi -Wall p84.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out 5", "15 30", "", ""), ("", "./a.out 3", "15 7", "", "")]
