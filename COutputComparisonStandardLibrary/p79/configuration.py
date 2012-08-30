substrings = ["37", "28", "1", "25", "9", "27", "24", "12", "18", "3", "15", "6", "0"]

compile_command = "gcc -ansi -Wall p79.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("37 28 1 25", "./a.out", "37 28 1 25 9 27 24 12 18 3 12 3 15 9 9 15 6 0 6 0  6 6 6 6", "", ""),]


