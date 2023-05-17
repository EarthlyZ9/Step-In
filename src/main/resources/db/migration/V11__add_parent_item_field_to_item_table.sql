ALTER TABLE item ADD parent_item_id bigint;
ALTER TABLE item ADD CONSTRAINT fk_item_parent_item FOREIGN KEY(parent_item_id)
    REFERENCES item (id) ON DELETE SET NULL;