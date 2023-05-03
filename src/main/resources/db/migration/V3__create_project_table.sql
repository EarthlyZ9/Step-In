create table if not exists project
(
    id bigint auto_increment primary key,
    name varchar(50) not null,
    created_at datetime not null default now(),
    updated_at datetime on update now(),
    owner_id bigint not null,

    foreign key (owner_id) references user (id) on delete cascade
);