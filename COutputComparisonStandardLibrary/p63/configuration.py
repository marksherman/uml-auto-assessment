substrings = ["32", "15"]

compile_command = "gcc -ansi -Wall p63.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "32", "1testdata63 testdata63", ""), ("", "./a.out", "15", "2testdata63 testdata63", ""),]
