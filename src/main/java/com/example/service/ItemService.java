package com.example.service;

import com.example.error.BusinessException;
import com.example.service.model.ItemModel;

import java.util.List;

public interface ItemService {

    ItemModel creatItem(ItemModel itemModel) throws BusinessException;
    List<ItemModel> listItem();
    ItemModel getItemById(Integer id);
    boolean decreaseStock(Integer itemId, Integer quantity);
    void increaseSales(Integer id, Integer quantity);

}
