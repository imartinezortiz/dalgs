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
import getpass
from socket import error as socket_error
import logging

#Global variables


h = httplib2.Http(".cache")
#h.add_credentials('name', 'password')


class ActivityRequest(object):
    def __init__(self):
        self.__id_course = None
        self.__id_group = None
        self.__name = None
        self.__description = None
        self.__code = None

    @property
    def id_course(self):
        return self.__id_course

    @id_course.setter
    def id_course(self, id_course):
        self.__id_course = id_course

    @property
    def id_group(self):
        return self.__id_group

    @id_group.setter
    def id_group(self, id_group):
        self.__id_group = id_group

    @property
    def name(self):
        return self.__name

    @name.setter
    def name(self, name):
        self.__name = name

    @property
    def description(self):
        return self.__description

    @description.setter
    def description(self, description):
        self.__description = description

    @property
    def code(self):
        return self.__code

    @code.setter
    def code(self, code):
        self.__code = code


class UrlData(object):
    def __init__(self):
        self.__path = None
        self.__token = None

    @property
    def path(self):
        return self.__path

    @path.setter
    def path(self, path):
        self.__path = path

    @property
    def token(self):
        return self.__token

    @token.setter
    def token(self, token):
        self.__token = token

    def evaluate(self):
        if self.__token and self.__path:
            return True
        return False


def configure_logging():
    logging.basicConfig(
                level=logging.DEBUG,
                datefmt='%H:%M:%S',
                format='[ %(levelname)s ] <%(asctime)s> %(message)s')

def generate_token(url_data):
    print '\n1. GENERATE TOKEN\n'
    # Authentication via console
    print "---------LOGIN-------"
    username = raw_input("Username: ")
    password = getpass.getpass()
    print "---------------------"
    port = raw_input("Port: ")

    url_data.path = 'http://localhost:%s/dalgs/' % port

    headers = {'Content-Type': 'application/json', 'Accept': 'application/json'}
    url = '%soauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username=%s&password=%s' % \
          (url_data.path, username, password)

    logging.debug('%s\n' % url)
    # TOKEN Call
    try:
        resp, content = h.request(url, "GET", headers={'Content-Type': 'application/json', 'Accept': 'application/json'})

        # Transform resp to JSON Object
        content_obj = json.loads(content)
        # Status
        status = resp["status"]
        logging.debug("Status: %s" % status)

        if status == "200":
            url_data.token = content_obj["value"]
            logging.info("TOKEN: %s" % url_data.token)

    except httplib2.ServerNotFoundError:
        print "ServerNotFoundError: API not available"
    except socket_error as serr:
        if serr.errno != errno.ECONNREFUSED:
                # Not the error we are looking for, re-raise
            raise serr
                # connection refused
                # handle here
        pass


def get_activity(url_data):
    if url_data.evaluate():
        print "\n2. GET ACTIVITY\n"
        id_activity = raw_input("Id Activity: ")
        try:
            url = "%sapi/activity/%s?access_token=%s" % (url_data.path, id_activity, url_data.token)
            resp, content = h.request(url, "GET", headers={'Content-Type': 'application/json', 'Accept': 'application/json'})
            logging.debug("Status: %s" % resp["status"])

            if resp["status"] == "200":
                content_obj = json.loads(content)
                aux = json.dumps(content_obj['activity'])

                if aux != 'null':
                    logging.info('Activity:\n %s' % aux)
                else:
                    logging.error("Activity with ID=%s no exists" % id_activity)
            else:
                print resp

        except socket_error as serr:
            if serr.errno != errno.ECONNREFUSED:
                    # Not the error we are looking for, re-raise
                    raise serr
                 # connection refused
                 # handle here
            pass
    else:
        logging.error("Generate a valid TOKEN!\n")


def post_activity(url_data):
    if url_data.evaluate():
        act = ActivityRequest()

        print "\n3. POST EXTERNAL ACTIVITY\n"
        act.id_course = raw_input("Id Course: ")
        act.id_group = raw_input("Id Group: ")
        act.name = raw_input("Name: ")
        act.description = raw_input("Description: ")
        act.code = raw_input("Code: ")
        try:
            url = "%sapi/activity?access_token=%s" % (url_data.path, url_data.token)
            data = {"id_course": act.id_course, "id_group": act.id_group, "name": act.name, "description": act.description,
                    "code": act.code}
            resp, content = h.request(url, "POST", headers={'Content-Type': 'application/json', 'Accept': 'application/json'}
                                      , body=json.dumps(data))
            if resp["status"] == "200":
                logging.info('Activity added correctly')
            else:
                print resp
        except socket_error as serr:
                if serr.errno != errno.ECONNREFUSED:
                        # Not the error we are looking for, re-raise
                        raise serr
                        # connection refused
                        # handle here
                pass
    else:
        logging.error("Generate a valid TOKEN!\n")


def menu(url_data):
    print "\n"
    print ("_______________________________\n")
    print ("           MAIN MENU           \n")
    print ("1- GET TOKEN")
    print ("2- GET Activity")
    print ("3- POST External Activity")
    print ("4- Exit")

    op = raw_input("\nChoose an option: ") 			# Loop finish when the user introduce 4
    print ("_______________________________\n")

    while op != '4':
        if op == '1':
            generate_token(url_data)
        elif op == '2':
            get_activity(url_data)
        elif op == '3':
            post_activity(url_data)

        print "\n"
        print ("_______________________________\n")
        print ("           MAIN MENU           \n")
        print ("1- GET TOKEN")
        print ("2- GET Activity")
        print ("3- POST External Activity")
        print ("4- Exit")

        op = raw_input("\nChoose an option: ") 		# Loop finish when the user introduce 4
        print ("_______________________________\n")


def main():
    configure_logging()
    url_data = UrlData()
    menu(url_data)

if __name__ == '__main__':
    main()