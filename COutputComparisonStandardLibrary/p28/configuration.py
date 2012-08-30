substrings = ["11", "9", "7", "14", "8"]

compile_command = "gcc -ansi -Wall p28.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out testdata28", "11", "1testdata28 testdata28", ""), ("", "./a.out testdata28", "9 14", "2testdata28 testdata28", ""), ("", "./a.out testdata28", "7 8 9", "3testdata28 testdata28", ""), ]


