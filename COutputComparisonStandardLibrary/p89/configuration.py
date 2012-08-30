substrings = ["11", "9", "7", "14", "8"]

compile_command = "gcc -ansi -Wall p89.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out testdata89", "11", "1testdata89 testdata89", ""), ("", "./a.out testdata89", "9 14", "2testdata89 testdata89", ""), ("", "./a.out testdata89", "7 8 9", "3testdata89 testdata89", ""), ]


