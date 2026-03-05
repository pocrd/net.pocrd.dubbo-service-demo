package com.pocrd.service_demo.dao.autogen.mapper;

import com.pocrd.service_demo.dao.BaseMapperTest;
import com.pocrd.service_demo.dao.autogen.entity.OrderDTO;
import com.pocrd.service_demo.dao.autogen.entity.OrderDTOExample;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderDTOMapper 测试类
 * 纯 MyBatis 测试，不依赖 Spring
 */
public class OrderDTOMapperTest extends BaseMapperTest {

    @Test
    public void testInsert() {
        OrderDTOMapper mapper = getMapper(OrderDTOMapper.class);
        
        OrderDTO order = new OrderDTO();
        order.setOrderNo("ORD_TEST_" + System.currentTimeMillis());
        order.setUserId(1L);
        order.setAmount(new BigDecimal("999.99"));
        order.setStatus((byte) 0);
        order.setRemark("测试订单");
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());

        int result = mapper.insert(order);

        assertEquals(1, result);
        assertNotNull(order.getId());
        System.out.println("插入订单成功，ID: " + order.getId());
    }

    @Test
    public void testInsertSelective() {
        OrderDTOMapper mapper = getMapper(OrderDTOMapper.class);
        
        OrderDTO order = new OrderDTO();
        order.setOrderNo("ORD_SEL_" + System.currentTimeMillis());
        order.setUserId(2L);
        order.setAmount(new BigDecimal("1999.99"));
        order.setStatus((byte) 1);

        int result = mapper.insertSelective(order);

        assertEquals(1, result);
        assertNotNull(order.getId());
        System.out.println("选择性插入订单成功，ID: " + order.getId());
    }

    @Test
    public void testSelectByPrimaryKey() {
        OrderDTOMapper mapper = getMapper(OrderDTOMapper.class);
        
        OrderDTO order = new OrderDTO();
        order.setOrderNo("ORD_SELECT_" + System.currentTimeMillis());
        order.setUserId(3L);
        order.setAmount(new BigDecimal("2999.99"));
        order.setStatus((byte) 0);
        order.setRemark("查询测试订单");
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        mapper.insert(order);

        OrderDTO found = mapper.selectByPrimaryKey(order.getId());

        assertNotNull(found);
        assertEquals(order.getOrderNo(), found.getOrderNo());
        assertEquals(0, order.getAmount().compareTo(found.getAmount()));
        System.out.println("查询订单成功: " + found.getOrderNo());
    }

    @Test
    public void testSelectByExample() {
        OrderDTOMapper mapper = getMapper(OrderDTOMapper.class);
        
        for (int i = 0; i < 5; i++) {
            OrderDTO order = new OrderDTO();
            order.setOrderNo("ORD_EXAMPLE_" + i + "_" + System.currentTimeMillis());
            order.setUserId((long) (i + 1));
            order.setAmount(new BigDecimal("1000.00").multiply(new BigDecimal(i + 1)));
            order.setStatus((byte) (i % 3));
            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());
            mapper.insert(order);
        }

        OrderDTOExample example = new OrderDTOExample();
        example.createCriteria().andStatusEqualTo((byte) 1);

        List<OrderDTO> list = mapper.selectByExample(example);

        assertNotNull(list);
        System.out.println("Example 查询成功，符合条件的订单数: " + list.size());
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        OrderDTOMapper mapper = getMapper(OrderDTOMapper.class);
        
        OrderDTO order = new OrderDTO();
        order.setOrderNo("ORD_UPD_" + System.currentTimeMillis());
        order.setUserId(4L);
        order.setAmount(new BigDecimal("3999.99"));
        order.setStatus((byte) 0);
        order.setRemark("原始备注");
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        mapper.insert(order);

        OrderDTO updateOrder = new OrderDTO();
        updateOrder.setId(order.getId());
        updateOrder.setRemark("更新后的备注");
        updateOrder.setStatus((byte) 1);

        int result = mapper.updateByPrimaryKeySelective(updateOrder);

        assertEquals(1, result);

        OrderDTO found = mapper.selectByPrimaryKey(order.getId());
        assertEquals("更新后的备注", found.getRemark());
        assertEquals((byte) 1, found.getStatus());
        System.out.println("选择性更新成功");
    }

    @Test
    public void testDeleteByPrimaryKey() {
        OrderDTOMapper mapper = getMapper(OrderDTOMapper.class);
        
        OrderDTO order = new OrderDTO();
        order.setOrderNo("ORD_DEL_" + System.currentTimeMillis());
        order.setUserId(6L);
        order.setAmount(new BigDecimal("6999.99"));
        order.setStatus((byte) 0);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        mapper.insert(order);
        Long id = order.getId();

        int result = mapper.deleteByPrimaryKey(id);

        assertEquals(1, result);

        OrderDTO found = mapper.selectByPrimaryKey(id);
        assertNull(found);
        System.out.println("删除成功");
    }
}
