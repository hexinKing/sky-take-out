package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺操作接口")
@Slf4j
public class ShopController {

    public static final String sta="SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result setstatus(@PathVariable Integer status){
        log.info("当前设置营业状态为：{}",status==1?"营业":"打烊");
//        将数据存入Redis中
        redisTemplate.opsForValue().set(sta,status);
        return Result.success();
    }


    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getstatus(){
//        从Redis中获取SHOP_STATUS的状态码
        Integer status = (Integer) redisTemplate.opsForValue().get(sta);
        log.info("获取营业状态为：{}",status==1?"营业":"打烊");
        return Result.success(status);
    }

}
