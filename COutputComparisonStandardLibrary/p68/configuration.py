substrings = ["day!", "great", "a", "is", "say,", "I", "This,"]

compile_command = "gcc -ansi -Wall p68.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("This, I say, is a great day!.", "./a.out", "day! great a is say, I This,", "", ""),]

