package com.example.controller;

import com.example.error.BusinessException;
import com.example.error.EmBusinessError;
import com.example.returntype.CommonReturnType;
import com.example.service.OrderService;
import com.example.service.model.OrderModel;
import com.example.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping(value = "/create", consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name="itemId") Integer itemId,
                                        @RequestParam(name="promoId") Integer promoId,
                                        @RequestParam(name="quantity") Integer quantity) throws BusinessException {
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("LOGIN");
        if (isLogin == null || !isLogin){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoId, quantity);
        return CommonReturnType.create(orderModel);
    }

}
