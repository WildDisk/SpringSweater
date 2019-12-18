delete from user_role;
delete from usr;

insert into usr(id, username, password, is_active) values
(1, 'admin', '$2a$08$mKEBBaUQgvJOy7xNZMzrReFVRKjxhuyz/Kwefz8anGvLZ4yXV1Bbu', true),
(2, 'John', '$2a$08$mKEBBaUQgvJOy7xNZMzrReFVRKjxhuyz/Kwefz8anGvLZ4yXV1Bbu', true);

insert into user_role(user_id, roles) values
(1, 'ADMIN'), (1, 'USER'),
(2, 'USER');