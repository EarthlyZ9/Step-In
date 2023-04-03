ALTER TABLE category
ADD number bigint NOT NULL,
ADD UNIQUE (name, project_id)