# -*- encoding: utf-8 -*-
#!/usr/bin/python
import json
import httplib2
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

def generate_token():
	
	# Pido por consola usuario y contraseña
	print "---------LOGIN-------"
	username = raw_input("Username: ")
	password = raw_input("Password: ")
	print "---------------------"
	port = raw_input("Port: ")
	
	
	global path
	path = 'http://localhost:'+ port + '/dalgs/'
	
	headers = {'Content-Type':'application/json', 'Accept': 'application/json'} 
	url = path + 'oauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username=' + username +'&password=' + password
	
	print url
	
	# Token Call
	resp, content = h.request(url, "GET", headers = {'Content-Type':'application/json', 'Accept': 'application/json'} )
	
	
	
	# Transformamos a JSON Object
	content_obj = json.loads(content)
	
	# Status
	status = resp["status"]
	
	print "Satus: " + status

	if status == "200":
		
		#Token
		global token
		token = content_obj["value"]
		
		print "Token: " + token
	
	else:
		print resp
	
		
def get_activity():		
	id_activity = raw_input("Id Activity: ")

	
	url =  path + "api/activity/" + id_activity + "?access_token=" + token
	
	resp, content = h.request(url, "GET", headers = {'Content-Type':'application/json', 'Accept': 'application/json'} )
	
	if resp["status"] == "200":
		content_obj = json.loads(content)
		print 'Activity:' 
		print  content_obj[""u'activity'""] 
	
	else:
		print resp
		
		print "\nGenerate a valid token!\n"
		

def post_activity():
	act = ActivityRequest()
	
	print "\n----NEW ACTIVITY----"
	act.id_course = raw_input("Id Course: ")
	act.id_group = raw_input("Id Group: ")
	act.name = raw_input("Name: ")
	act.description = raw_input("Description: ")
	act.code = raw_input("Code: ")
	
	#data = json.dumps(act)
	
	#print data
		
	url =  path + "api/activity?access_token=" + token 
	
	data = {"id_course" : act.id_course, "id_group" : act.id_group, "name" : act.name, "description" : act.description, "code" : act.code}

	resp, content = h.request(url, "POST", headers = {'Content-Type':'application/json', 'Accept': 'application/json'}, body = json.dumps(data) )
	
	if resp["status"] == "200":
		print 'Activity added correctly' 

	else:
		print resp
		
		
	

def menu (): 
	print "\n"
	print ("1- GET TOKEN") 
	print ("2- GET Activity") 
	print ("3- POST Activity") 
	print ("4- salir") 
	
	op = raw_input("Choose an option: ") # el bucle corta cuando pongas 4 
	
	while op != '4' : 
		if  op == '1': 
			generate_token()
		elif op == '2':
			get_activity()
		elif op == '3':	
			post_activity()
		
		print "\n"
		print ("1- GET TOKEN") 
		print ("2- GET Activity") 
		print ("3- POST Activity") 
		print ("4- salir") 
		
		op = raw_input("Choose an option: ") # el bucle corta cuando pongas 4 	
			
menu() 

