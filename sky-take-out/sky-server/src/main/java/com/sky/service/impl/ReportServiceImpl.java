package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {


    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计接口
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
//        获取begin-end间隔的时间
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)){
            begin= begin.plusDays(1);
            localDateList.add(begin);
        }
//        查询数据库获取每天的营业额（订单状态为已完成）
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : localDateList) {
            //        格式化
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            HashMap hashMap = new HashMap();
            hashMap.put("begin",beginTime );
            hashMap.put("end",endTime );
            hashMap.put("status",Orders.COMPLETED );
            Double turnover = orderMapper.beginAndEnd(hashMap);
            turnover = turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        }
//        将集合转换为字符串
        String dateList = StringUtils.join(localDateList, ",");
        String turnover = StringUtils.join(turnoverList, ",");
        return TurnoverReportVO
                .builder()
                .dateList(dateList)
                .turnoverList(turnover)
                .build();
    }

    /**
     * 用户统计接口
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
//        获取begin-end间隔的时间
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)){
            begin= begin.plusDays(1);
            localDateList.add(begin);
        }
//        查询数据库获取用户总量和新增用户
        List<Integer> totalUserList = new ArrayList<>();//用户总量
        List<Integer> newUserList = new ArrayList<>();//新增用户
        for (LocalDate localDate : localDateList) {
//        格式化
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            HashMap hashMap = new HashMap();
            hashMap.put("end",endTime );
//           总用户数量
            Integer count = userMapper.totalUserAndnewUser(hashMap);
            totalUserList.add(count);
            hashMap.put("begin",beginTime );
//           新增用户数量
            count = userMapper.totalUserAndnewUser(hashMap);
            newUserList.add(count);

        }
//        将集合转换为字符串
        String dateList = StringUtils.join(localDateList, ",");
        String totalUser = StringUtils.join(totalUserList, ",");
        String newUser = StringUtils.join(newUserList, ",");
        return UserReportVO
                .builder()
                .dateList(dateList)
                .totalUserList(totalUser)
                .newUserList(newUser)
                .build();
    }

    /**
     * 订单统计接口
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
//        获取begin-end间隔的时间
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(begin);
        while (!begin.equals(end)){
            begin= begin.plusDays(1);
            localDateList.add(begin);
        }
//        查询数据库获取每日订单数和每日有效订单数
        List<Integer> orderCountList = new ArrayList<>();//每日订单数
        List<Integer> validOrderCountList = new ArrayList<>();//每日有效订单数
        for (LocalDate localDate : localDateList) {
//        格式化
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            HashMap hashMap = new HashMap();
            hashMap.put("end",endTime );
            hashMap.put("begin",beginTime );
//           每日订单数
            Integer count = orderMapper.ordersCount(hashMap);
            orderCountList.add(count);
//           每日有效订单数
            hashMap.put("status",Orders.COMPLETED );
            count = orderMapper.ordersCount(hashMap);
            validOrderCountList.add(count);

        }
//       通过遍历集合获取订单总数、 有效订单数、订单完成率
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validCount = validOrderCountList.stream().reduce(Integer::sum).get();
        Double orderCompletionRate = 0.0;
        if (totalOrderCount!=null) {
            orderCompletionRate = validCount.doubleValue()/totalOrderCount;
        }
//        将集合转换为字符串
        String dateList = StringUtils.join(localDateList, ",");
        String orderCount = StringUtils.join(orderCountList, ",");
        String validOrderCount = StringUtils.join(validOrderCountList, ",");
        return OrderReportVO
                .builder()
                .dateList(dateList)
                .orderCountList(orderCount)
                .validOrderCountList(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 查询销量排名top10接口
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
//        查询数据库获取每日销售的商品名称和销量数量
            //        格式化
            LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
            HashMap hashMap = new HashMap();
            hashMap.put("end",endTime );
            hashMap.put("begin",beginTime );
            hashMap.put("status",Orders.COMPLETED );
            List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSelectTop10(hashMap);
//            将集合中的某个字段加起来构建成一个新的集合
        List<String> namelist = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
//        将集合转换为字符串
        String nameList = StringUtils.join(namelist, ",");
        List<Integer> numberlist = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numberlist, ",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }
}
