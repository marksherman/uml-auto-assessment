substrings = ["scalene", "isosceles", "equilateral", "right", "invalid"] 

compile_command = "gcc -ansi p58.c" 

number_of_test_cases = 5

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("4 5 7", "./a.out", "scalene triangle", "", ""), ("3 4 5", "./a.out", "right scalene triangle", "", ""), ("5 5 7", "./a.out", "isosceles triangle", "", ""), ("5 5 5", "./a.out", "equilateral triangle", "", ""), ("1 2 8", "./a.out", "invalid triangle", "", ""),]

