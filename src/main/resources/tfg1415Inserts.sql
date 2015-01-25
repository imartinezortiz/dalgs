-- Degree Inserts 
INSERT INTO degree (id_degree,code_degree,description,name) VALUES (1,'DEG1','Grado en Ingenieria del Software','GIS');
INSERT INTO degree (id_degree,code_degree,description,name) VALUES (2,'DEG2','Grado en Ingenieria Informatica','GII');
INSERT INTO degree (id_degree,code_degree,description,name) VALUES (3,'DEG3','Grado en Ingenieria de Computadores','GIC');

-- Academic Term Inserts
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (1, '2014-2015', '1');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (2, '2014-2015', '2');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (3, '2014-2015', '3');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (4, '2015-2016', '1');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (5, '2015-2016', '2');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (6, '2015-2016', '3');

-- Subject Inserts
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (1,'SUB1','Ingenieria Software','IS',1);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (2,'SUB2','Bases de Datos','BD', 1);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (3,'SUB3','Sistemas Operativos','SO', 1);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (4,'SUB4','Ingenieria Software','IS',2);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (5,'SUB5','Bases de Datos','BD', 2);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (6,'SUB6','Sistemas Operativos','SO', 2); 
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (7,'SUB7','Ingenieria Software','IS',3);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (8,'SUB8','Bases de Datos','BD', 3);
INSERT INTO subject (id_subject,code_subject,description,name, id_degree) VALUES (9,'SUB9','Sistemas Operativos','SO', 3);

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


--Activity Inserts 
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (1,'ACT1','Desarrollar diagramas de casos de uso','Casos de Uso',1);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (2,'ACT2','Elaborar un diagrama Entidad-Relacion','Entidad-Relacion',1);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (3,'ACT1','Desarrollar diagramas de casos de uso','Casos de Uso',2);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (4,'ACT2','Elaborar un diagrama Entidad-Relacion','Entidad-Relacion',2);
INSERT INTO activity (id_activity,code_activity,description,name,id_course) VALUES (5,'ACT3','Elaborar scripts en c','Lenguaje c',3);

-- Activity Inserts 
INSERT INTO activity_competencestatus (id_activity, competence_id_competence, percentage) VALUES ('1', 1,'10');
INSERT INTO activity_competencestatus (id_activity, competence_id_competence, percentage) VALUES ('2', 3,'20');
INSERT INTO activity_competencestatus (id_activity, competence_id_competence, percentage) VALUES ('3', 1,'30');
INSERT INTO activity_competencestatus (id_activity, competence_id_competence, percentage) VALUES ('4', 3, '40');
INSERT INTO activity_competencestatus (id_activity, competence_id_competence, percentage) VALUES ('5', 5,'20');

-- Subject_Competence Inserts 
INSERT INTO subject_competence(id_subject,id_competence) VALUES (1,1)
INSERT INTO subject_competence(id_subject,id_competence) VALUES (1,2)
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,1)
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,3)
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,4)
INSERT INTO subject_competence(id_subject,id_competence) VALUES (3,5)
INSERT INTO subject_competence(id_subject,id_competence) VALUES (5,5)


-- User - Role Inserts  ROLE_USER(2) ROLE_ADMIN(1)
INSERT INTO user (id, firstName, lastName, password, username) VALUES ('1', 'first', 'last', 'admin', 'admin');
INSERT INTO role (id, role, user_id) VALUES (NULL, '1', '1');

INSERT INTO user (id, firstName, lastName, password, username) VALUES ('2', 'first', 'last', 'user', 'user');
INSERT INTO role (id, role, user_id) VALUES (NULL, '2', '2');











