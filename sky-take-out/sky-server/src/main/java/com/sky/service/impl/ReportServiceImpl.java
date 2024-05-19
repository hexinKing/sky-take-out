package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {


    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

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
            Map hashMap = new HashMap();
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
            Map hashMap = new HashMap();
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
            Map hashMap = new HashMap();
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
            Map hashMap = new HashMap();
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

    /**
     * 导出Excel报表
     * @return
     */
    @Override
    public void export(HttpServletResponse response) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
