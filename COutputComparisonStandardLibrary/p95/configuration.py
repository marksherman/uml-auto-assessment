
substrings = ["Ella", "44", "Adam", "21", "Sally", "33", "Bertrand", "89", "187.000000"]

compile_command = "gcc -ansi -Wall p95.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out", "Ella 44 Adam 21 Sally 33 Bertrand 89 187.000000", "1testdata95 testdata95", ""),]

