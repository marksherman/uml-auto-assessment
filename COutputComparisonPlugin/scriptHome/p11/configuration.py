substrings = ["not", "equal"]

compile_command = "gcc -ansi -Wall p11.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("-1", "./a.out", "The number is not equal to zero.", "", ""), ("0", "./a.out", "The number is equal to zero.", "", ""), ("1", "./a.out", "The number is not equal to zero.", "", ""),]



