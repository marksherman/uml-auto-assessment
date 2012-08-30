
substrings = ["3", "2", "1", "0"]

compile_command = "gcc -ansi -Wall p90.c" 

number_of_test_cases = 4

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("634", "./a.out", "3", "", ""), ("28", "./a.out", "2", "", ""), ("4000", "./a.out", "1", "", ""), ("9", "./a.out", "0", "", ""),]

