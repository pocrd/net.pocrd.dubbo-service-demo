package com.pocrd.service_demo.service.impl;

import com.pocrd.service_demo.api.CRUDServiceHttpExport;
import com.pocrd.service_demo.api.entity.User;
import com.pocrd.service_demo.api.entity.Product;
import com.pocrd.service_demo.api.entity.Order;
import com.pocrd.service_demo.dao.autogen.entity.UserDTO;
import com.pocrd.service_demo.dao.autogen.entity.ProductDTO;
import com.pocrd.service_demo.dao.autogen.entity.OrderDTO;
import com.pocrd.service_demo.dao.autogen.mapper.UserDTOMapper;
import com.pocrd.service_demo.dao.autogen.mapper.ProductDTOMapper;
import com.pocrd.service_demo.dao.autogen.mapper.OrderDTOMapper;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CRUDService HTTP Export Implementation
 * 
 * 此实现类暴露到 Triple 协议（端口 50051）：供 Higress 网关 HTTP 调用
 */
@DubboService(version = "1.0.0", group = "public", registry = "nacos-public", protocol = "tri", path = "dapi")
public class CRUDServiceHttpExportImpl implements CRUDServiceHttpExport {

    @Autowired
    private UserDTOMapper userDTOMapper;
    
    @Autowired
    private ProductDTOMapper productDTOMapper;
    
    @Autowired
    private OrderDTOMapper orderDTOMapper;

    // User CRUD operations
    @Override
    public User createUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.id());
        userDTO.setUsername(user.username());
        userDTO.setEmail(user.email());
        userDTO.setPhone(user.phone());
        // 设置默认状态
        userDTO.setStatus((byte) 1);
        // 使用 insertSelective，不设置 createdAt 和 updatedAt，让数据库使用默认值
        userDTOMapper.insertSelective(userDTO);
        return new User(
            userDTO.getId(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getPhone()
        );
    }

    @Override
    public User getUserById(Long id) {
        UserDTO userDTO = userDTOMapper.selectByPrimaryKey(id);
        if (userDTO == null) {
            return null;
        }
        return new User(
            userDTO.getId(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getPhone()
        );
    }

    @Override
    public User updateUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.id());
        userDTO.setUsername(user.username());
        userDTO.setEmail(user.email());
        userDTO.setPhone(user.phone());
        // status 字段不更新（保持原有值）
        // createdAt 和 updatedAt 字段不设置，让数据库处理
        
        int updated = userDTOMapper.updateByPrimaryKeySelective(userDTO);
        if (updated == 0) {
            return null;
        }
        // 查询更新后的记录
        UserDTO updatedUserDTO = userDTOMapper.selectByPrimaryKey(user.id());
        return new User(
            updatedUserDTO.getId(),
            updatedUserDTO.getUsername(),
            updatedUserDTO.getEmail(),
            updatedUserDTO.getPhone()
        );
    }

    @Override
    public boolean deleteUser(Long id) {
        return userDTOMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserDTO> userDTOs = userDTOMapper.selectByExample(null);
        return userDTOs.stream()
                .map(userDTO -> new User(
                    userDTO.getId(),
                    userDTO.getUsername(),
                    userDTO.getEmail(),
                    userDTO.getPhone()
                ))
                .collect(Collectors.toList());
    }

    // Product CRUD operations
    @Override
    public Product createProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.id());
        productDTO.setProductCode(product.productCode());
        productDTO.setProductName(product.productName());
        productDTO.setPrice(product.price());
        productDTO.setStock(product.stock());
        productDTO.setCategory(product.category());
        // 设置默认值
        productDTO.setIsDeleted((byte) 0);
        // 使用 insertSelective，不设置 createdAt 和 updatedAt，让数据库使用默认值
        productDTOMapper.insertSelective(productDTO);
        return new Product(
            productDTO.getId(),
            productDTO.getProductCode(),
            productDTO.getProductName(),
            productDTO.getPrice(),
            productDTO.getStock(),
            productDTO.getCategory()
        );
    }

    @Override
    public Product getProductById(Long id) {
        ProductDTO productDTO = productDTOMapper.selectByPrimaryKey(id);
        if (productDTO == null) {
            return null;
        }
        return new Product(
            productDTO.getId(),
            productDTO.getProductCode(),
            productDTO.getProductName(),
            productDTO.getPrice(),
            productDTO.getStock(),
            productDTO.getCategory()
        );
    }

    @Override
    public Product updateProduct(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.id());
        productDTO.setProductCode(product.productCode());
        productDTO.setProductName(product.productName());
        productDTO.setPrice(product.price());
        productDTO.setStock(product.stock());
        productDTO.setCategory(product.category());
        // isDeleted 字段不更新（保持原有值）
        // createdAt 和 updatedAt 字段不设置，让数据库处理
        
        int updated = productDTOMapper.updateByPrimaryKeySelective(productDTO);
        if (updated == 0) {
            return null;
        }
        // 查询更新后的记录
        ProductDTO updatedProductDTO = productDTOMapper.selectByPrimaryKey(product.id());
        return new Product(
            updatedProductDTO.getId(),
            updatedProductDTO.getProductCode(),
            updatedProductDTO.getProductName(),
            updatedProductDTO.getPrice(),
            updatedProductDTO.getStock(),
            updatedProductDTO.getCategory()
        );
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productDTOMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<Product> getAllProducts() {
        List<ProductDTO> productDTOs = productDTOMapper.selectByExample(null);
        return productDTOs.stream()
                .map(productDTO -> new Product(
                    productDTO.getId(),
                    productDTO.getProductCode(),
                    productDTO.getProductName(),
                    productDTO.getPrice(),
                    productDTO.getStock(),
                    productDTO.getCategory()
                ))
                .collect(Collectors.toList());
    }

    // Order CRUD operations
    @Override
    public Order createOrder(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.id());
        orderDTO.setOrderNo(order.orderNo());
        orderDTO.setUserId(order.userId());
        orderDTO.setAmount(order.amount());
        orderDTO.setStatus(order.status());
        orderDTO.setRemark(order.remark());
        // 使用 insertSelective，不设置 createdAt 和 updatedAt，让数据库使用默认值
        orderDTOMapper.insertSelective(orderDTO);
        return new Order(
            orderDTO.getId(),
            orderDTO.getOrderNo(),
            orderDTO.getUserId(),
            orderDTO.getAmount(),
            orderDTO.getStatus(),
            orderDTO.getRemark()
        );
    }

    @Override
    public Order getOrderById(Long id) {
        OrderDTO orderDTO = orderDTOMapper.selectByPrimaryKey(id);
        if (orderDTO == null) {
            return null;
        }
        return new Order(
            orderDTO.getId(),
            orderDTO.getOrderNo(),
            orderDTO.getUserId(),
            orderDTO.getAmount(),
            orderDTO.getStatus(),
            orderDTO.getRemark()
        );
    }

    @Override
    public Order updateOrder(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.id());
        orderDTO.setOrderNo(order.orderNo());
        orderDTO.setUserId(order.userId());
        orderDTO.setAmount(order.amount());
        orderDTO.setStatus(order.status());
        orderDTO.setRemark(order.remark());
        // createdAt 和 updatedAt 字段不设置，让数据库处理
        
        int updated = orderDTOMapper.updateByPrimaryKeySelective(orderDTO);
        if (updated == 0) {
            return null;
        }
        // 查询更新后的记录
        OrderDTO updatedOrderDTO = orderDTOMapper.selectByPrimaryKey(order.id());
        return new Order(
            updatedOrderDTO.getId(),
            updatedOrderDTO.getOrderNo(),
            updatedOrderDTO.getUserId(),
            updatedOrderDTO.getAmount(),
            updatedOrderDTO.getStatus(),
            updatedOrderDTO.getRemark()
        );
    }

    @Override
    public boolean deleteOrder(Long id) {
        return orderDTOMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<Order> getAllOrders() {
        List<OrderDTO> orderDTOs = orderDTOMapper.selectByExample(null);
        return orderDTOs.stream()
                .map(orderDTO -> new Order(
                    orderDTO.getId(),
                    orderDTO.getOrderNo(),
                    orderDTO.getUserId(),
                    orderDTO.getAmount(),
                    orderDTO.getStatus(),
                    orderDTO.getRemark()
                ))
                .collect(Collectors.toList());
    }
}