insert into task_management.users (id, email, password) VALUES ('fedd6a4f-f0e8-4a50-82e7-8b69bffc6507', 'email1', '$2a$12$s013rU7ViA/0F5Ant.TY1uoXtNgV9xrIZOj5lj9Ngr5FhYLV7DAnO'); --pass1
insert into task_management.users (id, email, password) VALUES ('82dc22a0-d332-4241-959a-b20f8590155f', 'email2', '$2a$12$dVEX3B/WbIIiW0v.ce2tCutwdGxTUOt.6FAeovGVndzgMnXGDwu3K'); --pass2
insert into task_management.users (id, email, password) VALUES ('5405c989-9dbf-4e35-a923-8b1d4e4ad7bc', 'email3', '$2a$12$zBG7r3Ov3xjrKb7AtUnmNeE2r8vMmq/9.5yiCqp8Go0ip/54JjFuG'); --pass3

insert into task_management.task (id, header, description, status, priority, author_id, performer_id) VALUES ('3412cd10-9ec0-41f4-802f-e6440792fed2', 'header1', 'description1', 'OPENED', 'HIGH', 'fedd6a4f-f0e8-4a50-82e7-8b69bffc6507', '82dc22a0-d332-4241-959a-b20f8590155f');
insert into task_management.task (id, header, description, status, priority, author_id, performer_id) VALUES ('28ce2670-453a-46d5-b9a0-786fd4a52eab', 'header2', 'description2', 'IN_PROGRESS', 'MEDIUM', '82dc22a0-d332-4241-959a-b20f8590155f', '82dc22a0-d332-4241-959a-b20f8590155f');
insert into task_management.task (id, header, description, status, priority, author_id, performer_id) VALUES ('380f1c97-d71f-4a04-b025-0ba103b4f6b0', 'header3', 'description3', 'COMPLETED', 'LOW', '5405c989-9dbf-4e35-a923-8b1d4e4ad7bc', '82dc22a0-d332-4241-959a-b20f8590155f');

insert into task_management.comment (id, time, author_id, content, task_id) VALUES ('216a035d-3c5f-4c74-a860-6d1cb6d27a88', '2024-01-01 00:01:01', 'fedd6a4f-f0e8-4a50-82e7-8b69bffc6507', 'Content1', '3412cd10-9ec0-41f4-802f-e6440792fed2');
insert into task_management.comment (id, time, author_id, content, task_id) VALUES ('433c2bf7-41c3-462d-8d71-e01ee1849bcc', '2024-01-02 00:01:01', '82dc22a0-d332-4241-959a-b20f8590155f', 'Content2', '3412cd10-9ec0-41f4-802f-e6440792fed2');
insert into task_management.comment (id, time, author_id, content, task_id) VALUES ('5ea51c64-a8f8-42b2-9e8a-626690e0a187', '2024-01-03 00:01:01', '5405c989-9dbf-4e35-a923-8b1d4e4ad7bc', 'Content3', '28ce2670-453a-46d5-b9a0-786fd4a52eab');
insert into task_management.comment (id, time, author_id, content, task_id) VALUES ('b12c19cd-ebe2-4c10-be56-4623ce569542', '2024-01-04 00:01:01', 'fedd6a4f-f0e8-4a50-82e7-8b69bffc6507', 'Content4', '28ce2670-453a-46d5-b9a0-786fd4a52eab');
insert into task_management.comment (id, time, author_id, content, task_id) VALUES ('1272adcc-ed29-4835-9a26-bf9be266ab64', '2024-01-05 00:01:01', '5405c989-9dbf-4e35-a923-8b1d4e4ad7bc', 'Content5', '28ce2670-453a-46d5-b9a0-786fd4a52eab');
