ALTER TABLE project
    drop CONSTRAINT project_user_fk;

ALTER TABLE project
    ADD CONSTRAINT project_user_fk
        FOREIGN KEY (owner_id)
            REFERENCES user (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE step
    drop CONSTRAINT step_project_fk;

ALTER TABLE step
    ADD CONSTRAINT step_project_fk
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE item
    drop CONSTRAINT item_step_fk;

ALTER TABLE item
    ADD CONSTRAINT item_step_fk
        FOREIGN KEY (step_id)
            REFERENCES step (id)
            ON DELETE CASCADE ON UPDATE NO ACTION;