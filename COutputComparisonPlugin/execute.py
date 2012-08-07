#! /usr/bin/env python 

import os
import sys 
from subprocess import call 
import StringIO 
import ConfigParser
from xml.dom.minidom import parse, parseString


DEBUG = 0 

def compare_literal(reference_file, student_file): 

    reference_output_string = reference_file.read()
    student_output_string = student_file.read()

    if DEBUG > 0 :
        print reference_output_string
        print student_output_string

    split_reference = reference_output_string.split(" ")
    split_student = student_output_string.split(" ")

    if split_reference == split_student : 
        print "Success!"
        return 1
    else: 
        print "Failure!"
        return 0

# Opens a file, creating the file if it does not exist.
# This is likely a hack, but for nwo it works. #TODO better way?
def open_file(file_to_open):
    if os.path.exists(file_to_open): 
        file_handle = open(file_to_open, "r+")
    else: 
        file_handle = open(file_to_open, "w")
        file_handle = open(file_to_open, "r+")

#### Go actual program (definitions go above) ####


reference_file = open_file("reference.out")
student_file = open_file("student.out") 

n = "1"
compile_command = ["gcc", "-ansi", "-Wall", "p" + n + ".c"] 
call(compile_command)
output_command = ["./a.out", ">>", "student.out"] 
call(output_command)

compare_literal(reference_file, student_file) 



######THIS SHOULD WORK!

#config = StringIO.StringIO()
#config.write('[dummysection]\n')
#config.write(open(sys.argv[1], 'r').read())
#config.seek(0, os.SEEK_SET)

#cp = ConfigParser.SafeConfigParser()
#cp.readfp(config)
#cp.read(sys.argv[1])
#print cp.get('key')

initial_string = '[section]\n' + open(sys.argv[1], 'r').read()
initial_filepointer = StringIO.StringIO(initial_string) 
config = ConfigParser.RawConfigParser()
config.readfp(initial_filepointer)

#print config.get('section', 'max.score.correctness')


result_dir = config.get('section', 'resultDir')
print result_dir
compile_log = result_dir + "/" + "compile.log"
print compile_log

compile_log_handle = open(compile_log, 'w') 
compile_log_handle = open(compile_log, 'r+')

#compile_log_handle.write('<p>Hello</p>')

compile_log_handle.write('<div class="shadow"><table><tbody>\n') 
compile_log_handle.write('<tr><th>\n')
compile_log_handle.write('Compilation Produced Errors</th></tr>\n')
compile_log_handle.write('<tr><td><pre>\n')
compile_log_handle.write('Go fuck yourself.\n')
compile_log_handle.write('</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>')

# This will probably not work in our current setup. 
#config.set('section', 'report1.file', 'compile.log')
#config.set('section', 'report1.mimeType', 'text/html') 

## UNCOMMENT THIS
#config_file = open(sys.argv[1], 'r+') 
#config_file.replace('numReports=0', 'numReports=1')

config_file = open(sys.argv[1]).read()
config_file = config_file.replace('numReports=0', 'numReports=1')
config_file_write = open(sys.argv[1], 'w')
config_file_write.write(config_file)
config_file_write.close()

#config_file = open(sys.argv[1], 'a+b').write('numCodeMarkups=1\nreport1.file=compile.log\nreport1.mimeType=text/html')

config_file = open(sys.argv[1], 'a+b').write('disableCodeCoverage=1\nexec.timeout=6000\nreport1.file=compile.log\nreport1.mimeType=text/html\nnumCodeMarkups=0\nscore.correctness=0\nscore.tools=0')

#f1=open("GraderReport.html", 'w')
#f1=open("GraderReport.html", 'r+')
#f1.write('<div class="shadow"><table><tbody>\n') 
#f1.write('<div class="shadow"><table><tbody>\n') 
#f1.write('Compilation Produced Errors</th></tr>\n')
#f1.write('<tr><td><pre>\n')
#f1.write('Go fuck yourself.\n')
#f1.write('</pre></td></tr></tbody></table></div><div class="spacer">&nbsp;</div>')

#dom = parseString('<p>Hello!</p>')
#dom = str(dom.writexml)



student_file.close()
reference_file.close()

