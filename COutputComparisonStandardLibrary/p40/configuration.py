
substrings = ["1", "2", "5"]

compile_command = "gcc -ansi -Wall p40.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("5 5", "./a.out testdata40", "5", "1testdata40 testdatat40", ""), ("4 2", "./a.out", "2", "2testdata40 testdata40", ""), ("98 99", "./a.out", "1", "3testdata40 testdata40", "")]

