package com.test.myproject.core.services;

import java.util.List;

import com.test.myproject.core.interfaces.IOrderItemService;
import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.OrderItem;
import com.test.myproject.infrastructure.interfaces.IOrderItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderItemService implements IOrderItemService {

    @Autowired
    private IOrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> allByOrder(Order order) {
        log.info("Return List of orders ", order.getUser().getEmail());
        return orderItemRepository.findByOrder(order);
    }
}
