package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookServiceImpl addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result addAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.addAddressBook(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> listAddressBook(){
        List<AddressBook> addressBook = addressBookService.listAddressBook();
        return Result.success(addressBook);
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getIsdefault(){
        AddressBook addressBook = addressBookService.getIsdefault();
        return Result.success(addressBook);
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result updateAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateAddressBook(addressBook);
        return Result.success();
    }


    /**
     * 根据id删除地址
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result deleteABID(Long id){
        addressBookService.deleteABID(id);
        return Result.success();
    }


    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result GetByID(@PathVariable Long id){
        AddressBook addressBook = addressBookService.GetByID(id);
        return Result.success(addressBook);
    }


    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result putDefault(@RequestBody AddressBook addressBook){
        addressBookService.putDefault(addressBook);
        log.info("地址的id为：{}",addressBook);
        return Result.success();
    }


}
