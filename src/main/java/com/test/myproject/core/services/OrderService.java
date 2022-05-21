package com.test.myproject.core.services;

import java.util.Date;
import java.util.List;

import com.test.myproject.core.interfaces.IOrderService;
import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.OrderItem;
import com.test.myproject.core.order_aggregate.Status;
import com.test.myproject.core.order_aggregate.exception.OrderNotFoundException;
import com.test.myproject.core.product_aggreate.Product;
import com.test.myproject.core.product_aggreate.exception.ProductNotFoundException;
import com.test.myproject.core.product_aggreate.exception.ProductOutOfStockException;
import com.test.myproject.core.user_aggreate.User;
import com.test.myproject.core.user_aggreate.exception.UserNotFoundException;
import com.test.myproject.infrastructure.interfaces.IOrderItemRepository;
import com.test.myproject.infrastructure.interfaces.IOrderRepository;
import com.test.myproject.infrastructure.interfaces.IProductRepository;
import com.test.myproject.infrastructure.interfaces.IUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService implements IOrderService {

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Page<Order> all(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order one(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public Page<Order> allByStatus(Status status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable);
    }

    @Override
    @Transactional
    public Order save(List<OrderItem> orderItems, User userRequest) {
        log.info("Start Order");
        User user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException(userRequest.getEmail());
        }
        log.info("User ", user.getEmail());

        Order orderRequest = new Order();
        orderRequest.setUser(user);
        orderRequest.setStatus(Status.FAILED.name());
        orderRequest.setCreatedAt(new Date());
        orderRequest.setAmount(0.0);

        Order orderSaved = orderRepository.save(orderRequest);
        log.info("Start ordering User " + user.getEmail());
        log.info("==============================================================");
        for (OrderItem orderItem : orderItems) {
            // Product
            Product product = productRepository.getById(orderItem.getProduct().getId());
            if (product == null) {
                throw new ProductNotFoundException(orderItem.getProduct().getId());
            }
            if (product.getStock() - orderItem.getQuantity() < 0) {
                throw new ProductOutOfStockException(product.getId());
            }

            OrderItem orderItemSaved = orderItem;
            orderItemSaved.setOrder(orderSaved);
            orderItemSaved.setProduct(product);

            Double orderItemAmount = orderItem.getAmount() + (product.getPrice() *
                    orderItem.getQuantity());
            log.info("Order Item Amount => {} * {} = {}", product.getPrice(),
                    orderItem.getQuantity(), orderItemAmount);
            orderItemSaved.setAmount(orderItemAmount);
            orderItemRepository.save(orderItemSaved);

            Double orderAmount = orderRequest.getAmount() + orderItemAmount;
            log.info("====================================== Order Amount => {} + {} = {}", orderRequest.getAmount(),
                    orderItemAmount, orderAmount);
            orderRequest.setAmount(orderAmount);

            reductProduct(product, orderItem.getQuantity());
        }

        log.info("====================================== Total Amount => {}", orderRequest.getAmount());
        orderSaved = this.findAndUpdateOrder(orderRequest, orderSaved.getId());
        return orderSaved;
    }

    private void reductProduct(Product product, Integer quantity) {
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    @Override
    public Order updateStatus(Status status, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order findAndUpdateOrder(Order newOrder, Long id) {
        return orderRepository.findById(id).map(order -> {
            order.setAmount(newOrder.getAmount());
            order.setStatus(Status.PENDING.name());
            return orderRepository.save(order);
        }).orElseGet(() -> {
            newOrder.setId(id);
            return orderRepository.save(newOrder);
        });
    }

}
