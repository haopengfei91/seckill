package com.example.service.impl;

import com.example.dao.OrderDOMapper;
import com.example.dao.SequenceDOMapper;
import com.example.dataobject.OrderDO;
import com.example.dataobject.SequenceDO;
import com.example.error.BusinessException;
import com.example.error.EmBusinessError;
import com.example.service.ItemService;
import com.example.service.OrderService;
import com.example.service.UserService;
import com.example.service.model.OrderModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    OrderDOMapper orderDOMapper;
    @Autowired
    SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer quantity) throws BusinessException {
        if (userService.getUserById(userId) == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户不存在");
        }
        if (itemService.getItemById(itemId) == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商品不存在");
        }
        if (quantity < 0 || quantity > 99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "购买数量错误");
        }
        boolean result = itemService.decreaseStock(itemId, quantity);
        if (!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        OrderModel orderModel = new OrderModel();
        orderModel.setId(generateOrderNO());
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setQuantity(quantity);
        orderModel.setItemPrice(itemService.getItemById(itemId).getPrice());
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(quantity)));

        OrderDO orderDO = convertDOFromModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        itemService.increaseSales(itemId, quantity);
        return orderModel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private String generateOrderNO() {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowdate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowdate);

        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        int sequence = sequenceDO.getCurrentValue();
        int step = sequenceDO.getStep();
        sequenceDO.setCurrentValue(sequence + step);
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i=0; i<6-sequenceStr.length(); i++){
            stringBuilder.append("0");
        }
        stringBuilder.append(sequenceStr);
        stringBuilder.append("00");
        return stringBuilder.toString();
    }

    private OrderDO convertDOFromModel(OrderModel orderModel) {
        if (orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
