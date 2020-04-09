package com.example.service;

import com.example.error.BusinessException;
import com.example.service.model.OrderModel;

public interface OrderService {

    OrderModel createOrder(Integer userId, Integer itemId, Integer promoId, Integer quantity) throws BusinessException;
}
