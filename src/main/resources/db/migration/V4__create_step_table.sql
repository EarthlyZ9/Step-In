create table if not exists step
(
    id bigint auto_increment primary key,
    name varchar(50) not null,
    number bigint not null,
    created_at datetime not null default now(),
    updated_at datetime on update now(),
    project_id bigint not null,

    foreign key (project_id) references project (id) on delete cascade
);