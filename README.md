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


//   OAUTH2

1.- Para obtener el token (usuario escogido admin):

	http://localhost:8081/dalgs/oauth/token?grant_type=password&client_id=restapp&client_secret=restapp&username=admin&password=admin
	
Obtenemos:

{
	"value": "a4c50bfe-d1e2-4224-abfa-587fc6a90608",
	"expiration": 1429997871360,
	"tokenType": "bearer",
	"refreshToken": {
		"value": "8561c54f-de74-4b6b-ba0d-cbbd1f024246",
		"expiration": 1432589463683
	},
	"scope": [ ],
	"additionalInformation": { },
	"expired": false,
	"expiresIn": 119
}

2.- Usar dicho token para acceder a la api
	
	Headers:
		Accept application/json
	
	URL:
		localhost:8081/dalgs/api/activity/1?access_token=a4c50bfe-d1e2-4224-abfa-587fc6a90608
		



