substrings = ["bigger", "not"]

compile_command = "gcc -ansi -Wall p7.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("101", "./a.out", "The number is bigger than 100.", "", ""), ("100", "./a.out", "The number is not bigger than 100.", "", ""), ("99", "./a.out", "The number is not bigger than 100.", "", ""),]



