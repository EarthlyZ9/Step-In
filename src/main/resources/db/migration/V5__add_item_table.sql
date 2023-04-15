CREATE TABLE item (
    id bigint primary key auto_increment not null,
    content varchar(100) not null,
    step_id bigint not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,

    CONSTRAINT item_step_fk
    FOREIGN KEY (step_id)
    REFERENCES step (id)
)