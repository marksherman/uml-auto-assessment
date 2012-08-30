substrings = ["100", "101", "102", "103", "104", "105", "106", "107", "108", "109"]

compile_command = "gcc -ansi -Wall p27.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("100 101 102 103 104 105 106 107 108 109", "./a.out", "109 108 107 106 105 104 103 102 101 100", "1testdata27 testdata27", ""), ]

