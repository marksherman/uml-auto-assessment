substrings = ["negative", "positive", "zero"]

compile_command = "gcc -ansi -Wall p12.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("-1", "./a.out", "The number is negative.", "", ""), ("0", "./a.out", "The number is zero.", "", ""), ("1", "./a.out", "The number is positive.", "", ""),]



