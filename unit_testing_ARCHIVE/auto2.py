import os 
import fileinput 

###preformat dst_file 
dst_file = open('dst_file','r+') # create a new file called 'copy data'
dst_file.write('#include<minunit.h>')
dst_file.write('\n')
dst_file.write(src_file_content)

src_file = open('src_file', 'r+')


for line in fileinput.FileInput("dst_file",inplace=1):
    line = line.replace("main","student_main")
    print line,




#def find_and_replace(line): 
#    line = line.replace('hello', 'fuck')
#    dst_file.write(line)    
#    return



# src_file_content = src_file.read()






#for line in dst_file: 
#    find_and_replace(line)

  ###########SRC and DST directories#############
#SRC=/home/jdefilip/src
#DST=/home/jdefilip/dst


#maybe?
#dst_file_content = dst_file.read()
#print dst_file_content
#dst_file_content.replace('hello', 'fuck')
#dst_file.write(dst_file_content) 


#strange this works 
#for line in dst_file: 
#    line = line.replace('hello', 'fuck') 
#    print line 






#dst_file_content = dst_file.read()
#print dst_file_content
#THIS DOES NOT WORK!
#for line in fileinput.FileInput('dst_file', inplace=1):
#    line=line.replace('hello', 'fuck')
#    print line


#dst_file.write(src_file_content) # write file contents to dst_file


#for line in fileinput.FileInput("dst_file", inplace=1):
#    line=line.replace("hello","fuck")
#    print line



# I'm getting a error type 8 



