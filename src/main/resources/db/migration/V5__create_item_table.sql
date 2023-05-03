create table if not exists item
(
    id bigint auto_increment primary key,
    content varchar(100) not null,
    created_at datetime not null default now(),
    updated_at datetime on update now(),
    step_id bigint not null,

    foreign key (step_id) references step (id) on delete cascade
);