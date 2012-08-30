substrings = ["103"] 

compile_command = "gcc -ansi -Wall p74.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out testdata74 8", "103", "1testdata74 testdata74", ""),]
