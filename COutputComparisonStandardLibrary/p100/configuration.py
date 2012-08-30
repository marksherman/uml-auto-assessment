substrings = ["1", "2", "3", "accepts", "rejects"]

compile_command = "gcc -ansi -Wall p100.c" 

number_of_test_cases = 4

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("010", "./a.out", "Language 1 accepts. Language 2 accepts. Language 3 accepts.", "", ""), ("001001001", "./a.out", "Language 1 accepts. Language 2 accepts. Language 3 accepts.", "", ""), ("0111", "./a.out", "Language 1 rejects. Language 2 rejects. Language 3 rejects.", "", ""), ("111000", "./a.out", "Language 1 rejects. Language 2 rejects. Language 3 rejects.", "", ""),]

