package com.example.service;

import com.example.error.BusinessException;
import com.example.service.model.UserModel;

public interface UserService {

    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;
    UserModel validateLogin(String telephone, String encrptPassword) throws BusinessException;
}
