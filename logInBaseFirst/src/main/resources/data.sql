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
insert into role (name)values ("ROLE_EVEGOER");
 
INSERT INTO payment_policy(deposit_rate, interim_period, due_days, subsequent_number)
VALUES(0.4, 2, 2, 2);

insert into client_organisation (organisation_name, address, postal, phone, payment_policy_id,themecolour,logo_file_path) 
values ("Expo", "1 Expo Dr", "486150", "64032160", 1,"midnight-blue","expoLogo.png");
insert into client_organisation (organisation_name, address, postal, phone,themecolour,logo_file_path) 
values ("Suntec", "3 Temasek Boulevard","038983", "68221537","green","suntecLogo.png");

--Algattas is client_organisation 3
insert into client_organisation (organisation_name, address, postal, phone,themecolour,logo_file_path) 
values ("Algattas", "73 Ubi Road 1","408733", "31580456","red","algattasLogo.png");
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('superadmin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',3 ,'Kenneth LIM' , '2');
--Algattas superadmin is user 1

INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('admin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Jin Fa CHUA' , '3');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('user@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Kok Hwee TAN', '4');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('event@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Hailing ZHANG', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('jaydentkh@gmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'JayDen TAN', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('property@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'property manager', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('finance@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'finance manager', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('ticketing@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'ticketing manager', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('exteve@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1, 'external event organiser', '5');

INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('suntecadmin@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec Admin', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('suntecall@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec All roles', '5');


INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('tkgs.zhao.mingsha@gmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'Mingsha ZHAO', '5');
INSERT INTO user (email, password_hash,client_organisation_id, name ,security)
VALUES ('suntecexternal@localhost', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',2, 'Suntec external manager', '5');

--Assign roles to users for testing
INSERT into users_roles values( 1,1);
INSERT into users_roles values( 1,3);
INSERT into users_roles values( 2,2);
INSERT into users_roles values( 2,3);
INSERT into users_roles values( 3,3);
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
INSERT into users_roles values( 13,3);
INSERT into users_roles values( 13,8);


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

insert into building(name, address, postal_code, city, num_floor, pic_path) values("buildingNameMS", "MINGSHA", 123456,"SINGAPORE", 6, "expo.jpg");
insert into building(name, address, postal_code, city, num_floor, pic_path) values("buildingNameHL", "HAILING", 234567,"SINGAPORE", 4, "max_atria.jpg");
insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,1);
insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,2);
insert into vendor(contact, description, email, name)values("123", "good", "1@gmail.com", "MS");
insert into vendor(contact, description, email, name)values("456", "good", "2@gmail.com", "HL");
insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,1);
insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,2);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('flooPlan1.png',123,2,80,1);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,1);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('hsl(36, 100%, 66%)', 100, 250, 250, './svg/rect.svg', 100, null);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall',38,100,true,'HALL C', 50 , 71 , 7 , 1, 1);
INSERT INTO unit(description, sizex, rent, rentable, unit_number, sizey , col , row , level_id, square_id)
VALUES('Hall',18,100,true,'HALL B',51,52,7, 1, 2);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha','2016-10-26 22:00:00', '2016-10-25 10:00:00', 'party', 'CONCERT', null, 1, 'UNPAID', 12);
--INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)		
--VALUES('2016-09-06 22:00:00', 42.8, 0.4, '2016-09-12 22:00:00', 7, 64.2, '2016-09-19 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 42.8, 64.2, 64.2, 1, 0.00, 107, 100);		
--INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)		
--VALUES('2016-09-11 22:00:00', 42.8, 0.4, '2016-09-17 22:00:00', 7, 42.8, '2016-09-14 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 0.0, 107.0, 64.2, 1, 0.00, 107, 100);		
--INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)		
--VALUES('2016-09-08 22:00:00', 42.8, 0.4, '2016-09-14 22:00:00', 7, 42.8, '2016-09-11 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 0.0, 107.0, 64.2, 1, 0.00, 107, 100);		
--INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id, payment_plan_id)		
--VALUES('APPROVED','hahaha','2016-09-08 22:00:00', '2016-09-06 10:00:00', 'party1', 'CONCERT', null, 1, 'UNPAID', 12, 1);		
--INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id, payment_plan_id)		
--VALUES('APPROVED','hahaha','2016-09-12 22:00:00', '2016-09-11 10:00:00', 'party2', 'CONCERT', null, 1, 'UNPAID', 12, 2);		
--INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id, payment_plan_id)		
--VALUES('APPROVED','hahaha','2016-09-26 22:00:00', '2016-09-25 10:00:00', 'party3', 'CONCERT', null, 1, 'UNPAID', 12, 3);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha','2016-11-12 22:00:00', '2016-11-11 10:00:00', 'partyyyyyy', 'FAIR', null, 1, 'UNPAID', 12);

INSERT INTO user_events(user_id, events_id)VALUES(12, 1);
INSERT INTO user_events(user_id, events_id)VALUES(12, 2);		
--INSERT INTO user_events(user_id, events_id)VALUES(12, 3);		
--INSERT INTO user_events(user_id, events_id)VALUES(12, 4);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-26 22:00:00', '2016-10-25 10:00:00', 12, 1, 1, 1);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-11-12 22:00:00', '2016-11-11 10:00:00', 12, 1, 2, 1);
INSERT INTO maintenance VALUES (1, 'Repair projector', '2016-11-02 22:00:00', '2016-11-01 10:00:00');
INSERT INTO maintenance_vendors VALUES (1, 1);
INSERT INTO maintenance_vendors VALUES (1, 2);
INSERT INTO schedule VALUES (1, '2016-11-02 22:00:00', 1, '2016-11-01 10:00:00', 1, 1);
INSERT INTO schedule VALUES (2, '2016-11-02 22:00:00', 2, '2016-11-01 10:00:00', 1, 2);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('333',1,2,true,7,4);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('1771',1,2,true,8,4);

INSERT INTO special_rate(rate, description, period) VALUES(1.4, 'weekend premium', 'weekend');
INSERT INTO special_rate(rate, description, period) VALUES(0.8, 'Feb discount', 'FEB');
INSERT INTO client_organisation_special_rates(client_organisation_id, special_rates_id) values(1,1);
INSERT INTO client_organisation_special_rates(client_organisation_id, special_rates_id) values(1,2);

INSERT INTO client_organisation_system_subscriptions VALUES ('1', 'Finance System');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', 'Event Management System');
INSERT INTO client_organisation_system_subscriptions VALUES ('1', 'Property System');

INSERT INTO client_organisation_system_subscriptions VALUES ('2', 'Finance System');
INSERT INTO client_organisation_system_subscriptions VALUES ('2', 'Event Management System');
INSERT INTO client_organisation_system_subscriptions VALUES ('2', 'Property System');

insert into category values(1,'name',12,12,1);
insert into category values(2,'namenumber2',15,15.5,1);

insert into ticket values(1,'2016-10-26 22:00:00', 'P1', '2016-10-23 10:00:00', '2016-10-25 10:00:00', 'single entry','*15*',1);
insert into ticket values(2,'2016-10-26 22:00:00', 'P2', '2016-10-22 10:00:00', '2016-10-25 10:00:00', 'single entry','*16*',1);
insert into ticket values(3,'2016-10-26 22:00:00', 'P3', '2016-10-21 10:00:00', '2016-10-25 10:00:00', 'double entry','*17*',2);
insert into ticket values(4,'2016-10-26 22:00:00', 'P4', '2016-10-20 10:00:00', '2016-10-25 10:00:00', 'double entry','*18*',2);
insert into ticket values(5,'2016-10-26 22:00:00', 'P5', '2016-10-19 10:00:00', '2016-10-25 10:00:00', 'double entry','*19*',2);
insert into ticket values(6,'2016-12-26 22:00:00', 'P6', '2016-10-19 10:00:00', '2016-10-25 10:00:00', 'double entry','*19*',2);
insert into user_tickets values( 6,1);
insert into user_tickets values( 6,2);
insert into user_tickets values (6,6);
insert into discount values ( 1,"12345","DISCOUNT MESSAGE", "STARHUB");


INSERT INTO icon(icon_path, icon_type) VALUES('yard-fountain.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 1);
INSERT INTO icon(icon_path, icon_type) VALUES('mall-information-sign.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 2);
INSERT INTO icon(icon_path, icon_type) VALUES('mall-wifi-sign.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 3);
INSERT INTO icon(icon_path, icon_type) VALUES('waiting-room-sign.svg','CUST');
INSERT INTO client_organisation_icons VALUES (1, 4);