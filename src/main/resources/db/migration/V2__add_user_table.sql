CREATE TABLE user (
    id bigint primary key auto_increment not null,
    email varchar(200) not null,
    password varchar(100) not null,
    is_active bool not null default true,
    role varchar(20)  not null default 'ROLE_USER',
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,

    UNIQUE KEY email_unique_key (email),
    INDEX (email)
)