package com.example.service.impl;

import com.example.dao.ItemDOMapper;
import com.example.dao.ItemStockDOMapper;
import com.example.dataobject.ItemDO;
import com.example.dataobject.ItemStockDO;
import com.example.dataobject.UserDO;
import com.example.error.BusinessException;
import com.example.error.EmBusinessError;
import com.example.service.ItemService;
import com.example.service.model.ItemModel;
import com.example.validator.ValidationResult;
import com.example.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemDOMapper itemDOMapper;
    @Autowired
    ValidatorImpl validator;
    @Autowired
    ItemStockDOMapper itemStockDOMapper;

    @Override
    @Transactional
    public ItemModel creatItem(ItemModel itemModel) throws BusinessException {
        if (itemModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result = validator.validate(itemModel);
        if (result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, result.getErrMsg());
        }

        ItemDO itemDO = convertItemDoFromItemModel(itemModel);
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = convertItemStockDoFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        return getItemById(itemModel.getId());
    }

    private ItemStockDO convertItemStockDoFromItemModel(ItemModel itemModel) {
        if (itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setStock(itemModel.getStock());
        itemStockDO.setItemId(itemModel.getId());
        return itemStockDO;
    }

    private ItemDO convertItemDoFromItemModel(ItemModel itemModel) {
        if (itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.itemList();
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {

        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null){
            return null;
        }

        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        ItemModel itemModel = convertModelFromDataObject(itemDO, itemStockDO);

        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer quantity) {
        return itemStockDOMapper.decreaseStock(itemId, quantity);
    }

    @Override
    @Transactional
    public void increaseSales(Integer id, Integer quantity) {
        itemDOMapper.increaseSales(id, quantity);
    }

    private ItemModel convertModelFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        if (itemDO == null || itemStockDO == null){
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
