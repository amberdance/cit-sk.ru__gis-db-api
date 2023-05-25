insert into users (chat_id, username, email, role, organization_id)
values ('1111111111', md5(random()::text), 'test1@mail.ru', 'ADMIN', 1);

insert into users (chat_id, username, email, role, organization_id)
values ('2222222222', md5(random()::text), 'test2@mail.ru', 'ADMIN', 2);

insert into users (chat_id, username, email, role, organization_id)
values ('3333333333', md5(random()::text), 'test3@mail.ru', 'USER', 3);

insert into users (chat_id, username, email, role, organization_id)
values ('4444444444', md5(random()::text), 'test4@mail.ru', 'USER', 4);
