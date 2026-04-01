package com.pocrd.dubbo_demo.dao.mapper;

import com.pocrd.dubbo_demo.dao.BaseMapperTest;
import com.pocrd.dubbo_demo.dao.autogen.entity.OrderDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderDTOExtMapper 测试类
 * 纯 MyBatis 测试，不依赖 Spring
 */
public class OrderDTOExtMapperTest extends BaseMapperTest {

    @Test
    public void testBatchInsert() {
        OrderDTOExtMapper mapper = getMapper(OrderDTOExtMapper.class);
        
        List<OrderDTO> orderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OrderDTO order = new OrderDTO();
            order.setOrderNo("BATCH_ORD_" + i + "_" + System.currentTimeMillis());
            order.setUserId((long) (i + 1));
            order.setAmount(new BigDecimal("1000.00").multiply(new BigDecimal(i + 1)));
            order.setStatus((byte) (i % 3));
            order.setRemark("批量插入测试订单" + i);
            orderList.add(order);
        }

        int result = mapper.batchInsert(orderList);

        assertEquals(5, result);
        System.out.println("批量插入成功，插入条数: " + result);
    }

    @Test
    public void testSelectByUserId() {
        OrderDTOExtMapper mapper = getMapper(OrderDTOExtMapper.class);
        
        Long testUserId = 999L;
        List<OrderDTO> orderList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            OrderDTO order = new OrderDTO();
            order.setOrderNo("USER_ORD_" + i + "_" + System.currentTimeMillis());
            order.setUserId(testUserId);
            order.setAmount(new BigDecimal("2000.00"));
            order.setStatus((byte) 1);
            order.setRemark("用户订单" + i);
            orderList.add(order);
        }
        mapper.batchInsert(orderList);

        List<OrderDTO> found = mapper.selectByUserId(testUserId);

        assertNotNull(found);
        assertTrue(found.size() >= 3);
        System.out.println("按用户ID查询成功，订单数: " + found.size());
    }

    @Test
    public void testSelectByAmountRange() {
        OrderDTOExtMapper mapper = getMapper(OrderDTOExtMapper.class);
        
        List<OrderDTO> orderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderDTO order = new OrderDTO();
            order.setOrderNo("AMOUNT_ORD_" + i + "_" + System.currentTimeMillis());
            order.setUserId((long) (i + 1));
            order.setAmount(new BigDecimal("500.00").multiply(new BigDecimal(i + 1)));
            order.setStatus((byte) 1);
            orderList.add(order);
        }
        mapper.batchInsert(orderList);

        BigDecimal minAmount = new BigDecimal("1000.00");
        BigDecimal maxAmount = new BigDecimal("3000.00");
        List<OrderDTO> found = mapper.selectByAmountRange(minAmount, maxAmount);

        assertNotNull(found);
        System.out.println("金额范围查询成功，找到订单数: " + found.size());
    }

    @Test
    public void testSumAmountByUserId() {
        OrderDTOExtMapper mapper = getMapper(OrderDTOExtMapper.class);
        
        Long testUserId = 888L;
        List<OrderDTO> orderList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OrderDTO order = new OrderDTO();
            order.setOrderNo("SUM_ORD_" + i + "_" + System.currentTimeMillis());
            order.setUserId(testUserId);
            order.setAmount(new BigDecimal("1000.00").multiply(new BigDecimal(i + 1)));
            order.setStatus((byte) 1);
            orderList.add(order);
        }
        mapper.batchInsert(orderList);

        BigDecimal sum = mapper.sumAmountByUserId(testUserId);

        assertNotNull(sum);
        System.out.println("用户订单总金额统计成功，总金额: " + sum);
    }
}
