
substrings = ["30.320000"]

compile_command = "gcc -ansi -Wall p32.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("1.3 1.5 1.7 1.9 2.1 2.3 2.5 2.7 2.7 2.5 2.3 2.1 1.9 1.7 1.5 1.3", "./a.out", "30.320000", "", ""),]


