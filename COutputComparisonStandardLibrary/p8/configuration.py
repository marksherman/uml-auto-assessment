substrings = ["********"]

compile_command = ["gcc -ansi -Wall p8.c"]

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_string, files, hints), ...]
tests = [("", "./a.out", "********", "1testdata8 testdata8", ""), ("", "./a.out", "", "2testdata8 testdata8", "")]

