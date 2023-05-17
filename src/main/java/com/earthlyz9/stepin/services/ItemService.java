package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.dto.item.ItemCreateRequest;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.dto.item.ItemPatchRequest;
import java.util.List;

public interface ItemService {
    List<Item> getItemsByStepId(Integer stepId);

    Item getItemById(Integer itemId);

    Item createItem(ItemCreateRequest newItem, Integer stepId);

    Item partialUpdateItem(Integer itemId, ItemPatchRequest newItem);

    void deleteItemById(Integer itemId);
}
