package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AddressBookMapper {
    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    void addAddressBook(AddressBook addressBook);

    /**
     * 查询默认地址
     * @return
     */
    AddressBook listAddressBook(AddressBook addressBook);

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
    @Delete("Delete from address_book where id=#{id}")
    void deleteABID(Long id);

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    @Update("update address_book set is_default=#{isDefault} where id=#{id}")
    void putDefault(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * @param addressBook
     * @return
     */
    @Select("Select * from address_book where user_id = #{userId}")
    List<AddressBook> listuserId(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @Select("Select * from address_book where id = #{id}")
    AddressBook GetByID(Long id);
}
