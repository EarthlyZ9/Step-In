ALTER TABLE user
    MODIFY COLUMN is_active bool default true,
    MODIFY COLUMN role varchar(20) default 'ROLE_USER';