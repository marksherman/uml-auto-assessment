substrings = ["1729", "1", "1728", "12", "729", "1000", "9", "10", "4104", "8", "4096", "2", "16", "3375", "15"]

compile_command = "gcc -ansi -Wall p77.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("5000", "./a.out", "1729 1 1728 1 12 1 729 1000 9 10 4104 8 4096 2 16 2 729 3375 9 15", "", ""),]
