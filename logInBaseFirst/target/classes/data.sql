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
--10 roles
insert into role (name)values ("ROLE_SUPERADMIN");
insert into role (name)values ("ROLE_ADMIN");
insert into role (name)values ("ROLE_USER");
insert into role (name)values ("ROLE_EVENT");
insert into role (name)values ("ROLE_PROPERTY");
insert into role (name)values ("ROLE_FINANCE");
insert into role (name)values ("ROLE_TICKETING");
insert into role (name)values ("ROLE_EXTEVE");
insert into role (name)values ("ROLE_HIGHER");
insert into role (name)values ("ROLE_EVEGOER");
 
INSERT INTO payment_policy(deposit_rate, interim_period, due_days, subsequent_number)
VALUES(0.4, 2, 2, 2);

--ALTER TABLE client_organisation ENGINE=Innodb;
insert into client_organisation (organisation_name, address, end_date, fee, postal, phone, start_date, payment_policy_id,themecolour,logo_file_path) 
values ("Expo", "1 Expo Dr", "2017-06-30 22:00:00", "45000.00", "486150", "64032160", "2016-07-01 22:00:00", 1,"midnight-blue","expoLogo.png");
insert into client_organisation (organisation_name, address, end_date, fee, postal, phone, start_date, themecolour,logo_file_path) 
values ("Suntec", "3 Temasek Boulevard", "2017-06-30 22:00:00", "25000.00", "038983", "68221537", "2016-07-01 22:00:00", "green","suntecLogo.png");

--Algattas is client_organisation 3
insert into client_organisation (organisation_name, address, end_date, fee, postal, phone, start_date, themecolour,logo_file_path) 
values ("Algattas", "73 Ubi Road 1","2099-06-30 22:00:00","0.00","408733", "31580456","2010-07-01 22:00:00","red","algattasLogo.png");
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('superadmin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',3 ,'Kenneth LIM' , '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6','What is your favourite number' );
--Algattas superadmin is user 1
--User 2 to 9
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('admin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Jin Fa CHUA' , '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your mother''s maiden name');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('user@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Kok Hwee TAN', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('event@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Hailing ZHANG', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite food');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('jaydentkh@gmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'JayDen TAN', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite animal');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('property@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'property manager', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('finance@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'finance manager', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ticketing@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'ticketing manager', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('exteve@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'external event organiser', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
--User 10 to 11
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('suntecadmin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec Admin', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('suntecall@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec All roles', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
--User 12 to 13
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('tkgs.zhao.mingsha@gmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Mingsha ZHAO', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('suntecexternal@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec external manager', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
--User 14 to 21
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer1@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZMS1', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer2@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZHL2', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer3@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'TKH3', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer4@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'CJF4', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer5@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'KLZW5', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer6@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZMS6', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer7@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZHL7', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('goer8@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'CHS8', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
--User 22 to 29
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext1@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZMS11', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext2@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZHL22', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext3@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'TKH33', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext4@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'CJF44', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext5@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'KLZW55', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext6@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZMS66', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext7@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'ZHL77', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security, security_question)
VALUES ('ext8@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'CHS88', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6', 'What is your favourite number');


--Assign roles to users for testing
INSERT into users_roles values( 1,1);
INSERT into users_roles values( 1,3);
INSERT into users_roles values( 2,2);
INSERT into users_roles values( 2,3);
INSERT into users_roles values( 3,3);
INSERT into users_roles values( 3,9);
INSERT into users_roles values( 4,3);
INSERT into users_roles values( 4,4);
INSERT into users_roles values( 4,5);
INSERT into users_roles values( 4,6);
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
INSERT into users_roles values( 10,2);
INSERT into users_roles values( 10,3);
INSERT into users_roles values( 11,3);
INSERT into users_roles values( 11,4);
INSERT into users_roles values( 11,5);
INSERT into users_roles values( 11,6);
INSERT into users_roles values( 11,7);
INSERT into users_roles values( 11,8);
INSERT into users_roles values( 12,3);
INSERT into users_roles values( 12,4);
INSERT into users_roles values( 12,5);
INSERT into users_roles values( 12,6);
INSERT into users_roles values( 12,8);
INSERT into users_roles values( 12,9);
INSERT into users_roles values( 13,3);
INSERT into users_roles values( 13,8);
--INSERT into users_roles values( 14,3);
INSERT into users_roles values( 14,10);
--INSERT into users_roles values( 15,3);
INSERT into users_roles values( 15,10);
--INSERT into users_roles values( 16,3);
INSERT into users_roles values( 16,10);
--INSERT into users_roles values( 17,3);
INSERT into users_roles values( 17,10);
--INSERT into users_roles values( 18,3);
INSERT into users_roles values( 18,10);
--INSERT into users_roles values( 19,3);
INSERT into users_roles values( 19,10);
--INSERT into users_roles values( 20,3);
INSERT into users_roles values( 20,10);
--INSERT into users_roles values( 21,3);
INSERT into users_roles values( 21,10);
INSERT into users_roles values( 22,3);
INSERT into users_roles values( 22,8);
INSERT into users_roles values( 23,3);
INSERT into users_roles values( 23,8);
INSERT into users_roles values( 24,3);
INSERT into users_roles values( 24,8);
INSERT into users_roles values( 25,3);
INSERT into users_roles values( 25,8);
INSERT into users_roles values( 26,3);
INSERT into users_roles values( 26,8);
INSERT into users_roles values( 27,3);
INSERT into users_roles values( 27,8);
INSERT into users_roles values( 28,3);
INSERT into users_roles values( 28,8);
INSERT into users_roles values( 29,3);
INSERT into users_roles values( 29,8);

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

insert into building(name, address, postal_code, city, num_floor, pic_path) values("EXPO EAST", "1 Expo Dr", 486150,"SINGAPORE", 6, "expo.jpg");
insert into building(name, address, postal_code, city, num_floor, pic_path) values("EXPO WEST", "2 Expo Dr", 486151,"SINGAPORE", 4, "expo_west.jpg");
--insert into building(name, address, postal_code, city, num_floor, pic_path) values("EXPO NORTH", "3 Expo Dr", 486152,"SINGAPORE", 5, "expo_north.jpg");
--insert into building(name, address, postal_code, city, num_floor, pic_path) values("EXPO SOUTH", "3 Temasek Boulevard", 038983,"SINGAPORE", 2, "expo_south.jpg");
--insert into building(name, address, postal_code, city, num_floor, pic_path) values("SUNTEC", "MINGSHA", 123456,"SINGAPORE", 6, "expo.jpg");

insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,1);
insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,2);
--insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,3);
--insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,4);
--insert into client_organisation_buildings(client_organisation_id, buildings_id) values(2,5);

insert into vendor(contact, description, email, name, registration)values("84500062", "great", "1@gmail.com", "MS", "123456789A");
insert into vendor(contact, description, email, name, registration)values("84536000", "excellent", "2@gmail.com", "HL", "123456789B");
insert into vendor(contact, description, email, name, registration)values("81610000", "good", "3@gmail.com", "KL", "123456789C");

insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,1);
insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,2);
insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,3);

--Expo East
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,1,80,1);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,2,80,1);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,3,80,1);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,4,80,1);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,5,80,1);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,6,80,1);

--Expo West
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP2.jpg',123,1,60,2);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP2.jpg',123,2,60,2);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP2.jpg',123,3,60,2);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP2.jpg',123,4,60,2);
/*
--Expo North
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP3.jpeg',123,1,60,3);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP3.jpeg',123,2,60,3);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP3.jpeg',123,3,60,3);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP3.jpeg',123,4,60,3);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP3.jpeg',123,5,60,3);
--Expo South
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP4.png',123,1,80,4);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('fP4.png',123,2,80,4);*/

INSERT INTO building_levels(building_id, levels_id)VALUES(1,1);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,2);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,3);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,4);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,5);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,6);

INSERT INTO building_levels(building_id, levels_id)VALUES(2,7);
INSERT INTO building_levels(building_id, levels_id)VALUES(2,8);
INSERT INTO building_levels(building_id, levels_id)VALUES(2,9);
INSERT INTO building_levels(building_id, levels_id)VALUES(2,10);
/*
INSERT INTO building_levels(building_id, levels_id)VALUES(3,11);
INSERT INTO building_levels(building_id, levels_id)VALUES(3,12);
INSERT INTO building_levels(building_id, levels_id)VALUES(3,13);
INSERT INTO building_levels(building_id, levels_id)VALUES(3,14);
INSERT INTO building_levels(building_id, levels_id)VALUES(3,15);
INSERT INTO building_levels(building_id, levels_id)VALUES(4,16);
INSERT INTO building_levels(building_id, levels_id)VALUES(4,17);
*/
--Expo East
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
--Expo West
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
/*
--Expo North
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
--Expo South
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);*/

--Expo East
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL A1', 38, 11, 20, 1, 1);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 38, 100, true, 'HALL B1', 50, 72, 7, 1, 2);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL C1', 38, 31, 20, 1, 3);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL D1', 50, 51, 7, 1, 4);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL A2', 38, 11, 20, 2, 5);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 38, 100, true, 'HALL B2', 50, 72, 7, 2, 6);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL C2', 38, 31, 20, 2, 7);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL D2', 50, 51, 7, 2, 8);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL A3', 38, 11, 20, 3, 9);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 38, 100, true, 'HALL B3', 50, 72, 7, 3, 10);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL C3', 38, 31, 20, 3, 11);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL D3', 50, 51, 7, 3, 12);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL A4', 38, 11, 20, 4, 13);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 38, 100, true, 'HALL B4', 50, 72, 7, 4, 14);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL C4', 38, 31, 20, 4, 15);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL D4', 50, 51, 7, 4, 16);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL A6', 38, 11, 20, 6, 17);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 38, 100, true, 'HALL B6', 50, 72, 7, 6, 18);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL C6', 38, 31, 20, 6, 19);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL D6', 50, 51, 7, 6, 20);
/*INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL A6', 38, 11, 20, 6, 21);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 38, 100, true, 'HALL B6', 50, 72, 7, 6, 22);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL C6', 38, 31, 20, 6, 23);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 20, 100, true, 'HALL D6', 50, 51, 7, 6, 24);*/
-- Expo West
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 14, 100, true, 'HALL E1', 28, 12, 31, 7, 21);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL F1', 28, 12, 45, 7, 22);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL G1', 28, 12, 57, 7, 23);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 25, 100, true, 'HALL H1', 28, 12, 69, 7, 24);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 18, 100, true, 'HALL I1', 21, 19, 94, 7, 25);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 14, 100, true, 'HALL E2', 28, 12, 31, 8, 26);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL F2', 28, 12, 45, 8, 27);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL G2', 28, 12, 57, 8, 28);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 25, 100, true, 'HALL H2', 28, 12, 69, 8, 29);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 18, 100, true, 'HALL I2', 21, 19, 94, 8, 30);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 14, 100, true, 'HALL E3', 28, 12, 31, 9, 31);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL F3', 28, 12, 45, 9, 32);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL G3', 28, 12, 57, 9, 33);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 25, 100, true, 'HALL H3', 28, 12, 69, 9, 34);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 18, 100, true, 'HALL I3', 21, 19, 94, 9, 35);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 14, 100, true, 'HALL E4', 28, 12, 31, 10, 36);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL F4', 28, 12, 45, 10, 37);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 12, 100, true, 'HALL G4', 28, 12, 57, 10, 38);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 25, 100, true, 'HALL H4', 28, 12, 69, 10, 39);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 18, 100, true, 'HALL I4', 21, 19, 94, 10, 40);

/*
--Expo North
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL I1', 1, 2, 7, 11, 41);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL J1', 1, 10, 7, 11, 42);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL K1', 1, 16, 7, 11, 43);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL L1', 1, 28, 7, 11, 44);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL I2', 1, 2, 7, 12, 45);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL J2', 1, 10, 7, 12, 46);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL K2', 1, 16, 7, 12, 47);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL L2', 1, 28, 7, 12, 48);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL I3', 1, 10, 7, 13, 49);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL J3', 1, 16, 7, 13, 50);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL K3', 1, 28, 7, 13, 51);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL I4', 1, 10, 7, 14, 52);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL J4', 1, 16, 7, 14, 53);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL K4', 1, 28, 7, 14, 54);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL I5', 1, 10, 7, 15, 55);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL J5', 1, 16, 7, 15, 56);
--Expo South
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL M1', 1, 16, 7, 16, 57);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL N1', 1, 28, 7, 16, 58);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL M2', 1, 10, 7, 17, 59);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall', 1, 100, true, 'HALL N2', 1, 16, 7, 17, 60);*/



--INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)		
--VALUES('2016-09-06 22:00:00', 42.8, 0.4, '2016-09-12 22:00:00', 7, 64.2, '2016-09-19 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 42.8, 64.2, 64.2, 1, 0.00, 107, 100);		
--INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)		
--VALUES('2016-09-11 22:00:00', 42.8, 0.4, '2016-09-17 22:00:00', 7, 42.8, '2016-09-14 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 0.0, 107.0, 64.2, 1, 0.00, 107, 100);		
--INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)		
--VALUES('2016-09-08 22:00:00', 42.8, 0.4, '2016-09-14 22:00:00', 7, 42.8, '2016-09-11 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 0.0, 107.0, 64.2, 1, 0.00, 107, 100);		

--Insert 20 events
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha1','2016-09-03 22:00:00', '2016-09-01 10:00:00', 'Conference', 'CONFERENCE', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha2','2016-09-08 22:00:00', '2016-09-06 10:00:00', 'Annual Conference', 'CONFERENCE', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha3','2016-09-12 22:00:00', '2016-09-10 10:00:00', 'Flea Market', 'FAIR', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha4','2016-09-12 22:00:00', '2016-09-11 10:00:00', 'Meeting', 'CONFERENCE', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha5','2016-09-19 22:00:00', '2016-09-17 10:00:00', 'NTUC', 'FAIR', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha6','2016-09-26 22:00:00', '2016-09-25 10:00:00', 'Birthday', 'FAMILY', null, 0, 'PAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha7','2016-10-03 22:00:00', '2016-10-03 10:00:00', 'Concert', 'CONCERT', null, 1, 'PAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha8','2016-10-07 22:00:00', '2016-10-03 10:00:00', 'Movie Screening', 'LIFESTYLE', null, 0, 'UNPAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha9','2016-10-08 22:00:00', '2016-10-06 10:00:00', 'Singing Competition', 'CONCERT', null, 1, 'PAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha10','2016-10-12 22:00:00', '2016-10-11 10:00:00', 'Finance Workshop', 'SEMINAR', null, 1, 'UNPAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha11','2016-10-13 22:00:00', '2016-10-12 10:00:00', 'DND', 'CONFERENCE', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha12','2016-10-18 22:00:00', '2016-10-16 10:00:00', 'General meeting', 'CONFERENCE', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha13','2016-10-22 22:00:00', '2016-10-20 10:00:00', 'Flea Market 2', 'FAIR', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha14','2016-10-22 22:00:00', '2016-10-22 10:00:00', 'Audit Workshop', 'SEMINAR', null, 1, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha15','2016-10-26 22:00:00', '2016-10-26 10:00:00', 'Book Sale', 'FAIR', null, 0, 'PAID', 12);		
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)		
VALUES('APPROVED','hahaha16','2016-10-29 22:00:00', '2016-10-27 10:00:00', 'Modelling Workshop', 'SEMINAR', null, 1, 'PAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha17','2016-11-03 22:00:00', '2016-11-03 10:00:00', 'Party', 'CONCERT', null, 1, 'PAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha18','2016-11-04 22:00:00', '2016-11-03 10:00:00', 'Reunion Dinner', 'LIFESTYLE', null, 0, 'UNPAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha19','2016-11-12 22:00:00', '2016-11-11 10:00:00', 'Dance Competition', 'CONCERT', null, 1, 'PAID', 12);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha20','2016-11-16 22:00:00', '2016-11-15 10:00:00', 'Data Analysis Seminar', 'SEMINAR', null, 1, 'UNPAID', 12);

INSERT INTO user_events(user_id, events_id)VALUES(12, 1);
INSERT INTO user_events(user_id, events_id)VALUES(12, 2);	
INSERT INTO user_events(user_id, events_id)VALUES(23, 3);
INSERT INTO user_events(user_id, events_id)VALUES(24, 4);
INSERT INTO user_events(user_id, events_id)VALUES(25, 5);
INSERT INTO user_events(user_id, events_id)VALUES(26, 6);
INSERT INTO user_events(user_id, events_id)VALUES(27, 7);
INSERT INTO user_events(user_id, events_id)VALUES(28, 8);
INSERT INTO user_events(user_id, events_id)VALUES(29, 9);		
INSERT INTO user_events(user_id, events_id)VALUES(12, 10);
INSERT INTO user_events(user_id, events_id)VALUES(12, 11);
INSERT INTO user_events(user_id, events_id)VALUES(22, 12);	
INSERT INTO user_events(user_id, events_id)VALUES(23, 13);
INSERT INTO user_events(user_id, events_id)VALUES(24, 14);
INSERT INTO user_events(user_id, events_id)VALUES(25, 15);
INSERT INTO user_events(user_id, events_id)VALUES(26, 16);
INSERT INTO user_events(user_id, events_id)VALUES(27, 17);
INSERT INTO user_events(user_id, events_id)VALUES(28, 18);
INSERT INTO user_events(user_id, events_id)VALUES(29, 19);		
INSERT INTO user_events(user_id, events_id)VALUES(12, 20);
--INSERT INTO user_events(user_id, events_id)VALUES(12, 3);
--INSERT INTO user_events(user_id, events_id)VALUES(12, 4);

--Insert 30 Bookings
--1 to 10
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-03 22:00:00', '2016-09-01 10:00:00', 12, 1, 1, 1);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-03 22:00:00', '2016-09-01 10:00:00', 12, 2, 1, 2);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-08 22:00:00', '2016-09-06 10:00:00', 22, 3, 2, 3);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-12 22:00:00', '2016-09-10 10:00:00', 23, 2, 3, 2);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-12 22:00:00', '2016-09-10 10:00:00', 23, 4, 3, 4);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-12 22:00:00', '2016-09-11 10:00:00', 24, 7, 4, 7);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-19 22:00:00', '2016-09-17 10:00:00', 25, 13, 5, 13);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-19 22:00:00', '2016-09-17 10:00:00', 25, 14, 5, 14);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-09-26 22:00:00', '2016-09-25 10:00:00', 26, 25, 6, 25);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-03 22:00:00', '2016-10-03 10:00:00', 27, 1, 7, 1);
--11 to 20
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-07 22:00:00', '2016-10-03 10:00:00', 28, 29, 8, 29);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-07 22:00:00', '2016-10-03 10:00:00', 28, 25, 8, 25);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-08 22:00:00', '2016-10-06 10:00:00', 29, 22, 9, 22);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-12 22:00:00', '2016-10-11 10:00:00', 12, 26, 10, 26);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-13 22:00:00', '2016-10-12 10:00:00', 12, 16, 11, 16);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-18 22:00:00', '2016-10-16 10:00:00', 22, 11, 12, 11);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-18 22:00:00', '2016-10-16 10:00:00', 22, 22, 12, 22);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-22 22:00:00', '2016-10-20 10:00:00', 23, 33, 13, 33);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-22 22:00:00', '2016-10-22 10:00:00', 24, 6, 14, 6);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-26 22:00:00', '2016-10-26 10:00:00', 25, 2, 15, 2);
--21 to 30
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-29 22:00:00', '2016-10-27 10:00:00', 26, 33, 16, 33);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-29 22:00:00', '2016-10-27 10:00:00', 26, 22, 16, 22);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-29 22:00:00', '2016-10-27 10:00:00', 26, 11, 16, 11);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-03 22:00:00', '2016-11-03 10:00:00', 27, 2, 17, 2);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-03 22:00:00', '2016-11-03 10:00:00', 27, 3, 17, 3);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-04 22:00:00', '2016-11-03 10:00:00', 28, 5, 18, 5);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-12 22:00:00', '2016-11-11 10:00:00', 29, 1, 19, 1);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-16 22:00:00', '2016-11-15 10:00:00', 12, 4, 20, 4);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-16 22:00:00', '2016-11-15 10:00:00', 12, 5, 20, 5);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-16 22:00:00', '2016-11-15 10:00:00', 12, 6, 20, 6);
/*
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-12 22:00:00', '2016-11-11 10:00:00', 29, 1, 3, 1);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-16 22:00:00', '2016-11-15 10:00:00', 12, 4, 4, 2);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-16 22:00:00', '2016-11-15 10:00:00', 12, 5, 4, 3);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-16 22:00:00', '2016-11-15 10:00:00', 12, 6, 4, 4);
*/
INSERT INTO maintenance VALUES (1, 1, 'Repair projector', '2016-11-02 22:00:00', '2016-11-01 10:00:00');
INSERT INTO maintenance VALUES (2, 1, 'Cleaning', '2016-11-18 22:00:00', '2016-11-16 10:00:00');
INSERT INTO maintenance_vendors VALUES (1, 1);
INSERT INTO maintenance_vendors VALUES (1, 2);
INSERT INTO maintenance_vendors VALUES (2, 1);
INSERT INTO maintenance_vendors VALUES (2, 2);
INSERT INTO schedule VALUES (1, '2016-11-02 22:00:00', 1, '2016-11-01 10:00:00', 1, 1);
INSERT INTO schedule VALUES (2, '2016-11-02 22:00:00', 2, '2016-11-01 10:00:00', 1, 2);
INSERT INTO schedule VALUES (3, '2016-11-18 22:00:00', 1, '2016-11-16 10:00:00', 2, 1);
INSERT INTO schedule VALUES (4, '2016-11-18 22:00:00', 2, '2016-11-16 10:00:00', 2, 2);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('333',1,2,true,7,4);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('1771',1,2,true,8,4);

INSERT INTO special_rate(rate, description, period) VALUES(1.4, 'weekend premium', 'weekend');
INSERT INTO special_rate(rate, description, period) VALUES(0.8, 'Feb discount', 'FEB');
INSERT INTO client_organisation_special_rates(client_organisation_id, special_rates_id) values(1,1);
INSERT INTO client_organisation_special_rates(client_organisation_id, special_rates_id) values(1,2);

INSERT INTO client_organisation_system_subscriptions VALUES ('1', '0');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', '1');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', '2');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', '3');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', '4');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', '5');

INSERT INTO client_organisation_system_subscriptions VALUES ('2', '0');
INSERT INTO client_organisation_system_subscriptions VALUES ('2', '1');
INSERT INTO client_organisation_system_subscriptions VALUES ('2', '2');

--Insert 12 categories
/*
INSERT INTO category VALUES(1,'STUDENT',20,12,7);
INSERT INTO category VALUES(2,'ADULT',20,15.5,7);
INSERT INTO category VALUES(3,'ALL',20,10,9);
INSERT INTO category VALUES(4,'ADULT',10,15,10);
INSERT INTO category VALUES(5,'ELDERLY',10,12,10);
INSERT INTO category VALUES(6,'ALL',20,15,14);
INSERT INTO category VALUES(7,'STUDENT',10,12,16);
INSERT INTO category VALUES(8,'ADULT',10,15.5,16);
INSERT INTO category VALUES(9,'CHILDREN',10,10,17);
INSERT INTO category VALUES(10,'ADULT',15,15.5,17);
INSERT INTO category VALUES(11,'ALL',20,15,19);
INSERT INTO category VALUES(12,'ALL',15,12,20);*/
INSERT INTO category VALUES(1,'ALL',20,15,3);
INSERT INTO category VALUES(2,'ALL',15,12,4);

/*
--Insert 24 tickets
insert into ticket values(1,'2016-10-03 22:00:00', 'P1', '2016-10-01 10:00:00', '2016-10-03 10:00:00', 'single entry','*01*',1);
insert into ticket values(2,'2016-10-03 22:00:00', 'P2', '2016-10-01 10:00:00', '2016-10-03 10:00:00', 'single entry','*02*',1);
insert into ticket values(3,'2016-10-03 22:00:00', 'P3', '2016-10-02 10:00:00', '2016-10-03 10:00:00', 'single entry','*03*',2);
insert into ticket values(4,'2016-10-03 22:00:00', 'P4', '2016-10-02 10:00:00', '2016-10-03 10:00:00', 'single entry','*04*',2);
insert into ticket values(5,'2016-10-08 22:00:00', 'P5', '2016-10-05 10:00:00', '2016-10-06 10:00:00', 'double entry','*05*',3);
insert into ticket values(6,'2016-10-08 22:00:00', 'P6', '2016-10-05 10:00:00', '2016-10-06 10:00:00', 'double entry','*06*',3);
insert into ticket values(7,'2016-10-12 22:00:00', 'P7', '2016-10-06 10:00:00', '2016-10-11 10:00:00', 'double entry','*07*',4);
insert into ticket values(8,'2016-10-12 22:00:00', 'P8', '2016-10-06 10:00:00', '2016-10-11 10:00:00', 'double entry','*08*',4);
insert into ticket values(9,'2016-10-12 22:00:00', 'P9', '2016-10-06 10:00:00', '2016-10-11 10:00:00', 'double entry','*09*',5);
insert into ticket values(10,'2016-10-12 22:00:00', 'P10', '2016-10-06 10:00:00', '2016-10-11 10:00:00', 'double entry','*10*',5);
insert into ticket values(11,'2016-10-22 22:00:00', 'P11', '2016-10-20 10:00:00', '2016-10-22 10:00:00', 'double entry','*11*',6);
insert into ticket values(12,'2016-10-22 22:00:00', 'P12', '2016-10-20 10:00:00', '2016-10-22 10:00:00', 'double entry','*12*',6);
insert into ticket values(13,'2016-10-29 22:00:00', 'P13', '2016-10-25 10:00:00', '2016-10-27 10:00:00', 'double entry','*13*',7);
insert into ticket values(14,'2016-10-29 22:00:00', 'P14', '2016-10-25 10:00:00', '2016-10-27 10:00:00', 'double entry','*14*',7);
insert into ticket values(15,'2016-10-29 22:00:00', 'P15', '2016-10-25 10:00:00', '2016-10-27 10:00:00', 'double entry','*15*',8);
insert into ticket values(16,'2016-10-29 22:00:00', 'P16', '2016-10-25 10:00:00', '2016-10-27 10:00:00', 'double entry','*16*',8);
insert into ticket values(17,'2016-11-03 22:00:00', 'P17', '2016-11-01 10:00:00', '2016-11-03 10:00:00', 'single entry','*17*',9);
insert into ticket values(18,'2016-11-03 22:00:00', 'P18', '2016-11-01 10:00:00', '2016-11-03 10:00:00', 'single entry','*18*',9);
insert into ticket values(19,'2016-11-03 22:00:00', 'P19', '2016-11-02 10:00:00', '2016-11-03 10:00:00', 'single entry','*19*',10);
insert into ticket values(20,'2016-11-03 22:00:00', 'P20', '2016-11-02 10:00:00', '2016-11-03 10:00:00', 'single entry','*20*',10);
insert into ticket values(21,'2016-11-12 22:00:00', 'P21', '2016-11-05 10:00:00', '2016-11-11 10:00:00', 'single entry','*21*',11);
insert into ticket values(22,'2016-11-12 22:00:00', 'P22', '2016-11-05 10:00:00', '2016-11-11 10:00:00', 'single entry','*22*',11);
insert into ticket values(23,'2016-11-16 22:00:00', 'P23', '2016-11-05 10:00:00', '2016-11-15 10:00:00', 'single entry','*23*',12);
insert into ticket values(24,'2016-11-16 22:00:00', 'P24', '2016-11-05 10:00:00', '2016-11-15 10:00:00', 'single entry','*24*',12);*/

insert into ticket values(1,'2016-11-12 22:00:00', 'P21', '2016-11-05 10:00:00',false, '2016-11-11 10:00:00', 'single entry','*21*',1);
insert into ticket values(2,'2016-11-12 22:00:00', 'P22', '2016-11-05 10:00:00',false, '2016-11-11 10:00:00', 'single entry','*22*',1);
insert into ticket values(3,'2016-11-16 22:00:00', 'P23', '2016-11-05 10:00:00',false, '2016-11-15 10:00:00', 'single entry','*23*',2);
insert into ticket values(4,'2016-11-16 22:00:00', 'P24', '2016-11-05 10:00:00', false,'2016-11-15 10:00:00', 'single entry','*24*',2);

insert into feedback values(1, '123', '2016-11-12 22:00:00', null, 'hahaha', 12);
insert into event_feedbacks values(20, 1);
/*
insert into user_tickets values(14,1);
insert into user_tickets values(14,2);
insert into user_tickets values(15,3);
insert into user_tickets values(15,4);
insert into user_tickets values(16,5);
insert into user_tickets values(16,6);
insert into user_tickets values(17,7);
insert into user_tickets values(17,8);
insert into user_tickets values(18,9);
insert into user_tickets values(18,10);
insert into user_tickets values(19,11);
insert into user_tickets values(19,12);
insert into user_tickets values(20,13);
insert into user_tickets values(20,14);
insert into user_tickets values(21,15);
insert into user_tickets values(21,16);
insert into user_tickets values(14,17);
insert into user_tickets values(14,18);
insert into user_tickets values(16,19);
insert into user_tickets values(16,20);
insert into user_tickets values(19,21);
insert into user_tickets values(19,22);
insert into user_tickets values(20,23);
insert into user_tickets values(20,24);*/

insert into user_tickets values(12,1);
insert into user_tickets values(12,2);
insert into user_tickets values(12,3);
insert into user_tickets values(12,4);

insert into discount values ( 1,"12345","Enjoy 10% off your next mobile phone purchase. Exclusively at starhub!", "STARHUB");


INSERT INTO icon(icon_path, icon_type) VALUES('yard-fountain.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 1);
INSERT INTO icon(icon_path, icon_type) VALUES('mall-information-sign.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 2);
INSERT INTO icon(icon_path, icon_type) VALUES('mall-wifi-sign.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 3);
INSERT INTO icon(icon_path, icon_type) VALUES('waiting-room-sign.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 4);