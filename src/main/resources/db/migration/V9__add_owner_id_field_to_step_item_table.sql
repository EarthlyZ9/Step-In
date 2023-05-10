ALTER TABLE step ADD owner_id bigint NOT NULL;
ALTER TABLE step ADD CONSTRAINT fk_step_owner FOREIGN KEY(owner_id)
    REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE item ADD owner_id bigint NOT NULL;
ALTER TABLE item ADD CONSTRAINT fk_item_owner FOREIGN KEY(owner_id)
    REFERENCES user (id) ON DELETE CASCADE;