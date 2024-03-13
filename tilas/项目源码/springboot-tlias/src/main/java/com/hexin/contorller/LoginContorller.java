package com.hexin.contorller;

import com.hexin.pojo.Emp;
import com.hexin.pojo.Result;
import com.hexin.service.EmpService;
import com.hexin.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登錄
 */
@RestController
@RequestMapping("/login")
public class LoginContorller {

    @Autowired
    private EmpService empService;


    //    页面登录
    @PostMapping
    public Result SystemLogin(@RequestBody Emp emp) {
        Emp emp1 = empService.SystemLogin(emp);

//        对返回的数据Emp进行校验
        //账号密码正确
        if (emp1 != null) {
//            若emp1!=null则下发JWT令牌
            Map<String, Object> claims = new HashMap<>();//创建一个HashMap来存储数据
            claims.put("id", emp1.getId());//往HashMap中添加数据
            claims.put("username", emp1.getUsername());
            claims.put("password", emp1.getPassword());
//            放回jwt令牌
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);//想客服端返回Result格式
        }

//        账号密码错误
        return Result.error("账号或者密码错误");
    }

}
