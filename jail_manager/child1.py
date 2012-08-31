#! /usr/bin/env python

import sys
import pipes

print "Running python! Argument: " + sys.argv[1]

t = pipes.Template()
pipe_out = t.open( sys.argv[1] , 'w' )

pipe_out.write('Sent from python!\n')

pipe_out.close()
