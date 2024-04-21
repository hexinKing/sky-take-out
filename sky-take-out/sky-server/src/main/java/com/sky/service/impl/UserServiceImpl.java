package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String WX_logid="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 登录
     * @return
     * @param userLoginDTO
     */
    @Override
    public User landing(UserLoginDTO userLoginDTO)  {
//        通过HttpClient发送get请求获取当前用户的openid
        String openid = getOpenid(userLoginDTO.getCode());
//        检索判断该用户是否为新用户，即openid是否存在
        User user = userMapper.selectOpenid(openid);
//        判断openid是否为null，是则登录失败抛出异常
        if (openid==null){
            throw new UserNotLoginException(MessageConstant.LOGIN_FAILED);
        }
//        如果是新用户则将其添加到数据库中
        if (user==null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.addUser(user);
        }
        return user;
    }


    /**
     * 通过工具类WeChatProperties的get方法获取当前用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("appid",weChatProperties.getAppid());
        hashMap.put("secret",weChatProperties.getSecret());
        hashMap.put("js_code",code);
        hashMap.put("grant_type", "authorization_code");
//        返回json格式的数据包
        String json = HttpClientUtil.doGet(WX_logid, hashMap);
//        解析获取openid
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }


}
