---		TFG Project (April)	---

localhost:8081/dalgs/oauth/token?grant_type=password&client_id=my-trusted-client&secret=secret&username=admin&password=admin

{
"error": "invalid_client",
"error_description": "Bad client credentials"
}



curl -v -X POST   -H "Content-Type: application/json"    -H "Authorization: Basic YWRtaW46YWRtaW4K=" 'http://localhost:8081/dalgs/oauth/token?grant_type=password&username=admin&password=admin&grant_type=password&scope=read%20write%20trust&'

{
"error": "invalid_client",
"error_description": "Bad client credentials"
}

curl -X GET -vu client_id=my-trusted-client http://localhost:8081/dalgs/oauth/token -H "Accept: application/json" -d "password=admin&username=admin&grant_type=password&scope=read%20write%20trust&client_id=my-trusted-client" 

{
"error": "invalid_client",
"error_description": "Bad client credentials"
}


 curl -X POST -vu clientapp:secret  http://localhost:8081/dalgs/oauth/token -H "Accept: application/json"  -d "password=admin&username=admin&grant_type=password&scope=read%20write&client_secret=secret&client_id=clientapp" 