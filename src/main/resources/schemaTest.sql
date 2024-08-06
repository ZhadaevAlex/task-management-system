create schema if not exists task_management;

drop table if exists task_management.task_management.comment;

drop table if exists task_management.task_management.task;

drop table if exists task_management.task_management.users;

create table if not exists task_management.task_management.users
(
    id uuid primary key,
    email varchar(255) null,
    password varchar(128) null
);

create table if not exists task_management.task_management.task
(
    id uuid primary key,
    header  varchar(255) null,
    description varchar(255) null,
    status varchar(255) null,
    priority varchar(255) null,
    author_id UUID null,
    performer_id UUID null,
    foreign key(author_id) references task_management.users (id) on delete set null,
    foreign key(performer_id) references task_management.users (id) on delete set null
);

create table if not exists task_management.task_management.comment
(
    id uuid primary key,
    time timestamp null,
    author_id uuid null,
    content varchar(4096),
    task_id uuid null,
    foreign key (author_id) references task_management.task_management.users (id) on delete set null,
    foreign key (task_id) references task_management.task_management.task (id) on delete set null
)