---		TFG Project (April)	---

//TO-DO


//NOT WORKING

Optional: Change the pagination
0.- XAPI --> http://experienceapi.com

// DONE
0.-	Fixed Group bug
1.- Simple GET&POST (dummy)
2.- Change type of competence to an enum
3.- Add credits attribute to subject
4.- Add url for the "ficha docente" 
5.- POST activities to a group and/or course
6.- Subject View CSV Download --> http://localhost:8081/dalgs/degree/1/module/1/topic/1/subject/download.htm
7.- Upload CSV of Competences --> encoding?? That's why ParseEnum fails
8.- Export csv
9.- Securing Web Service (exception in Spring Security ???) call header (BASIC)


<<<<<<< HEAD
=======
curl -X GET -vu client_id=my-trusted-client http://localhost:8081/dalgs/oauth/token -H "Accept: application/json" -d "password=admin&username=admin&grant_type=password&scope=read%20write%20trust&client_id=my-trusted-client" 

{
"error": "invalid_client",
"error_description": "Bad client credentials"
}


 curl -X POST -vu clientapp:secret  http://localhost:8081/dalgs/oauth/token -H "Accept: application/json"  -d "password=admin&username=admin&grant_type=password&scope=read%20write&client_secret=secret&client_id=clientapp" 
>>>>>>> oauth2
