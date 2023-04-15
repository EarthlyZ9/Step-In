package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.entities.ItemPatchRequest;
import java.util.List;

public interface ItemService {
    List<Item> getItems();

    Item getItemById(Integer itemId);

    Item createItem(Item newItem, Integer stepId);

    Item partialUpdateItem(Integer itemId, ItemPatchRequest newItem);

    void deleteItemById(Integer itemId);
}
