substrings = ["*******", "*****", "*"] 

compile_command = "gcc -ansi -Wall p30.c" 

number_of_test_cases = 3

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("1 1", "./a.out", "*", "", ""), ("7 7", "./a.out", "******* *     *  *     *  *     *  *     *  *     * *******", "", ""), ("5 3", "./a.out", "***** *   * *****", "", ""),]

