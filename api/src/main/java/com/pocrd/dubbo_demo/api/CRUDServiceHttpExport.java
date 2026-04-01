package com.pocrd.dubbo_demo.api;

import com.pocrd.dubbo_demo.api.entity.ErrorCode;
import com.pocrd.dubbo_demo.api.entity.User;
import com.pocrd.dubbo_demo.api.entity.Product;
import com.pocrd.dubbo_demo.api.entity.Order;

import com.pocrd.api_publish_service.sdk.annotation.ApiGroup;
import com.pocrd.api_publish_service.sdk.annotation.Description;
import com.pocrd.api_publish_service.sdk.annotation.ApiParameter;

import java.util.List;

/**
 * CRUD service interface definition - HTTP Export via Higress Gateway
 *
 * 此接口定义的方法将通过 Higress 网关暴露给公网 HTTP 访问
 * 使用 Dubbo Triple 协议（兼容 gRPC/HTTP2）
 * 提供对 User、Product、Order 实体的 CRUD 操作
 */
@ApiGroup(name="CRUDService", minCode=200000, maxCode=209999, codeDefine=ErrorCode.class)
@Description("CRUD服务，提供用户、产品、订单的增删改查操作")
public interface CRUDServiceHttpExport {
    
    // User CRUD operations
    @Description("创建用户")
    User createUser(@ApiParameter(desc = "用户信息", required = true) User user);
    
    @Description("根据ID获取用户")
    User getUserById(@ApiParameter(desc = "用户ID", required = true) Long id);
    
    @Description("更新用户信息")
    User updateUser(@ApiParameter(desc = "用户信息", required = true) User user);
    
    @Description("删除用户")
    boolean deleteUser(@ApiParameter(desc = "用户ID", required = true) Long id);
    
    @Description("获取所有用户列表")
    List<User> getAllUsers();
    
    // Product CRUD operations  
    @Description("创建产品")
    Product createProduct(@ApiParameter(desc = "产品信息", required = true) Product product);
    
    @Description("根据ID获取产品")
    Product getProductById(@ApiParameter(desc = "产品ID", required = true) Long id);
    
    @Description("更新产品信息")
    Product updateProduct(@ApiParameter(desc = "产品信息", required = true) Product product);
    
    @Description("删除产品")
    boolean deleteProduct(@ApiParameter(desc = "产品ID", required = true) Long id);
    
    @Description("获取所有产品列表")
    List<Product> getAllProducts();
    
    // Order CRUD operations
    @Description("创建订单")
    Order createOrder(@ApiParameter(desc = "订单信息", required = true) Order order);
    
    @Description("根据ID获取订单")
    Order getOrderById(@ApiParameter(desc = "订单ID", required = true) Long id);
    
    @Description("更新订单信息")
    Order updateOrder(@ApiParameter(desc = "订单信息", required = true) Order order);
    
    @Description("删除订单")
    boolean deleteOrder(@ApiParameter(desc = "订单ID", required = true) Long id);
    
    @Description("获取所有订单列表")
    List<Order> getAllOrders();
    
}