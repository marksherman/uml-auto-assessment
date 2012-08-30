
substrings = ["1", "5040"]

compile_command = "gcc -ansi -Wall p42.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("0", "./a.out", "1", "", ""), ("7", "./a.out", "5040", "", ""),]


