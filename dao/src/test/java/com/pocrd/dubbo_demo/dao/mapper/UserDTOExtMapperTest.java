package com.pocrd.dubbo_demo.dao.mapper;

import com.pocrd.dubbo_demo.dao.BaseMapperTest;
import com.pocrd.dubbo_demo.dao.autogen.entity.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserDTOExtMapper 测试类
 * 纯 MyBatis 测试，不依赖 Spring
 */
public class UserDTOExtMapperTest extends BaseMapperTest {

    @Test
    public void testBatchInsert() {
        UserDTOExtMapper mapper = getMapper(UserDTOExtMapper.class);
        
        List<UserDTO> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserDTO user = new UserDTO();
            user.setUsername("batch_user_" + i + "_" + System.currentTimeMillis());
            user.setEmail("batch" + i + "@example.com");
            user.setPhone("1390000000" + i);
            user.setStatus((byte) 1);
            userList.add(user);
        }

        int result = mapper.batchInsert(userList);

        assertEquals(5, result);
        System.out.println("批量插入成功，插入条数: " + result);
    }

    @Test
    public void testSelectByUsernameLike() {
        UserDTOExtMapper mapper = getMapper(UserDTOExtMapper.class);
        
        List<UserDTO> userList = new ArrayList<>();
        UserDTO user1 = new UserDTO();
        user1.setUsername("test_special_user_" + System.currentTimeMillis());
        user1.setEmail("special1@example.com");
        user1.setStatus((byte) 1);
        userList.add(user1);

        UserDTO user2 = new UserDTO();
        user2.setUsername("another_special_user_" + System.currentTimeMillis());
        user2.setEmail("special2@example.com");
        user2.setStatus((byte) 1);
        userList.add(user2);

        mapper.batchInsert(userList);

        List<UserDTO> found = mapper.selectByUsernameLike("special");

        assertNotNull(found);
        assertTrue(found.size() >= 2);
        System.out.println("模糊查询成功，找到用户数: " + found.size());
    }

    @Test
    public void testSelectByStatus() {
        UserDTOExtMapper mapper = getMapper(UserDTOExtMapper.class);
        
        List<UserDTO> activeUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            UserDTO user = new UserDTO();
            user.setUsername("active_user_" + i + "_" + System.currentTimeMillis());
            user.setEmail("active" + i + "@example.com");
            user.setStatus((byte) 1);
            activeUsers.add(user);
        }
        mapper.batchInsert(activeUsers);

        List<UserDTO> activeList = mapper.selectByStatus((byte) 1);

        assertNotNull(activeList);
        System.out.println("启用的用户数: " + activeList.size());
    }

    @Test
    public void testBatchUpdateStatus() {
        UserDTOExtMapper mapper = getMapper(UserDTOExtMapper.class);
        
        List<UserDTO> userList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserDTO user = new UserDTO();
            user.setUsername("batch_status_" + i + "_" + System.currentTimeMillis());
            user.setEmail("status" + i + "@example.com");
            user.setStatus((byte) 1);
            userList.add(user);
        }
        mapper.batchInsert(userList);

        List<UserDTO> inserted = mapper.selectByUsernameLike("batch_status_");
        List<Long> ids = new ArrayList<>();
        for (UserDTO user : inserted) {
            ids.add(user.getId());
        }

        int result = mapper.batchUpdateStatus(ids, (byte) 0);

        assertTrue(result > 0);
        System.out.println("批量更新状态成功，更新条数: " + result);
    }

    @Test
    public void testLogicDeleteById() {
        UserDTOExtMapper mapper = getMapper(UserDTOExtMapper.class);
        
        UserDTO user = new UserDTO();
        user.setUsername("logic_delete_test_" + System.currentTimeMillis());
        user.setEmail("logic_delete@example.com");
        user.setStatus((byte) 1);

        List<UserDTO> list = new ArrayList<>();
        list.add(user);
        mapper.batchInsert(list);

        List<UserDTO> inserted = mapper.selectByUsernameLike("logic_delete_test_");
        assertFalse(inserted.isEmpty());
        Long id = inserted.get(0).getId();

        int result = mapper.logicDeleteById(id);

        assertEquals(1, result);
        System.out.println("逻辑删除成功");
    }
}
