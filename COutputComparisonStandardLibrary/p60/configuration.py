substrings = ["301", "302", "303", "3", "2", "7", "43", "151", "101"]

compile_command = "gcc -ansi -Wall p60.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out 300 305", "301 = 7 * 43 302 = 2 * 151 303 = 3 * 101", "", ""),]

