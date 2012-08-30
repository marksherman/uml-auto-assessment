substrings = ["16", "19", "20"]

compile_command = "gcc -ansi -Wall p65.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "16 19 20", "1testdata65 testdata65", ""),]
