substrings = [".a/.out", "marshmallow", "fluff", "was", "made", "in", "flynn", "massachusetts"]

compile_command = "gcc -ansi -Wall p22.c" 

number_of_test_cases = 2

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out marshmallow fluff was made in flynn massachusetts", "massachusetts flynn in made was fluff marshmallow", "", ""), ]

