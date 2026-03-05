package com.pocrd.service_demo.dao.autogen.mapper;

import com.pocrd.service_demo.dao.BaseMapperTest;
import com.pocrd.service_demo.dao.autogen.entity.ProductDTO;
import com.pocrd.service_demo.dao.autogen.entity.ProductDTOExample;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProductDTOMapper 测试类
 * 纯 MyBatis 测试，不依赖 Spring
 */
public class ProductDTOMapperTest extends BaseMapperTest {

    @Test
    public void testInsert() {
        ProductDTOMapper mapper = getMapper(ProductDTOMapper.class);
        
        ProductDTO product = new ProductDTO();
        product.setProductCode("PROD_TEST_" + System.currentTimeMillis());
        product.setProductName("测试商品");
        product.setPrice(new BigDecimal("999.99"));
        product.setStock(100);
        product.setCategory("测试分类");
        product.setIsDeleted((byte) 0);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        int result = mapper.insert(product);

        assertEquals(1, result);
        assertNotNull(product.getId());
        System.out.println("插入商品成功，ID: " + product.getId());
    }

    @Test
    public void testSelectByPrimaryKey() {
        ProductDTOMapper mapper = getMapper(ProductDTOMapper.class);
        
        ProductDTO product = new ProductDTO();
        product.setProductCode("PROD_SELECT_" + System.currentTimeMillis());
        product.setProductName("查询测试商品");
        product.setPrice(new BigDecimal("2999.99"));
        product.setStock(200);
        product.setCategory("电子产品");
        product.setIsDeleted((byte) 0);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        mapper.insert(product);

        ProductDTO found = mapper.selectByPrimaryKey(product.getId());

        assertNotNull(found);
        assertEquals(product.getProductCode(), found.getProductCode());
        assertEquals(0, product.getPrice().compareTo(found.getPrice()));
        System.out.println("查询商品成功: " + found.getProductName());
    }

    @Test
    public void testSelectByExample() {
        ProductDTOMapper mapper = getMapper(ProductDTOMapper.class);
        
        for (int i = 0; i < 5; i++) {
            ProductDTO product = new ProductDTO();
            product.setProductCode("PROD_EXAMPLE_" + i + "_" + System.currentTimeMillis());
            product.setProductName("测试商品" + i);
            product.setPrice(new BigDecimal("100.00").multiply(new BigDecimal(i + 1)));
            product.setStock(100 * (i + 1));
            product.setCategory(i % 2 == 0 ? "电子产品" : "家居用品");
            product.setIsDeleted((byte) 0);
            product.setCreatedAt(new Date());
            product.setUpdatedAt(new Date());
            mapper.insert(product);
        }

        ProductDTOExample example = new ProductDTOExample();
        example.createCriteria()
            .andCategoryEqualTo("电子产品")
            .andIsDeletedEqualTo((byte) 0);

        List<ProductDTO> list = mapper.selectByExample(example);

        assertNotNull(list);
        System.out.println("Example 查询成功，符合条件的商品数: " + list.size());
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        ProductDTOMapper mapper = getMapper(ProductDTOMapper.class);
        
        ProductDTO product = new ProductDTO();
        product.setProductCode("PROD_UPD_" + System.currentTimeMillis());
        product.setProductName("原始商品名");
        product.setPrice(new BigDecimal("3999.99"));
        product.setStock(100);
        product.setCategory("电子产品");
        product.setIsDeleted((byte) 0);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        mapper.insert(product);

        ProductDTO updateProduct = new ProductDTO();
        updateProduct.setId(product.getId());
        updateProduct.setProductName("更新后的商品名");
        updateProduct.setStock(150);

        int result = mapper.updateByPrimaryKeySelective(updateProduct);

        assertEquals(1, result);

        ProductDTO found = mapper.selectByPrimaryKey(product.getId());
        assertEquals("更新后的商品名", found.getProductName());
        assertEquals(150, found.getStock());
        System.out.println("选择性更新成功");
    }

    @Test
    public void testDeleteByPrimaryKey() {
        ProductDTOMapper mapper = getMapper(ProductDTOMapper.class);
        
        ProductDTO product = new ProductDTO();
        product.setProductCode("PROD_DEL_" + System.currentTimeMillis());
        product.setProductName("删除测试商品");
        product.setPrice(new BigDecimal("6999.99"));
        product.setStock(30);
        product.setCategory("手机");
        product.setIsDeleted((byte) 0);
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        mapper.insert(product);
        Long id = product.getId();

        int result = mapper.deleteByPrimaryKey(id);

        assertEquals(1, result);

        ProductDTO found = mapper.selectByPrimaryKey(id);
        assertNull(found);
        System.out.println("删除成功");
    }
}
