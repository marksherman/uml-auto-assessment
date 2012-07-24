import os 
import fileinput

# FILE STUFF 

src_file = open('src_file', 'r+') #open the sample file 
dst_file = open('dst_file','r+') # create a new file called 'copy data'
src_file_content = src_file.read()
dst_file.write('#include<minunit.h>')
dst_file.write('\n')

for line in dst_file: 
    line = line.replace('hello', 'fuck') 
    src_file_content = src_file_content + line 
    
dst_file.write(src_file_content)






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



