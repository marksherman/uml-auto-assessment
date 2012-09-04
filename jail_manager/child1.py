#! /usr/bin/env python

# DO NOT USE PYCHECKER ON THIS FILE. IT WILL EAT IT. I'M NOT KIDDING.

import sys
import pipes

print "Running python! Argument: " + sys.argv[1]

t = pipes.Template()
pipe_out = t.open( sys.argv[1] , 'w' )

pipe_out.write('Sent from python!\n')

pipe_out.close()
