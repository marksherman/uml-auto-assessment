substrings = ["0.000000", "2.000000", "1.414214"]

compile_command = "gcc -ansi -Wall -lm p15.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("0", "./a.out", "0.000000", "", ""), ("4", "./a.out 511 Mel Ott Life is good", "2.000000", "", ""), ("2", "./a.out Jem and Scout", "1.414214", "", ""),]

