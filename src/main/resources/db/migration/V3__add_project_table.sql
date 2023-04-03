CREATE TABLE project (
    id bigint primary key auto_increment not null,
    name varchar(20) not null,
    owner_id bigint not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,

    CONSTRAINT project_user_fk
    FOREIGN KEY (owner_id)
    REFERENCES user (id)
)