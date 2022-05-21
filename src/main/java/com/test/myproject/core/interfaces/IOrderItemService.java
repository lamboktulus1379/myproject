package com.test.myproject.core.interfaces;

import java.util.List;

import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.OrderItem;

public interface IOrderItemService {
    public List<OrderItem> allByOrder(Order order);
}
