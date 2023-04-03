CREATE TABLE item (
    id bigint primary key auto_increment not null,
    content varchar(100) not null,
    category_id bigint not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,

    CONSTRAINT item_category_fk
    FOREIGN KEY (category_id)
    REFERENCES category (id)
)