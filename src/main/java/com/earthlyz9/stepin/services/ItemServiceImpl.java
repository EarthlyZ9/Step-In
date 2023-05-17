package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.dto.item.ItemCreateRequest;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.dto.item.ItemPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.exceptions.PermissionDeniedException;
import com.earthlyz9.stepin.repositories.ItemRepository;
import com.earthlyz9.stepin.utils.AuthUtils;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final StepServiceImpl stepServiceImpl;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, StepServiceImpl stepServiceImpl) {
        this.itemRepository = itemRepository;
        this.stepServiceImpl = stepServiceImpl;
    }

    @Override
    public List<Item> getItemsByStepId(Integer stepId)
        throws NotFoundException, PermissionDeniedException {
        Step step = stepServiceImpl.getStepById(stepId);
        step.checkPermission(AuthUtils.getRequestUserId());
        return itemRepository.findByStepId(stepId);
    }

    @Override
    public Item getItemById(Integer itemId) throws NotFoundException, PermissionDeniedException {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new NotFoundException("item with the provided id does not exist");
        }
        Item item = optionalItem.get();
        item.checkPermission(AuthUtils.getRequestUserId());
        return item;
    }

    @Override
    @Transactional
    public Item createItem(ItemCreateRequest newItem, Integer stepId)
        throws NotFoundException, PermissionDeniedException {
        Step currentStep = stepServiceImpl.getStepById(stepId);
        Item parentItem = getItemById(newItem.getParentItemId());

        int requestUserId = AuthUtils.getRequestUserId();
        currentStep.checkPermission(requestUserId);

        newItem.setId(0);
        newItem.setStepId(stepId);
        newItem.setOwnerId(requestUserId);
        newItem.setParentItemId(parentItem.getId());

        Item item = ItemCreateRequest.toEntity(newItem);

        Item savedItem = itemRepository.save(item);
        savedItem.setStep(currentStep);
        return savedItem;
    }

    @Override
    @Transactional
    public Item partialUpdateItem(Integer itemId, ItemPatchRequest newItem)
        throws NotFoundException, PermissionDeniedException {
        Item object = getItemById(itemId);
        object.checkPermission(AuthUtils.getRequestUserId());
        object.setContent(newItem.getContent());
        return itemRepository.save(object);
    }

    @Override
    @Transactional
    public void deleteItemById(Integer itemId) throws NotFoundException, PermissionDeniedException {
        Item targetItem = getItemById(itemId);
        targetItem.checkPermission(AuthUtils.getRequestUserId());
        itemRepository.deleteById(itemId);
    }
}
