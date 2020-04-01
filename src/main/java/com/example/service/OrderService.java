package com.example.service;

import com.example.error.BusinessException;
import com.example.service.model.OrderModel;

public interface OrderService {

    OrderModel createOrder(Integer userId, Integer itemId, Integer quantity) throws BusinessException;
}
