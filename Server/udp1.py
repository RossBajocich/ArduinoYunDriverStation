#!/usr/bin/python
import socket
import sys
sys.path.insert(0, '/usr/lib/python2.7/bridge/')
from bridgeclient import BridgeClient as bridgeclient

HOST = ''   # Symbolic name, meaning all available interfaces
PORT = 8888 # Arbitrary non-privileged port

bc = bridgeclient()  
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'Socket created'
   
#Bind socket to local host and port
try:
	s.bind((HOST, PORT))
except socket.error as msg:
	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
	sys.exit()
                    
print 'Socket bind complete'
                     
#Start listening on socket
s.listen(10)
print 'Socket now listening'

while 1:                     
	#now keep talking with the client
	while 1:
		#wait to accept a connection - blocking call
		conn, addr = s.accept()
		print 'Connected with ' + addr[0] + ':' + str(addr[1])
		break;
	while 1:
		data = conn.recv(256)
		if not data: break
		bc.put('data', data)
		print(data)
		print("past!")
	print "Disconnected? Restarting Connection!"
s.close()
