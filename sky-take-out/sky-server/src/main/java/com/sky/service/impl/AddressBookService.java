package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressBookService implements AddressBookServiceImpl {


    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @Override
    public void addAddressBook(AddressBook addressBook) {
//        获取用户id
        Long  userid = BaseContext.getCurrentId();
        addressBook.setUserId(userid);
//        log.info("新增地址为：{}",addressBook);
//        判断是否为初次新增地址，是则设置为默认地址，否则相反
        List<AddressBook> addressBookList = addressBookMapper.listuserId(addressBook);
        if (addressBookList==null || addressBookList.size()==0) {
            addressBook.setIsDefault(StatusConstant.ENABLE);
            addressBookMapper.addAddressBook(addressBook);
        }else {
            addressBook.setIsDefault(StatusConstant.DISABLE);
            addressBookMapper.addAddressBook(addressBook);
        }
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    @Override
    public List<AddressBook> listAddressBook() {
        AddressBook addressBook = new AddressBook();
//        获取用户id
        Long  userid = BaseContext.getCurrentId();
        addressBook.setUserId(userid);
//        根据用户ID查询用户信息
        List<AddressBook> addressBookList = addressBookMapper.listuserId(addressBook);
        return addressBookList;
    }

    /**
     * 查询默认地址
     * @return
     */
    @Override
    public AddressBook getIsdefault() {
        AddressBook addressBook = new AddressBook();
//        获取用户id
        Long  userid = BaseContext.getCurrentId();
        addressBook.setUserId(userid);
        addressBook.setIsDefault(StatusConstant.ENABLE);
//        根据用户id和默认地址查询信息
        addressBook = addressBookMapper.listAddressBook(addressBook);
        return addressBook;
    }

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    @Override
    public void updateAddressBook(AddressBook addressBook) {
        addressBookMapper.updateAddressBook(addressBook);
    }

    /**
     * 根据id删除地址
     * @return
     */
    @Override
    public void deleteABID(Long id) {
        addressBookMapper.deleteABID(id);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Override
    public AddressBook GetByID(Long id) {
        AddressBook addressBook = addressBookMapper.GetByID(id);
        return addressBook;
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @Override
    public void putDefault(AddressBook addressBook) {
//        查询是否有默认地址，有则取消默认
//        获取用户id
        Long  userid = BaseContext.getCurrentId();
        addressBook.setUserId(userid);
//        设置默认地址
        addressBook.setIsDefault(StatusConstant.ENABLE);
//        根据用户id和默认地址查询信息
        AddressBook addressBook1 = addressBookMapper.listAddressBook(addressBook);
        if (addressBook1!=null) {
//            取消原来的默认地址
            addressBook1.setIsDefault(StatusConstant.DISABLE);
            addressBookMapper.putDefault(addressBook1);
        }
//        设置新的默认地址
        addressBookMapper.putDefault(addressBook);
    }
}
