# -#- encoding: utf-8 -#-
#!/usr/bin/python

#
# This file is part of D.A.L.G.S.
#
# D.A.L.G.S is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# D.A.L.G.S is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
#




import json
import httplib2
import errno
from socket import error as socket_error
from urllib import urlencode

#Global variables


h = httplib2.Http(".cache")
#h.add_credentials('name', 'password')

class ActivityRequest():
	id_course = -1
	id_group = -1
	name = ""
	description = ""
	code = ""

def generate_TOKEN():
	
	print "\n1. GENERATE TOKEN\n"
		
	
	# Pido por consola usuario y contraseña
	print "---------LOGIN-------"
	username = raw_input("Username: ")
	password = raw_input("Password: ")
	print "---------------------"
	port = raw_input("Port: ")
	
	
	global PATH
	PATH = 'http://localhost:'+ port + '/dalgs/'
	
	headers = {'Content-Type':'application/json', 'Accept': 'application/json'} 
	url = PATH + 'oauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username=' + username +'&password=' + password
	
	print url
	
	# TOKEN Call
	try:
		resp, content = h.request(url, "GET", headers = {'Content-Type':'application/json', 'Accept': 'application/json'} )
	
	    # Transformamos a JSON Object
		content_obj = json.loads(content)
	
	    # Status
	    
		status = resp["status"]
	
	    
		print "Satus: " + status
		
		if status == "200":
			#TOKEN
			global TOKEN
		
			TOKEN = content_obj["value"]
			
			print "TOKEN: " + TOKEN
		
	except httplib2.ServerNotFoundError:
		print "ServerNotFoundError: API not available"
	
		
	except socket_error as serr:
		if serr.errno != errno.ECONNREFUSED:
	        # Not the error we are looking for, re-raise
			raise serr
	    	# connection refused
	    	# handle here
		
		
def get_activity():	
	
	print "\n2. GET ACTIVITY\n"
	
	id_activity = raw_input("Id Activity: ")

	try:
		
		url =  PATH + "api/activity/" + id_activity + "?access_token=" + TOKEN
		
		resp, content = h.request(url, "GET", headers = {'Content-Type':'application/json', 'Accept': 'application/json'} )
		
		if resp["status"] == "200":
			content_obj = json.loads(content)
			print 'Activity:' 
			print  content_obj[""u'activity'""] 
		
		else:
			print resp
				
	except NameError:
		
		print "\nGenerate a valid TOKEN!\n"
		

def post_activity():
	act = ActivityRequest()
	
	print "\n3. POST ACTIVITY\n"
	act.id_course = raw_input("Id Course: ")
	act.id_group = raw_input("Id Group: ")
	act.name = raw_input("Name: ")
	act.description = raw_input("Description: ")
	act.code = raw_input("Code: ")
	
	#data = json.dumps(act)
	
	#print data
		
	try:
		
		
		url =  PATH + "api/activity?access_token=" + TOKEN 
		
	
		data = {"id_course" : act.id_course, "id_group" : act.id_group, "name" : act.name, "description" : act.description, "code" : act.code}
	
		resp, content = h.request(url, "POST", headers = {'Content-Type':'application/json', 'Accept': 'application/json'}, body = json.dumps(data) )
		
		if resp["status"] == "200":
			print 'Activity added correctly' 
	
		else:
			print resp
			
	except NameError:
		print "\nGenerate a valid TOKEN!\n"
		
	

def menu (): 
	print "\n"
	print ("_______________________________\n") 
	print ("           MAIN MENU           \n") 
	print ("1- GET TOKEN") 
	print ("2- GET Activity") 
	print ("3- POST Activity") 
	print ("4- Exit") 
	
	op = raw_input("\nChoose an option: ") 			# Loop finish when the user introduce 4 
	print ("_______________________________\n") 
		
	
	while op != '4' : 
		if  op == '1': 
			generate_TOKEN()
		elif op == '2':
			get_activity()
		elif op == '3':	
			post_activity()
		
		print "\n"
		print ("_______________________________\n") 
		print ("           MAIN MENU           \n") 
		print ("1- GET TOKEN") 
		print ("2- GET Activity") 
		print ("3- POST Activity") 
		print ("4- salir") 
		
		
		op = raw_input("\nChoose an option: ") 		# Loop finish when the user introduce 4 
		print ("_______________________________\n") 
				
			
menu() 

