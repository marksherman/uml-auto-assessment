substrings = ["1", "4", "7"]

compile_command = "gcc -ansi -Wall p14.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "1", "", ""), ("", "./a.out 511 Mel Ott Life is good", "7", "", ""), ("", "./a.out Jem and Scout", "4", "", ""),]

