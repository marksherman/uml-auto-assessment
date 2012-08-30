substrings = ["12.650000", "0.000000"]

compile_command = "gcc -ansi -Wall p19.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("2.3 5.5", "./a.out", "12.650000", "", ""), ("2.3 0.0", "./a.out", "0.000000", "", ""),]

