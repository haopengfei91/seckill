package com.example.service.impl;

import com.example.dao.OrderDOMapper;
import com.example.dataobject.OrderDO;
import com.example.error.BusinessException;
import com.example.error.EmBusinessError;
import com.example.returntype.CommonReturnType;
import com.example.service.ItemService;
import com.example.service.OrderService;
import com.example.service.UserService;
import com.example.service.model.OrderModel;
import com.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    OrderDOMapper orderDOMapper;

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
        orderModel.setId(generateNO());
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setQuantity(quantity);
        orderModel.setItemPrice(itemService.getItemById(itemId).getPrice());
        orderModel.setOrderPrice(orderModel.getItemPrice().multiply(new BigDecimal(quantity)));

        OrderDO orderDO = convertDOFromModel(orderModel);
        orderDOMapper.insertSelective(orderDO);
        return orderModel;
    }

    private String generateNO() {

        return "2000";
    }

    private OrderDO convertDOFromModel(OrderModel orderModel) {
        if (orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        return orderDO;
    }
}
