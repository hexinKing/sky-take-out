package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询User信息
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User selectOpenid(String openid);

    /**
     * 添加新用户
     * @param user
     */
    void addUser(User user);

    /**
     * 根据id查询用户表
     * @param userID
     * @return
     */
    @Select("Select * from user where id=#{id}")
    User getById(Long userID);

    /**
     * 获取每日的用户总量和新增用户
     * @param hashMap
     * @return
     */
    Integer totalUserAndnewUser(Map hashMap);
}
