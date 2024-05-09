package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookServiceImpl {

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    void addAddressBook(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * @return
     */
    List<AddressBook> listAddressBook();

    /**
     * 查询默认地址
     * @return
     */
    AddressBook getIsdefault();

    /**
     * 根据id修改地址
     * @param addressBook
     * @return
     */
    void updateAddressBook(AddressBook addressBook);

    /**
     * 根据id删除地址
     * @return
     */
    void deleteABID(Long id);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    AddressBook GetByID(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    void putDefault(AddressBook addressBook);
}
