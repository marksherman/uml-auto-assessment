substrings = ["hetay", "uickqay", "rownbay", "oxfay", "umpedjay", "overway", "hetay", "azylay", "ogday", "ecausebay", "itway", "ouldway", "otnay", "ovemay", "yayway"]

compile_command = "gcc -ansi -Wall p93.c" 

number_of_test_cases = 1

# FORMAT: tests = [(stdin, args, reference_output_strings, files, hints), ...]
tests = [("the quick brown fox jumped over the lazy dog because it would not move yay .....", "./a.out", "hetay uickqay rownbay oxfay umpedjay overway hetay azylay ogday ecausebay itway ouldway otnay ovemay yayway", "", ""),]
