substrings = ["103"] 

compile_command = "gcc -ansi -Wall p67.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out testdata67 8", "103", "1testdata67 testdata67", ""),]
