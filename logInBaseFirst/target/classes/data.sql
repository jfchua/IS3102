--INSERT INTO user (email, password_hash, role)
--VALUES ('demo@localhost', '$2a$10$ebyC4Z5WtCXXc.HGDc1Yoe6CLFzcntFmfse6/pTj7CeDY5I05w16C', 'ADMIN');
--INSERT INTO user (email, password_hash, role)
--VALUES ('1@1', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'USER');
--INSERT INTO user (email, password_hash, role)
--VALUES ('2@2', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'USER');
--INSERT INTO user (email, password_hash, role)
--VALUES ('event@event', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'EVENT');
--INSERT INTO user (email, password_hash, role)
--VALUES ('kenneth1399@hotmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'USER');
insert into role (name)values ("ROLE_SUPERADMIN");
insert into role (name)values ("ROLE_ADMIN");
insert into role (name)values ("ROLE_USER");
insert into role (name)values ("ROLE_EVENT");
insert into role (name)values ("ROLE_PROPERTY");
insert into role (name)values ("ROLE_FINANCE");
insert into role (name)values ("ROLE_TICKETING");
insert into role (name)values ("ROLE_EXTEVE");
 

insert into client_organisation (organisation_name) values ("Expo");
insert into client_organisation (organisation_name) values ("Suntec");
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('superadmin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1 ,'Super Admin');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('admin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Admin');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('user@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'User');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('event@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'event');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('kenneth1399@hotmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'lim');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('property@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'property manager');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('finance@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'finance manager');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('ticketing@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'ticketing manager');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('exteve@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'external event organiser');

INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('suntecadmin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec Admin');
INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('suntecall@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec All roles');

--Assign roles to users for testing
INSERT into users_roles values( 1,1);
INSERT into users_roles values( 1,3);
INSERT into users_roles values( 2,2);
INSERT into users_roles values( 2,3);
INSERT into users_roles values( 3,3);
INSERT into users_roles values( 4,3);
INSERT into users_roles values( 4,4);
INSERT into users_roles values( 4,5);
INSERT into users_roles values( 4,8);
INSERT into users_roles values( 5,3);
INSERT into users_roles values( 6,5);
INSERT into users_roles values( 6,3);
INSERT into users_roles values( 7,6);
INSERT into users_roles values( 7,3);
INSERT into users_roles values( 8,7);
INSERT into users_roles values( 8,3);
INSERT into users_roles values( 9,8);
INSERT into users_roles values( 9,3);
INSERT into users_roles values( 10,1);
INSERT into users_roles values( 10,3);
INSERT into users_roles values( 11,3);
INSERT into users_roles values( 11,4);
INSERT into users_roles values( 11,5);
INSERT into users_roles values( 11,6);
INSERT into users_roles values( 11,7);
INSERT into users_roles values( 11,8);

--INSERT INTO message (subject,sender,recipient,message) values('subject','1@1','1@1','test');
--INSERT INTO message (subject,sender,recipient,message) values('pikachu','dragonite','1@1','charmander');
INSERT INTO todotask(date,task) values('2016-10-12','Do work'); 
INSERT INTO user_to_do_list (user_id,to_do_list_id) values('2','1');
INSERT INTO todotask(date,task) values('2016-09-30', 'Submit report'); 
INSERT INTO user_to_do_list (user_id,to_do_list_id) values('2','2');
INSERT INTO todotask(date,task) values('2016-10-12','Find manager'); 
INSERT INTO user_to_do_list (user_id,to_do_list_id) values('2','3');
INSERT INTO todotask(date,task) values('2016-10-12','Add event'); 
INSERT INTO user_to_do_list (user_id,to_do_list_id) values('2','4');


insert into message (message, sender_name,subject,recipient_id)values("Please change your password every 28 days for security.","admin@localhost","IMPORTANT",3);
insert into message (message, sender_name,subject,recipient_id)values("Hello World","user@localhost","test Message",2);
insert into message (message, sender_name,subject,recipient_id)values("This event needs some modification, reply me asap.","event@localhost","Event Message",6);
insert into message (message, sender_name,subject,recipient_id)values("The pricing model seems a little off, can we discuss this later?","finance@localhost","Finance matter",6);


--insert into building(name, address, postal_code, city, num_floor, pic_path) values("buildingName", "MINGSHA", 12345,"SINGAPORE", 6, "");
--insert into level(file_path,level_length,level_num, width,building) values ("",1,2,3,1);


--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('111',1,2,true,3,4);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('123',1,2,true,5,4);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('1123',1,2,true,6,4);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('333',1,2,true,7,4);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('1771',1,2,true,8,4);

INSERT INTO client_organisation_system_subscriptions VALUES ('1', 'Finance System');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', 'Event Management System');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', 'Property System');

INSERT INTO client_organisation_system_subscriptions VALUES ('2', 'Finance System');
INSERT INTO client_organisation_system_subscriptions VALUES ('2', 'Event Management System');
INSERT INTO client_organisation_system_subscriptions VALUES ('2', 'Property System');






