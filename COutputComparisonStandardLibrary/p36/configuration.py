
substrings = ["is", "not", "prime"]

compile_command = "gcc -ansi -Wall p36.c" 

number_of_test_cases = 4

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("", "./a.out testdata36", "is not prime", "1testdata36 testdata36", ""), ("", "./a.out testdata36", "is prime", "2testdata36 testdata36", ""), ("", "./a.out testdata36", "is not prime", "3testdata36 testdata36", ""), ("", "./a.out testdata36", "is prime", "4testdata36 testdata36", ""), ]

