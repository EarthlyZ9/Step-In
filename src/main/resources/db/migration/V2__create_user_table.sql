CREATE TABLE IF NOT EXISTS user (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    nickname varchar(100) not null,
    email varchar(100) not null,
    image_url varchar(255),
    password varchar(255),
    is_active boolean default 1,
    role varchar(12) default 'USER',
    social_provider_type varchar(10),
    social_id varchar(10),
    created_at datetime not null default now(),
    updated_at datetime on update now()
)