substrings = ["-9", "-36", "16", "40", "-13", "25", "22", "-49", "47", "-2", "42", "39", "41", "50", "-37", "21", "-44"]

compile_command = "gcc -ansi -Wall p72.c"

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out 19 testdata72", "-49 -44 -37 -36 -13 -9 -2 16 21 22 22 25 39 40 40 41 42 47 50", "1testdata72 testdata72", ""),]





