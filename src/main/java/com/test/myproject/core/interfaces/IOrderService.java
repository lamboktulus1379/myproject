package com.test.myproject.core.interfaces;

import java.util.List;

import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.OrderItem;
import com.test.myproject.core.order_aggregate.Status;
import com.test.myproject.core.user_aggreate.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    public Page<Order> all(Pageable pageable);

    public Order save(List<OrderItem> orderItems, User user);

    public Order one(Long id);

    public Order updateStatus(Status status, Long id);

    public Page<Order> allByStatus(Status status, Pageable pageable);

    public Order findAndUpdateOrder(Order order, Long id);
}
