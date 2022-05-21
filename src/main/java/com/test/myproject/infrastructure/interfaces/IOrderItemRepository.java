package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
