package com.pocrd.service_demo.dao.mapper;

import com.pocrd.service_demo.dao.BaseMapperTest;
import com.pocrd.service_demo.dao.autogen.entity.ProductDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ProductDTOExtMapper 测试类
 * 纯 MyBatis 测试，不依赖 Spring
 */
public class ProductDTOExtMapperTest extends BaseMapperTest {

    @Test
    public void testBatchInsert() {
        ProductDTOExtMapper mapper = getMapper(ProductDTOExtMapper.class);
        
        List<ProductDTO> productList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductDTO product = new ProductDTO();
            product.setProductCode("BATCH_PROD_" + i + "_" + System.currentTimeMillis());
            product.setProductName("批量测试商品" + i);
            product.setPrice(new BigDecimal("100.00").multiply(new BigDecimal(i + 1)));
            product.setStock(100 * (i + 1));
            product.setCategory("批量分类");
            productList.add(product);
        }

        int result = mapper.batchInsert(productList);

        assertEquals(5, result);
        System.out.println("批量插入成功，插入条数: " + result);
    }

    @Test
    public void testSelectByCategory() {
        ProductDTOExtMapper mapper = getMapper(ProductDTOExtMapper.class);
        
        String testCategory = "测试电子分类_" + System.currentTimeMillis();
        List<ProductDTO> productList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductDTO product = new ProductDTO();
            product.setProductCode("CAT_PROD_" + i + "_" + System.currentTimeMillis());
            product.setProductName("分类测试商品" + i);
            product.setPrice(new BigDecimal("999.00"));
            product.setStock(50);
            product.setCategory(testCategory);
            productList.add(product);
        }
        mapper.batchInsert(productList);

        List<ProductDTO> found = mapper.selectByCategory(testCategory);

        assertNotNull(found);
        assertTrue(found.size() >= 5);
        System.out.println("按分类查询成功，商品数: " + found.size());
    }

    @Test
    public void testDeductStock() {
        ProductDTOExtMapper mapper = getMapper(ProductDTOExtMapper.class);
        
        ProductDTO product = new ProductDTO();
        product.setProductCode("STOCK_PROD_" + System.currentTimeMillis());
        product.setProductName("库存测试商品");
        product.setPrice(new BigDecimal("999.00"));
        product.setStock(100);
        product.setCategory("库存测试分类");

        List<ProductDTO> list = new ArrayList<>();
        list.add(product);
        mapper.batchInsert(list);

        List<ProductDTO> inserted = mapper.selectByProductCodeLike("STOCK_PROD_");
        assertFalse(inserted.isEmpty());
        Long id = inserted.get(0).getId();

        int result = mapper.deductStock(id, 30);

        assertEquals(1, result);
        System.out.println("扣减库存成功");
    }

    @Test
    public void testDeductStockInsufficient() {
        ProductDTOExtMapper mapper = getMapper(ProductDTOExtMapper.class);
        
        ProductDTO product = new ProductDTO();
        product.setProductCode("STOCK_LOW_" + System.currentTimeMillis());
        product.setProductName("低库存测试商品");
        product.setPrice(new BigDecimal("999.00"));
        product.setStock(10);
        product.setCategory("库存测试分类");

        List<ProductDTO> list = new ArrayList<>();
        list.add(product);
        mapper.batchInsert(list);

        List<ProductDTO> inserted = mapper.selectByProductCodeLike("STOCK_LOW_");
        assertFalse(inserted.isEmpty());
        Long id = inserted.get(0).getId();

        int result = mapper.deductStock(id, 100);

        assertEquals(0, result);
        System.out.println("库存不足测试成功");
    }

    @Test
    public void testSelectLowStock() {
        ProductDTOExtMapper mapper = getMapper(ProductDTOExtMapper.class);
        
        List<ProductDTO> productList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductDTO product = new ProductDTO();
            product.setProductCode("LOW_STOCK_" + i + "_" + System.currentTimeMillis());
            product.setProductName("低库存商品" + i);
            product.setPrice(new BigDecimal("500.00"));
            product.setStock(5 * (i + 1));
            product.setCategory("低库存测试分类");
            productList.add(product);
        }
        mapper.batchInsert(productList);

        int threshold = 20;
        List<ProductDTO> found = mapper.selectLowStock(threshold);

        assertNotNull(found);
        System.out.println("低库存查询成功，库存不足商品数: " + found.size());
    }
}
