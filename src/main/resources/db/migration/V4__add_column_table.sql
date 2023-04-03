CREATE TABLE category (
    id bigint primary key auto_increment not null,
    name varchar(20) not null,
    project_id bigint not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,

    CONSTRAINT category_project_fk
    FOREIGN KEY (project_id)
    REFERENCES project (id)
)