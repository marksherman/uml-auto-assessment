substrings = ["1", "34"] 

compile_command = "gcc -ansi -Wall p54.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("1", "./a.out", "1", "", ""), ("9", "./a.out", "34", "", ""),]
