substrings = ["-10", "-9", "-8", "-7", "-5", "-2", "-1", "3", "4", "5", "6", "7", "8"] 

compile_command = "gcc -ansi -Wall p92.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "8 occurs 1 times. 7 occurs 1 times. 6 occurs 2 times. 5 occurs 2 times. 4 occurs 1 times. 3 occurs 1 times. -1 occurs 2 times. -2 occurs 2 times. -5 occurs 3 times. -7 occurs 1 times. -8 occurs 1 times. -9 occurs 1 times. -10 occurs 1 times.", "1testdata92 testdata92", ""),]


