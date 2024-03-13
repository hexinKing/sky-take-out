package com.hexin.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询结果的封装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {

    private Long total;//总记录数
    private List rows;//当前页数据列表

}
