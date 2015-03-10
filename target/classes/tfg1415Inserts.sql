-- ACL Classes -- The name of the class of the domain objects we have in the ACL system
INSERT INTO acl_class (id, class) VALUES (1,'es.ucm.fdi.dalgs.domain.AcademicTerm');
INSERT INTO acl_class (id, class) VALUES (2,'es.ucm.fdi.dalgs.domain.Activity');
INSERT INTO acl_class (id, class) VALUES (3,'es.ucm.fdi.dalgs.domain.Competence');
INSERT INTO acl_class (id, class) VALUES (4,'es.ucm.fdi.dalgs.domain.Course');
INSERT INTO acl_class (id, class) VALUES (5,'es.ucm.fdi.dalgs.domain.Degree');
INSERT INTO acl_class (id, class) VALUES (6,'es.ucm.fdi.dalgs.domain.Group');
INSERT INTO acl_class (id, class) VALUES (7,'es.ucm.fdi.dalgs.domain.LearningGoal');
INSERT INTO acl_class (id, class) VALUES (8,'es.ucm.fdi.dalgs.domain.Module');
INSERT INTO acl_class (id, class) VALUES (9,'es.ucm.fdi.dalgs.domain.Subject');
INSERT INTO acl_class (id, class) VALUES (10,'es.ucm.fdi.dalgs.domain.Topic');
INSERT INTO acl_class (id, class) VALUES (11,'es.ucm.fdi.dalgs.domain.User');
INSERT INTO acl_class (id, class) VALUES (12, 'es.ucm.fdi.dalgs.classes.ResultClass');
-- ---------------------------------------------------

-- ACL Sid -- Authorizations - Roles
INSERT INTO acl_sid (id, principal, sid) VALUES	(2,00000000,'ROLE_ADMIN');
INSERT INTO acl_sid (id, principal, sid) VALUES	(3,00000000,'ROLE_USER');
INSERT INTO acl_sid (id, principal, sid) VALUES	(1,00000001,'admin');
	
-- -------------------------------------------------------

-- Degree Inserts 
INSERT INTO degree (id_degree,code_degree,description,name) VALUES (1,'DEG1','Grado en Ingenieria del Software','GIS');
INSERT INTO degree (id_degree,code_degree,description,name) VALUES (2,'DEG2','Grado en Ingenieria Informatica','GII');
INSERT INTO degree (id_degree,code_degree,description,name) VALUES (3,'DEG3','Grado en Ingenieria de Computadores','GIC');

-- Object Identity
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (1,00000001,1,5,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (2,00000001,2,5,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (3,00000001,3,5,NULL,1);

-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (1,0,00000000,00000000,00000001,16,1,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (2,1,00000000,00000000,00000001,8,1,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (3,2,00000000,00000000,00000001,1,1,3);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (4,0,00000000,00000000,00000001,16,2,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (5,1,00000000,00000000,00000001,8,2,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (6,2,00000000,00000000,00000001,1,2,3);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (7,0,00000000,00000000,00000001,16,3,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (8,1,00000000,00000000,00000001,8,3,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (9,2,00000000,00000000,00000001,1,3,3);
-- -----------------------------------------------------

-- Academic Term Inserts - Fill into acl domain
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (1, '2014-2015', '1');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (2, '2014-2015', '2');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (3, '2014-2015', '3');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (4, '2015-2016', '1');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (5, '2015-2016', '2');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (6, '2015-2016', '3');

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (4,00000001,1,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (5,00000001,2,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (6,00000001,3,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (7,00000001,4,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (8,00000001,5,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (9,00000001,6,1,NULL,1);
	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (10,0,00000000,00000000,00000001,16,4,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (11,1,00000000,00000000,00000001,8,4,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (12,2,00000000,00000000,00000001,1,4,3);
	
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (13,0,00000000,00000000,00000001,16,5,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (14,1,00000000,00000000,00000001,8,5,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (15,2,00000000,00000000,00000001,1,5,3);
	
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (16,0,00000000,00000000,00000001,16,6,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (17,1,00000000,00000000,00000001,8,6,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (18,2,00000000,00000000,00000001,1,6,3);
	
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (19,0,00000000,00000000,00000001,16,7,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (20,1,00000000,00000000,00000001,8,7,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (21,2,00000000,00000000,00000001,1,7,3);
	
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (22,0,00000000,00000000,00000001,16,8,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (23,1,00000000,00000000,00000001,8,8,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (24,2,00000000,00000000,00000001,1,8,3);
	
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (25,0,00000000,00000000,00000001,16,9,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (26,1,00000000,00000000,00000001,8,9,2);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (27,2,00000000,00000000,00000001,1,9,3);
	

-- ----------------------------------------------------------------------------------

--Module Inserts

INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (1, 'MOD1', 'Materias Básicas', 'description1', 1);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (2, 'MOD2', 'Materias comunes a la informatica','description2', 1);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (3, 'MOD3', 'Tecnologia especifica','description3', 1);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (4, 'MOD4', 'Complementario', 'description4',1);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (5, 'MOD5', 'Optativo', 'description5',1);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (6, 'MOD6', 'Trabajo fin de grado','description6', 1);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (7, 'MOD7', 'Materias comunes a la informatica','description7', 1);

INSERT INTO module (id_module, code_module, name, description, id_degree) VALUES (8, 'MOD8', 'Materias Básicas','description8', 2);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (9, 'MOD9', 'Materias comunes a la informatica','description9', 2);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (10, 'MOD10', 'Tecnologia especifica', 'description10',2);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (11, 'MOD11', 'Complementario', 'description11',2);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (12, 'MOD12', 'Optativo','description12', 2);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (13, 'MOD13', 'Trabajo fin de grado','description13', 2);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (14, 'MOD14', 'Materias comunes a la informatica','description14', 2);

INSERT INTO module (id_module, code_module, name, description, id_degree) VALUES (15, 'MOD15', 'Materias Básicas','description15', 3);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (16, 'MOD16', 'Materias comunes a la informatica','description16', 3);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (17, 'MOD17', 'Tecnologia especifica','description17', 3);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (18, 'MOD18', 'Complementario','description18', 3);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (19, 'MOD19', 'Optativo', 'description19',3);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (20, 'MOD20', 'Trabajo fin de grado','description20', 3);
INSERT INTO module (id_module, code_module, name, description,id_degree) VALUES (21, 'MOD21', 'Materias comunes a la informatica','description21',  3);

--Topic Inserts

INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (1, 'TOP1', 'Fisica', 'description1',1);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (2, 'TOP2', 'Empresa', 'description2',2);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (3, 'TOP3', 'Informatica','description3', 3);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (4, 'TOP4', 'Matematicas','description4', 1);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (5, 'TOP5', 'Sistemas operativos y redes fundamentales','description5', 3);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (6, 'TOP6', 'Métodos Estadísticos y de Investigación Operativa','description6', 15);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (7, 'TOP7', 'Desarrollo del software fundamental','description7', 21);

-- Subject Inserts
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (1,'SUB1','Ingenieria Software','IS',7);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (2,'SUB2','Bases de Datos','BD',7);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (3,'SUB3','Sistemas Operativos','SO',3);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (4,'SUB4','Ingenieria Software','IS',3);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (5,'SUB5','Bases de Datos','BD',3);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (6,'SUB6','Sistemas Operativos','SO',1); 
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (7,'SUB7','Ingenieria Software','IS',1);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (8,'SUB8','Bases de Datos','BD',5);
INSERT INTO subject (id_subject,code_subject,description,name, id_topic) VALUES (9,'SUB9','Sistemas Operativos','SO',5);

--Degree-subjects Inserts
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (1,1);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (1,2);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (3,4);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (1,6);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (2,3);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (2,7);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (2,8);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (3,5);
--INSERT INTO degree_subject(degree_id_degree, subjects_id_subject) VALUES (3,9);



-- Course Inserts
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (1, '1', '1');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (2, '1', '2');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (3, '1', '3');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (4, '2', '1');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (5, '2', '5');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (6, '3', '7');

-- Competence Inserts 
INSERT INTO competence (id_competence,code_competence,description,name,id_degree) VALUES (1,'COMP1','adquirir conocimientos en UML','UML',1);
INSERT INTO competence (id_competence,code_competence,description,name,id_degree) VALUES (2,'COMP2','aprender diversos patrones de Ingenieria Software','Patrones',1);
INSERT INTO competence (id_competence,code_competence,description,name,id_degree) VALUES (3,'COMP3','Saber diseñar una base de datos relacional','Base datos relacional',2);
INSERT INTO competence (id_competence,code_competence,description,name,id_degree) VALUES (4,'COMP4','Adquirir conocimientos en consultas contra una base de datos','Consultas SQL',2);
INSERT INTO competence (id_competence,code_competence,description,name,id_degree) VALUES (5,'COMP5','Aprendizaje de sincronizacion y comunicacion de procesos','Sincronizacion de Procesos',3);

--LearningGoal Inserts
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (1, 'LEA1', 'learning 1','descripcion learning 1', 1);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (2, 'LEA2', 'learning 2','descripcion learning 2', 1);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (3, 'LEA3', 'learning 3','descripcion learning 3', 2);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (4, 'LEA4', 'learning 4','descripcion learning 4', 2);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (5, 'LEA5', 'learning 5','descripcion learning 5', 2);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (6, 'LEA6', 'learning 6','descripcion learning 6', 3);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (7, 'LEA7', 'learning 7','descripcion learning 7', 4);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (8, 'LEA8', 'learning 8','descripcion learning 8', 5);


--Activity Inserts 
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (1,'ACT1','Desarrollar diagramas de casos de uso','Casos de Uso',1);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (2,'ACT2','Elaborar un diagrama Entidad-Relacion','Entidad-Relacion',1);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (3,'ACT3','Diagramas de secuencia','Secuencia',2);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (4,'ACT4','Diagramas de activitidades','actividades',2);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (5,'ACT5','Diagrama de clases','clases',3);

-- Activity Inserts 
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, percentage) VALUES ('1', 1,'10');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, percentage) VALUES ('2', 3,'20');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, percentage) VALUES ('3', 1,'30');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, percentage) VALUES ('4', 3, '40');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, percentage) VALUES ('5', 5,'20');

-- Subject_Competence Inserts 
INSERT INTO subject_competence(id_subject,id_competence) VALUES (1,1);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (1,2);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,1);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,3);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,4);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (3,5);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (5,5);





--Group Inserts
INSERT INTO _group (id_group, name, id_course) VALUES (1, 'group 1', 1);
INSERT INTO _group (id_group, name, id_course) VALUES (2, 'group 2', 1);
INSERT INTO _group (id_group, name, id_course) VALUES (3, 'group 3', 2);
INSERT INTO _group (id_group, name, id_course) VALUES (4, 'group 4', 2);
INSERT INTO _group (id_group, name, id_course) VALUES (5, 'group 5', 3);

-- User - Role Inserts  ROLE_USER(2) ROLE_ADMIN(1)

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username) VALUES (1, true, true, true, 'admin@ucm.es', true, 'adminFirst', 'adminLast', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', null, 'admin');

INSERT INTO user_roles (user, role) VALUES(1, 'ROLE_USER');
INSERT INTO user_roles (user, role) VALUES(1, 'ROLE_ADMIN');


INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username) VALUES (2, true, true, true, 'student@ucm.es', true, 'studentFirst', 'studentLast', '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb', null, 'student');

INSERT INTO user_roles (user, role) VALUES(2, 'ROLE_USER');
INSERT INTO user_roles (user, role) VALUES(2, 'ROLE_STUDENT');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username) VALUES (3, true, true, true, 'professor@ucm.es', true, 'professorFirst', 'professorLast', '17c1532ca6cff8f6a3a8200028af6c2580bf37f39e10cb0966e8a573e3b24a1f', null, 'professor');

INSERT INTO user_roles (user, role) VALUES(3, 'ROLE_USER');
INSERT INTO user_roles (user, role) VALUES(3, 'ROLE_PROFESSOR');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username) VALUES (4, true, true, true, 'user@ucm.es', true, 'userFirst', 'userLast', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', null, 'user');

INSERT INTO user_roles (user, role) VALUES(4, 'ROLE_USER');















