ALTER TABLE project
    drop CONSTRAINT project_user_fk;

ALTER TABLE project
    ADD CONSTRAINT project_user_fk
        FOREIGN KEY (owner_id)
            REFERENCES user (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE category
    drop CONSTRAINT category_project_fk;

ALTER TABLE category
    ADD CONSTRAINT category_project_fk
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE item
    drop CONSTRAINT item_category_fk;

ALTER TABLE item
    ADD CONSTRAINT item_category_fk
        FOREIGN KEY (category_id)
            REFERENCES category (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;