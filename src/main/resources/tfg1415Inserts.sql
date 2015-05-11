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
INSERT INTO acl_class (id, class) VALUES (12,'es.ucm.fdi.dalgs.domain.ExternalActivity');
-- ---------------------------------------------------

-- ACL Sid -- Authorizations - Roles
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
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (2,1,00000000,00000000,00000001,16,1,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (5,1,00000000,00000000,00000001,16,2,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (8,1,00000000,00000000,00000001,16,3,1);
-- -----------------------------------------------------

-- Academic Term Inserts - Fill into acl domain
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (1, '2014/2015', '1');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (2, '2014/2015', '2');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (3, '2014/2015', '3');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (4, '2015/2016', '1');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (5, '2015/2016', '2');
INSERT INTO academicterm (id_academicterm,term, id_degree) VALUES (6, '2015/2016', '3');

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (4,00000001,1,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (5,00000001,2,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (6,00000001,3,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (7,00000001,4,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (8,00000001,5,1,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (9,00000001,6,1,NULL,1);
	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (11,1,00000000,00000000,00000001,16,4,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (14,1,00000000,00000000,00000001,16,5,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (17,1,00000000,00000000,00000001,16,6,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (20,1,00000000,00000000,00000001,16,7,1);	
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (23,1,00000000,00000000,00000001,16,8,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (26,1,00000000,00000000,00000001,16,9,1);
	

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

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (10,00000001,1,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (11,00000001,2,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (12,00000001,3,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (13,00000001,4,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (14,00000001,5,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (15,00000001,6,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (16,00000001,7,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (17,00000001,8,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (18,00000001,9,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (19,00000001,10,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (20,00000001,11,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (21,00000001,12,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (22,00000001,13,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (23,00000001,14,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (24,00000001,15,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (25,00000001,16,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (26,00000001,17,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (27,00000001,18,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (28,00000001,19,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (29,00000001,20,8,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (30,00000001,21,8,NULL,1);
	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (29,1,00000000,00000000,00000001,16,10,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (32,1,00000000,00000000,00000001,16,11,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (35,1,00000000,00000000,00000001,16,12,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (38,1,00000000,00000000,00000001,16,13,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (41,1,00000000,00000000,00000001,16,14,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (44,1,00000000,00000000,00000001,16,15,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (47,1,00000000,00000000,00000001,16,16,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (50,1,00000000,00000000,00000001,16,17,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (53,1,00000000,00000000,00000001,16,18,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (56,1,00000000,00000000,00000001,16,19,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (59,1,00000000,00000000,00000001,16,20,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (62,1,00000000,00000000,00000001,16,21,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (59,1,00000000,00000000,00000001,16,22,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (62,1,00000000,00000000,00000001,16,23,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (59,1,00000000,00000000,00000001,16,24,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (62,1,00000000,00000000,00000001,16,25,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (65,1,00000000,00000000,00000001,16,26,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (68,1,00000000,00000000,00000001,16,27,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (71,1,00000000,00000000,00000001,16,28,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (74,1,00000000,00000000,00000001,16,29,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (77,1,00000000,00000000,00000001,16,30,1);


--Topic Inserts

INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (1, 'TOP1', 'Fisica', 'description1',1);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (2, 'TOP2', 'Empresa', 'description2',2);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (3, 'TOP3', 'Informatica','description3', 3);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (4, 'TOP4', 'Matematicas','description4', 1);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (5, 'TOP5', 'Sistemas operativos y redes fundamentales','description5', 3);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (6, 'TOP6', 'Métodos Estadísticos y de Investigación Operativa','description6', 15);
INSERT INTO topic (id_topic, code_topic, name, description,id_module) VALUES (7, 'TOP7', 'Desarrollo del software fundamental','description7', 21);

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (31,00000001,1,10,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (32,00000001,2,10,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (33,00000001,3,10,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (34,00000001,4,10,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (35,00000001,5,10,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (36,00000001,6,10,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (37,00000001,7,10,NULL,1);

	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (80,1,00000000,00000000,00000001,16,31,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (83,1,00000000,00000000,00000001,16,32,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (86,1,00000000,00000000,00000001,16,33,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (89,1,00000000,00000000,00000001,16,34,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (92,1,00000000,00000000,00000001,16,35,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (95,1,00000000,00000000,00000001,16,36,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (98,1,00000000,00000000,00000001,16,37,1);

-- Subject Inserts
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (1,'SUB1',12,'Ingenieria Software','IS', 'url',7);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (2,'SUB2',6,'Bases de Datos','BD','url',7);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (3,'SUB3',6,'Sistemas Operativos','SO','url',3);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (4,'SUB4',9,'Ingenieria Software','IS','url',3);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (5,'SUB5',6,'Bases de Datos','BD','url',3);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (6,'SUB6',6,'Sistemas Operativos','SO','url',1); 
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (7,'SUB7',12,'Ingenieria Software','IS','url',1);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (8,'SUB8',6,'Bases de Datos','BD','url',5);
INSERT INTO subject (id_subject,code_subject,credits,description,name,url_doc, id_topic) VALUES (9,'SUB9',6,'Sistemas Operativos','SO','url',5);

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (38,00000001,1,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (39,00000001,2,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (40,00000001,3,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (41,00000001,4,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (42,00000001,5,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (43,00000001,6,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (44,00000001,7,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (45,00000001,8,9,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (46,00000001,9,9,NULL,1);

	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (101,1,00000000,00000000,00000001,16,38,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (104,1,00000000,00000000,00000001,16,39,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (107,1,00000000,00000000,00000001,16,40,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (110,1,00000000,00000000,00000001,16,41,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (113,1,00000000,00000000,00000001,16,42,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (116,1,00000000,00000000,00000001,16,43,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (119,1,00000000,00000000,00000001,16,44,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (122,1,00000000,00000000,00000001,16,45,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (125,1,00000000,00000000,00000001,16,46,1);

-- Course Inserts
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (1, '1', '1');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (2, '1', '2');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (3, '1', '3');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (4, '2', '1');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (5, '2', '5');
INSERT INTO course (id_course, id_academicterm, id_subject) VALUES (6, '3', '7');


-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (47,00000001,1,4,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (48,00000001,2,4,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (49,00000001,3,4,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (50,00000001,4,4,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (51,00000001,5,4,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (52,00000001,6,4,NULL,1);

	
-- Acl Entry

INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (128,1,00000000,00000000,00000001,16,47,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (131,1,00000000,00000000,00000001,16,48,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (134,1,00000000,00000000,00000001,16,49,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (137,1,00000000,00000000,00000001,16,50,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (140,1,00000000,00000000,00000001,16,51,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (143,1,00000000,00000000,00000001,16,52,1);

	
-- Competence Inserts 
INSERT INTO competence (id_competence,code_competence,description,name, _type, id_degree) VALUES (1,'COMP1','adquirir conocimientos en UML','UML', 0, 1);
INSERT INTO competence (id_competence,code_competence,description,name,_type,id_degree) VALUES (2,'COMP2','aprender diversos patrones de Ingenieria Software','Patrones', 1,1);
INSERT INTO competence (id_competence,code_competence,description,name,_type,id_degree) VALUES (3,'COMP3','Saber diseñar una base de datos relacional','Base datos relacional',0,2);
INSERT INTO competence (id_competence,code_competence,description,name,_type,id_degree) VALUES (4,'COMP4','Adquirir conocimientos en consultas contra una base de datos','Consultas SQL',2,2);
INSERT INTO competence (id_competence,code_competence,description,name,_type,id_degree) VALUES (5,'COMP5','Aprendizaje de sincronizacion y comunicacion de procesos','Sincronizacion de Procesos',3,3);

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (53,00000001,1,3,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (54,00000001,2,3,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (55,00000001,3,3,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (56,00000001,4,3,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (57,00000001,5,3,NULL,1);

-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (146,1,00000000,00000000,00000001,16,53,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (149,1,00000000,00000000,00000001,16,54,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (152,1,00000000,00000000,00000001,16,55,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (155,1,00000000,00000000,00000001,16,56,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (158,1,00000000,00000000,00000001,16,57,1);

--LearningGoal Inserts
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (1, 'LEA1', 'learning 1','descripcion learning 1', 1);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (2, 'LEA2', 'learning 2','descripcion learning 2', 1);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (3, 'LEA3', 'learning 3','descripcion learning 3', 2);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (4, 'LEA4', 'learning 4','descripcion learning 4', 2);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (5, 'LEA5', 'learning 5','descripcion learning 5', 2);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (6, 'LEA6', 'learning 6','descripcion learning 6', 3);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (7, 'LEA7', 'learning 7','descripcion learning 7', 4);
INSERT INTO learninggoal (id_learninggoal, code_learning, name, description, id_competence) VALUES (8, 'LEA8', 'learning 8','descripcion learning 8', 5);

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (58,00000001,1,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (59,00000001,2,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (60,00000001,3,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (61,00000001,4,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (62,00000001,5,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (63,00000001,6,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (64,00000001,7,7,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (65,00000001,8,7,NULL,1);

	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (161,1,00000000,00000000,00000001,16,58,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (164,1,00000000,00000000,00000001,16,59,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (167,1,00000000,00000000,00000001,16,60,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (170,1,00000000,00000000,00000001,16,61,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (173,1,00000000,00000000,00000001,16,62,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (176,1,00000000,00000000,00000001,16,63,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (179,1,00000000,00000000,00000001,16,64,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (182,1,00000000,00000000,00000001,16,65,1);

--Activity Inserts 
INSERT INTO _activity (id_activity,code_activity,description,name,id_course) VALUES (1,'ACT1','Desarrollar diagramas de casos de uso','Casos de Uso',1);
INSERT INTO _activity (id_activity,code_activity,description,name,id_course) VALUES (2,'ACT2','Elaborar un diagrama Entidad-Relacion','Entidad-Relacion',1);
INSERT INTO _activity (id_activity,code_activity,description,name,id_course) VALUES (3,'ACT3','Diagramas de secuencia','Secuencia',2);
INSERT INTO _activity (id_activity,code_activity,description,name,id_course) VALUES (4,'ACT4','Diagramas de activitidades','actividades',2);
INSERT INTO _activity (id_activity,code_activity,description,name,id_course) VALUES (5,'ACT5','Diagrama de clases','clases',3);


INSERT INTO course_activities (id_course,id_activity) VALUES (1,1);
INSERT INTO course_activities (id_course,id_activity) VALUES (1,2);

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (66,00000001,1,2,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (67,00000001,2,2,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (68,00000001,3,2,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (69,00000001,4,2,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (70,00000001,5,2,NULL,1);

	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (185,1,00000000,00000000,00000001,16,66,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (188,1,00000000,00000000,00000001,16,67,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (191,1,00000000,00000000,00000001,16,68,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (194,1,00000000,00000000,00000001,16,69,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (197,1,00000000,00000000,00000001,16,70,1);

--Group Inserts
INSERT INTO _group (id_group, name, id_course) VALUES (1, 'group 1', 1);
INSERT INTO _group (id_group, name, id_course) VALUES (2, 'group 2', 1);
INSERT INTO _group (id_group, name, id_course) VALUES (3, 'group 3', 2);
INSERT INTO _group (id_group, name, id_course) VALUES (4, 'group 4', 2);
INSERT INTO _group (id_group, name, id_course) VALUES (5, 'group 5', 3);

-- Object Identity --
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (71,00000001,1,6,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (72,00000001,2,6,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (73,00000001,3,6,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (74,00000001,4,6,NULL,1);
INSERT INTO acl_object_identity (ID, ENTRIES_INHERITING, OBJECT_ID_IDENTITY, OBJECT_ID_CLASS, PARENT_OBJECT, OWNER_SID) VALUES (75,00000001,5,6,NULL,1);

	
-- Acl Entry
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (200,1,00000000,00000000,00000001,16,71,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (203,1,00000000,00000000,00000001,16,72,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (206,1,00000000,00000000,00000001,16,73,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (209,1,00000000,00000000,00000001,16,74,1);
INSERT INTO acl_entry (ID, ACE_ORDER, AUDIT_FAILURE, AUDIT_SUCCESS, GRANTING, MASK, ACL_OBJECT_IDENTITY, SID) VALUES (212,1,00000000,00000000,00000001,16,75,1);

-- Activity_LearningGoalStatus Inserts 
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, weight) VALUES ('1', 1,'10');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, weight) VALUES ('2', 3,'20');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, weight) VALUES ('3', 1,'30');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, weight) VALUES ('4', 3, '40');
INSERT INTO activity_learninggoalstatus (id_activity, learninggoal_id_learninggoal, weight) VALUES ('5', 5,'20');

-- Subject_Competence Inserts 
INSERT INTO subject_competence(id_subject,id_competence) VALUES (1,1);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (1,2);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,1);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,3);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (2,4);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (3,5);
INSERT INTO subject_competence(id_subject,id_competence) VALUES (5,5);

-- User - Role Inserts  ROLE_USER(2) ROLE_ADMIN(1)

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (1, true, true, true, 'admin@ucm.es', true, 'adminFirst', 'adminLast', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', null, 'admin', 'adminLast, adminFirst - admin');
INSERT INTO user_roles (user, role) VALUES(1, 'ROLE_ADMIN');
INSERT INTO user_roles (user, role) VALUES(1, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (2, true, true, true, 'student@ucm.es', true, 'studentFirst', 'studentLast', '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb', null, 'student', 'studentLast, studentFirst - student');
INSERT INTO user_roles (user, role) VALUES(2, 'ROLE_STUDENT');
INSERT INTO user_roles (user, role) VALUES(2, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (3, true, true, true, 'professor@ucm.es', true, 'professorFirst', 'professorLast', '17c1532ca6cff8f6a3a8200028af6c2580bf37f39e10cb0966e8a573e3b24a1f', null, 'professor', 'professorLast, professorFirst - professor');
INSERT INTO user_roles (user, role) VALUES(3, 'ROLE_PROFESSOR');
INSERT INTO user_roles (user, role) VALUES(3, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (4, true, true, true, 'user@ucm.es', true, 'userFirst', 'userLast', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', null, 'user', 'userLast, userFirst - user');
INSERT INTO user_roles (user, role) VALUES(4, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (5, true, true, true, 'professor2@ucm.es', true, 'professor2First', 'professor2Last', '17c1532ca6cff8f6a3a8200028af6c2580bf37f39e10cb0966e8a573e3b24a1f', null, 'professor2', 'professor2Last, professor2First - professor2');
INSERT INTO user_roles (user, role) VALUES(5, 'ROLE_PROFESSOR');
INSERT INTO user_roles (user, role) VALUES(5, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (6, true, true, true, 'coordinator@ucm.es', true, 'coordinatorFirst', 'coordinatorLast', '17c1532ca6cff8f6a3a8200028af6c2580bf37f39e10cb0966e8a573e3b24a1f', null, 'coordinator', 'coordinatorLast, coordinatorFirst - coordinator');
INSERT INTO user_roles (user, role) VALUES(6, 'ROLE_PROFESSOR');
INSERT INTO user_roles (user, role) VALUES(6, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (7, true, true, true, 'professor3@ucm.es', true, 'professor3First', 'professor3Last', '17c1532ca6cff8f6a3a8200028af6c2580bf37f39e10cb0966e8a573e3b24a1f', null, 'professor3', 'professor3Last, professor3First - professor3');
INSERT INTO user_roles (user, role) VALUES(7, 'ROLE_PROFESSOR');
INSERT INTO user_roles (user, role) VALUES(7, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (8, true, true, true, 'student2@ucm.es', true, 'student2First', 'student2Last', '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb', null, 'student2', 'student2Last, student2First - student2');
INSERT INTO user_roles (user, role) VALUES(8, 'ROLE_STUDENT');
INSERT INTO user_roles (user, role) VALUES(8, 'ROLE_USER');

INSERT INTO user (id_user, accountNonExpired, accountNonLocked, credentialsNonExpired, email, enabled, firstname, lastname, password, salt, username, fullname) VALUES (9, true, true, true, 'student3@ucm.es', true, 'student3First', 'student3Last', '264c8c381bf16c982a4e59b0dd4c6f7808c51a05f64c35db42cc78a2a72875bb', null, 'student3', 'student3Last, student3First - student3');
INSERT INTO user_roles (user, role) VALUES(9, 'ROLE_STUDENT');
INSERT INTO user_roles (user, role) VALUES(9, 'ROLE_USER');




--	Student_Group

-- 	Professor_Group

--	Coordinator_Course




