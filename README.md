---		TFG Project (May)	---

//TO-DO
0.- Añadir coleccion de actividades externas a curso y grupo (mover de una coleccion a otra)
1.- Permitir a profesores y coordinadores importar users a un grupo concreto
2.- Añadir archivos pdf, doc....  enunciados de las actividades (campo url). 
		Para ello rutas relativas a WEB-INF/attachments
		Hay que declarar en el xml el rootpath, el nombre del doc sera el codigo de la actividad
3.- Memoria

//NOT WORKING

Optional: Change the pagination
0.- XAPI --> http://experienceapi.com
1.- OAuth mediante Google - Gestionar un buzón de correo
2.- eGarante
3.- Notificacion de actividades nuevas pnotify

// DONE


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
		



