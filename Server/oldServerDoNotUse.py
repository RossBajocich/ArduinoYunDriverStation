#!/usr/bin/python
import socket
import sys
import time
sys.path.insert(0, '/usr/lib/python2.7/bridge/')
from bridgeclient import BridgeClient as bridgeclient

HOST = ''   # Symbolic name, meaning all available interfaces
PORT = 8888 # Arbitrary non-privileged port

bc = bridgeclient()

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = (HOST, PORT)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.bind(server_address)

print 'Socket created'
                                         
while 1:
    data, sender = sock.recvfrom(3)    
	
	if not data: break
	bc.put('data', data)
	time.sleep(.05)
