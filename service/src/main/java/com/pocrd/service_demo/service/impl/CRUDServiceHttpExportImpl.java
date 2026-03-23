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
        userDTO.setStatus(user.status());
        userDTO.setCreatedAt(user.createdAt());
        userDTO.setUpdatedAt(user.updatedAt());
        userDTOMapper.insert(userDTO);
        return new User(
            userDTO.getId(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getPhone(),
            userDTO.getStatus(),
            userDTO.getCreatedAt(),
            userDTO.getUpdatedAt()
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
            userDTO.getPhone(),
            userDTO.getStatus(),
            userDTO.getCreatedAt(),
            userDTO.getUpdatedAt()
        );
    }

    @Override
    public User updateUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.id());
        userDTO.setUsername(user.username());
        userDTO.setEmail(user.email());
        userDTO.setPhone(user.phone());
        userDTO.setStatus(user.status());
        userDTO.setCreatedAt(user.createdAt());
        userDTO.setUpdatedAt(user.updatedAt());
        int updated = userDTOMapper.updateByPrimaryKey(userDTO);
        if (updated == 0) {
            return null;
        }
        return new User(
            userDTO.getId(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getPhone(),
            userDTO.getStatus(),
            userDTO.getCreatedAt(),
            userDTO.getUpdatedAt()
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
                    userDTO.getPhone(),
                    userDTO.getStatus(),
                    userDTO.getCreatedAt(),
                    userDTO.getUpdatedAt()
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
        productDTO.setIsDeleted(product.isDeleted());
        productDTO.setCreatedAt(product.createdAt());
        productDTO.setUpdatedAt(product.updatedAt());
        productDTOMapper.insert(productDTO);
        return new Product(
            productDTO.getId(),
            productDTO.getProductCode(),
            productDTO.getProductName(),
            productDTO.getPrice(),
            productDTO.getStock(),
            productDTO.getCategory(),
            productDTO.getIsDeleted(),
            productDTO.getCreatedAt(),
            productDTO.getUpdatedAt()
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
            productDTO.getCategory(),
            productDTO.getIsDeleted(),
            productDTO.getCreatedAt(),
            productDTO.getUpdatedAt()
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
        productDTO.setIsDeleted(product.isDeleted());
        productDTO.setCreatedAt(product.createdAt());
        productDTO.setUpdatedAt(product.updatedAt());
        int updated = productDTOMapper.updateByPrimaryKey(productDTO);
        if (updated == 0) {
            return null;
        }
        return new Product(
            productDTO.getId(),
            productDTO.getProductCode(),
            productDTO.getProductName(),
            productDTO.getPrice(),
            productDTO.getStock(),
            productDTO.getCategory(),
            productDTO.getIsDeleted(),
            productDTO.getCreatedAt(),
            productDTO.getUpdatedAt()
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
                    productDTO.getCategory(),
                    productDTO.getIsDeleted(),
                    productDTO.getCreatedAt(),
                    productDTO.getUpdatedAt()
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
        orderDTO.setCreatedAt(order.createdAt());
        orderDTO.setUpdatedAt(order.updatedAt());
        orderDTOMapper.insert(orderDTO);
        return new Order(
            orderDTO.getId(),
            orderDTO.getOrderNo(),
            orderDTO.getUserId(),
            orderDTO.getAmount(),
            orderDTO.getStatus(),
            orderDTO.getRemark(),
            orderDTO.getCreatedAt(),
            orderDTO.getUpdatedAt()
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
            orderDTO.getRemark(),
            orderDTO.getCreatedAt(),
            orderDTO.getUpdatedAt()
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
        orderDTO.setCreatedAt(order.createdAt());
        orderDTO.setUpdatedAt(order.updatedAt());
        int updated = orderDTOMapper.updateByPrimaryKey(orderDTO);
        if (updated == 0) {
            return null;
        }
        return new Order(
            orderDTO.getId(),
            orderDTO.getOrderNo(),
            orderDTO.getUserId(),
            orderDTO.getAmount(),
            orderDTO.getStatus(),
            orderDTO.getRemark(),
            orderDTO.getCreatedAt(),
            orderDTO.getUpdatedAt()
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
                    orderDTO.getRemark(),
                    orderDTO.getCreatedAt(),
                    orderDTO.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }
}