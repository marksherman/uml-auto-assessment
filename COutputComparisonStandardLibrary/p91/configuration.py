substrings = ["Monday", "Tuesday", "Thursday", "Wednesday"] 

compile_command = "gcc -ansi -Wall p91.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("1994 1809 1963 2096", "./a.out", "Monday Tuesday Thursday Wednesday", "", ""),]


