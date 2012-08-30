substrings = ["dreams", "are", "true", "while", "they", "last", "and", "do", "we", "not" "live", "in"]

compile_command = "gcc -ansi -Wall p99.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "and are do dreams dreams in last live not they true we while", "1testdata99 testdata99", ""),]

