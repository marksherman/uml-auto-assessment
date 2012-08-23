#! /usr/bin/env python 

import re 

f = open("testdata", "r")
f_string = f.read()

x = re.compile('passed [0-9]')
#mod_x = x.sub('', f_string)
list_x = re.findall('passed [0-9]', f_string)
x2 = re.sub('', list_x)
print x2

y = re.compile('of [0-9]')
list_y = re.findall('of [0-9]', f_string)
y2 = re.sub('', list_y)
#mod_y = y.sub('', mod_x)
print y2




