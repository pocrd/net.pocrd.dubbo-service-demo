package com.pocrd.service_demo.api;

import com.pocrd.service_demo.api.entity.ErrorCode;
import com.pocrd.service_demo.api.entity.User;
import com.pocrd.service_demo.api.entity.Product;
import com.pocrd.service_demo.api.entity.Order;

import com.pocrd.api_publish_service.sdk.annotation.ApiGroup;

import java.util.List;

/**
 * CRUD service interface definition - HTTP Export via Higress Gateway
 *
 * 此接口定义的方法将通过 Higress 网关暴露给公网 HTTP 访问
 * 使用 Dubbo Triple 协议（兼容 gRPC/HTTP2）
 * 提供对 User、Product、Order 实体的 CRUD 操作
 */
@ApiGroup(name="CRUDService", minCode=200000, maxCode=209999, codeDefine=ErrorCode.class)
public interface CRUDServiceHttpExport {
    
    // User CRUD operations
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(User user);
    boolean deleteUser(Long id);
    List<User> getAllUsers();
    
    // Product CRUD operations  
    Product createProduct(Product product);
    Product getProductById(Long id);
    Product updateProduct(Product product);
    boolean deleteProduct(Long id);
    List<Product> getAllProducts();
    
    // Order CRUD operations
    Order createOrder(Order order);
    Order getOrderById(Long id);
    Order updateOrder(Order order);
    boolean deleteOrder(Long id);
    List<Order> getAllOrders();
    
}