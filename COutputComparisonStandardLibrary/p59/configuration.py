substrings = ["3", "697", "699", "701", "703", "705"]

compile_command = "gcc -ansi -Wall p59.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out 700 710", "3 697 3 699 3 701 3 703 3 705", "", ""),]

