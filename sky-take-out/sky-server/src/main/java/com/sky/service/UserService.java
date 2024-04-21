package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    /**
     * 登录
     * @return
     * @param userLoginDTO
     */
    User landing(UserLoginDTO userLoginDTO) ;
}
