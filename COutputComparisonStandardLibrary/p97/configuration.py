
substrings = ["palindrome", "not"]

compile_command = "gcc -ansi -Wall p97.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("a", "./a.out", "a is a palindrome.", "", ""), ("racecar", "./a.out", "racecar is a palindrome.", "", ""), ("gargantuan", "./a.out", "gargantuan is not a palindrome.", "", "")]
