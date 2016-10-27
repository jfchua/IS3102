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

insert into client_organisation (organisation_name, address, postal, phone, payment_policy_id) 
values ("Expo", "1 Expo Dr", "486150", "64032160", 1);
insert into client_organisation (organisation_name, address, postal, phone) 
values ("Suntec", "3 Temasek Boulevard","038983", "68221537");
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

INSERT INTO user (email, password_hash,client_organisation_id, name)
VALUES ('tkgs.zhao.mingsha@gmail.com', '$2a$04$skH54MFgrVwCYx1lCbDgz.icEeks2GAIfEMi5y1ENtP9klVzj39w6',1,'event');

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
INSERT into users_roles values( 10,1);
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

insert into building(name, address, postal_code, city, num_floor, pic_path) values("buildingNameMS", "MINGSHA", 123456,"SINGAPORE", 6, "");
insert into building(name, address, postal_code, city, num_floor, pic_path) values("buildingNameHL", "HAILING", 234567,"SINGAPORE", 4, "");
insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,1);
insert into client_organisation_buildings(client_organisation_id, buildings_id) values(1,2);
insert into vendor(contact, description, email, name)values("123", "good", "1@gmail.com", "MS");
insert into vendor(contact, description, email, name)values("456", "good", "2@gmail.com", "HL");
insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,1);
insert into client_organisation_vendors(client_organisation_id, vendors_id) values(1,2);
INSERT INTO level(file_path, length, level_num, width, building_id)
VALUES('111',123,2,80,1);
INSERT INTO building_levels(building_id, levels_id)VALUES(1,1);
INSERT INTO square(color, height, positionleft, positiontop, type, width, icon_id)
VALUES('coral', 100, 100, 100, './svg/rect.svg', 100, null);
INSERT INTO unit(description, length, rent, rentable, unit_number, width, level_id, square_id)
VALUES('123',100,100,true,'unit1',100, 1, 1);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id)
VALUES('APPROVED','hahaha','2016-10-26 22:00:00', '2016-10-25 10:00:00', 'party', 'CONCERT', null, 1, 'UNPAID', 12);
INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)
VALUES('2016-09-06 22:00:00', 42.8, 0.4, '2016-09-12 22:00:00', 7, 64.2, '2016-09-19 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 42.8, 64.2, 64.2, 1, 0.00, 107, 100);
INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)
VALUES('2016-09-11 22:00:00', 42.8, 0.4, '2016-09-17 22:00:00', 7, 42.8, '2016-09-14 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 0.0, 107.0, 64.2, 1, 0.00, 107, 100);
INSERT INTO payment_plan(created, deposit, deposit_rate, due, gst, next_payment, notification_due, overdue, owner, paid, payable, subsequent, subsequent_number, ticket_revenue, total, total_before_gst)
VALUES('2016-09-08 22:00:00', 42.8, 0.4, '2016-09-14 22:00:00', 7, 42.8, '2016-09-11 22:00:00', 0, 'tkgs.zhao.mingsha@gmail.com', 0.0, 107.0, 64.2, 1, 0.00, 107, 100);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id, payment_plan_id)
VALUES('APPROVED','hahaha','2016-09-08 22:00:00', '2016-09-06 10:00:00', 'party1', 'CONCERT', null, 1, 'UNPAID', 12, 1);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id, payment_plan_id)
VALUES('APPROVED','hahaha','2016-09-12 22:00:00', '2016-09-11 10:00:00', 'party2', 'CONCERT', null, 1, 'UNPAID', 12, 2);
INSERT INTO event(approval_status, event_description, event_end_date, event_start_date, event_title, event_type, file_path, has_ticket, payment_status, event_org_id, payment_plan_id)
VALUES('APPROVED','hahaha','2016-09-26 22:00:00', '2016-09-25 10:00:00', 'party3', 'CONCERT', null, 1, 'UNPAID', 12, 3);


INSERT INTO user_events(user_id, events_id)VALUES(12, 1);
INSERT INTO user_events(user_id, events_id)VALUES(12, 2);
INSERT INTO user_events(user_id, events_id)VALUES(12, 3);
INSERT INTO user_events(user_id, events_id)VALUES(12, 4);
INSERT INTO booking_appl(event_end_date_time, event_start_date_time, owner, room, event_id, unit_id)
VALUES('2016-10-26 22:00:00', '2016-10-25 10:00:00', 1, 1, 1, 1);
--INSERT INTO unit(description, length, rent, rentable, unit_number, width)
--VALUES('1123',1,2,true,6,4);
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








