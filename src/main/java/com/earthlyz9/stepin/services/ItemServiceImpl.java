package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Step;
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
    private final StepServiceImpl stepServiceImpl;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, StepServiceImpl stepServiceImpl) {
        this.itemRepository = itemRepository;
        this.stepServiceImpl = stepServiceImpl;
    }

    @Override
    public List<Item> getItemsByStepId(Integer stepId) throws NotFoundException {
        stepServiceImpl.getStepById(stepId);
        return itemRepository.findByStepId(stepId);
    }

    @Override
    public Item getItemById(Integer itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) throw new NotFoundException("item with the provided id does not exist");
        return item.get();
    }

    @Override
    @Transactional
    public Item createItem(Item item, Integer stepId) throws NotFoundException {
        Step currentStep = stepServiceImpl.getStepById(stepId);
        item.setId(0);
        item.setStepId(stepId);
        Item newItem = itemRepository.save(item);
        newItem.setStep(currentStep);
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
