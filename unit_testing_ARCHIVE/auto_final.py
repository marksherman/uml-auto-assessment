import os 
#import fileinput 

###preformat dst_file 
dst_file = open('dst_file','r+') # create a new file called 'copy data'
dst_file.write('#include<minunit.h>')
dst_file.write('\n')

###read in src_file one line at a time, replace main with student_main and write to dst_file
src_file = open('src_file', 'r+')
for line in src_file: 
    line = line.replace('main', 'student_main')
    dst_file.write(line)

###






