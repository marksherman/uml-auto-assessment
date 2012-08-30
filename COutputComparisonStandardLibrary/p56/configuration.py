substrings = ["103"] 

compile_command = "gcc -ansi -Wall p56.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out testdata56 8", "103", "1testdata56 testdata56", ""),]
