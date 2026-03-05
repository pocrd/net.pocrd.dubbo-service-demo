package com.pocrd.service_demo.dao.autogen.mapper;

import com.pocrd.service_demo.dao.BaseMapperTest;
import com.pocrd.service_demo.dao.autogen.entity.UserDTO;
import com.pocrd.service_demo.dao.autogen.entity.UserDTOExample;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDTOMapper 测试类
 * 纯 MyBatis 测试，不依赖 Spring
 */
public class UserDTOMapperTest extends BaseMapperTest {

    @Test
    public void testInsert() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 准备测试数据
        UserDTO user = new UserDTO();
        user.setUsername("test_insert_" + System.currentTimeMillis());
        user.setEmail("test_insert@example.com");
        user.setPhone("13900139000");
        user.setStatus((byte) 1);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // 执行插入
        int result = mapper.insert(user);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(user.getId());
        System.out.println("插入用户成功，ID: " + user.getId());
    }

    @Test
    public void testInsertSelective() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 准备测试数据 - 只设置部分字段
        UserDTO user = new UserDTO();
        user.setUsername("test_selective_" + System.currentTimeMillis());
        user.setStatus((byte) 1);

        // 执行插入
        int result = mapper.insertSelective(user);

        // 验证结果
        assertEquals(1, result);
        assertNotNull(user.getId());
        System.out.println("选择性插入用户成功，ID: " + user.getId());
    }

    @Test
    public void testSelectByPrimaryKey() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 先插入一条数据
        UserDTO user = new UserDTO();
        user.setUsername("test_select_" + System.currentTimeMillis());
        user.setEmail("test_select@example.com");
        user.setPhone("13900139001");
        user.setStatus((byte) 1);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        mapper.insert(user);

        // 查询数据
        UserDTO found = mapper.selectByPrimaryKey(user.getId());

        // 验证结果
        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
        assertEquals(user.getEmail(), found.getEmail());
        System.out.println("查询用户成功: " + found.getUsername());
    }

    @Test
    public void testSelectByExample() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 先插入多条数据
        for (int i = 0; i < 5; i++) {
            UserDTO user = new UserDTO();
            user.setUsername("test_example_" + i + "_" + System.currentTimeMillis());
            user.setEmail("test" + i + "@example.com");
            user.setStatus((byte) (i % 2));
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            mapper.insert(user);
        }

        // 使用 Example 查询 status = 1 的用户
        UserDTOExample example = new UserDTOExample();
        example.createCriteria().andStatusEqualTo((byte) 1);

        List<UserDTO> list = mapper.selectByExample(example);

        // 验证结果
        assertNotNull(list);
        System.out.println("Example 查询成功，符合条件的用户数: " + list.size());
    }

    @Test
    public void testCountByExample() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 使用 Example 统计
        UserDTOExample example = new UserDTOExample();
        long count = mapper.countByExample(example);

        // 验证结果
        assertTrue(count >= 0);
        System.out.println("用户总数: " + count);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 先插入一条数据
        UserDTO user = new UserDTO();
        user.setUsername("test_update_" + System.currentTimeMillis());
        user.setEmail("test_update@example.com");
        user.setStatus((byte) 1);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        mapper.insert(user);

        // 选择性更新
        UserDTO updateUser = new UserDTO();
        updateUser.setId(user.getId());
        updateUser.setEmail("updated@example.com");

        int result = mapper.updateByPrimaryKeySelective(updateUser);

        // 验证结果
        assertEquals(1, result);

        // 查询验证
        UserDTO found = mapper.selectByPrimaryKey(user.getId());
        assertEquals("updated@example.com", found.getEmail());
        assertEquals(user.getUsername(), found.getUsername()); // 用户名未变
        System.out.println("选择性更新成功");
    }

    @Test
    public void testUpdateByPrimaryKey() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 先插入一条数据
        UserDTO user = new UserDTO();
        user.setUsername("test_full_update_" + System.currentTimeMillis());
        user.setEmail("test_full@example.com");
        user.setPhone("13900139002");
        user.setStatus((byte) 1);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        mapper.insert(user);

        // 完整更新
        user.setEmail("full_updated@example.com");
        user.setPhone("13900139099");
        int result = mapper.updateByPrimaryKey(user);

        // 验证结果
        assertEquals(1, result);

        // 查询验证
        UserDTO found = mapper.selectByPrimaryKey(user.getId());
        assertEquals("full_updated@example.com", found.getEmail());
        assertEquals("13900139099", found.getPhone());
        System.out.println("完整更新成功");
    }

    @Test
    public void testDeleteByPrimaryKey() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 先插入一条数据
        UserDTO user = new UserDTO();
        user.setUsername("test_delete_" + System.currentTimeMillis());
        user.setEmail("test_delete@example.com");
        user.setStatus((byte) 1);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        mapper.insert(user);
        Long id = user.getId();

        // 删除数据
        int result = mapper.deleteByPrimaryKey(id);

        // 验证结果
        assertEquals(1, result);

        // 查询验证
        UserDTO found = mapper.selectByPrimaryKey(id);
        assertNull(found);
        System.out.println("删除成功");
    }

    @Test
    public void testDeleteByExample() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 先插入测试数据
        UserDTO user = new UserDTO();
        user.setUsername("test_del_example_" + System.currentTimeMillis());
        user.setEmail("test_del@example.com");
        user.setStatus((byte) 0);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        mapper.insert(user);

        // 使用 Example 删除
        UserDTOExample example = new UserDTOExample();
        example.createCriteria().andUsernameLike("test_del_example_%");

        int result = mapper.deleteByExample(example);

        // 验证结果
        assertTrue(result >= 1);
        System.out.println("Example 删除成功，删除条数: " + result);
    }

    @Test
    public void testComplexExample() {
        UserDTOMapper mapper = getMapper(UserDTOMapper.class);
        
        // 测试复杂的 Example 查询条件
        UserDTOExample example = new UserDTOExample();
        example.createCriteria()
            .andStatusEqualTo((byte) 1)
            .andEmailIsNotNull();
        example.or(example.createCriteria()
            .andUsernameLike("user%")
            .andStatusEqualTo((byte) 0));
        example.setOrderByClause("created_at DESC");

        List<UserDTO> list = mapper.selectByExample(example);

        assertNotNull(list);
        System.out.println("复杂查询成功，结果数: " + list.size());
    }
}
