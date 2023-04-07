package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Category;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.entities.ItemPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.ItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService{
    private final ItemRepository itemRepository;
    private final CategoryServiceImpl categoryServiceImpl;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, CategoryServiceImpl categoryServiceImpl) {
        this.itemRepository = itemRepository;
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(Integer itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) throw new NotFoundException("item with the provided id does not exist");
        return item.get();
    }

    @Override
    @Transactional
    public Item createItem(Item item, Integer categoryId) throws NotFoundException {
        Category currentCategory = categoryServiceImpl.getCategoryById(categoryId);
        item.setId(0);
        item.setCategoryId(categoryId);
        Item newItem = itemRepository.save(item);
        newItem.setCategory(currentCategory);
        return newItem;
    }

    @Override
    @Transactional
    public Item partialUpdateItem(Integer itemId, ItemPatchRequest newItem) throws NotFoundException{
        Item object = getItemById(itemId);
        object.setContent(newItem.getContent());
        return itemRepository.save(object);
    }

    @Override
    @Transactional
    public void deleteItemById(Integer itemId) throws NotFoundException{
        getItemById(itemId);
        itemRepository.deleteById(itemId);
    }
}
