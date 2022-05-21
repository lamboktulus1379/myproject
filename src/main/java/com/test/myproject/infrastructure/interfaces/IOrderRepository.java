package com.test.myproject.infrastructure.interfaces;

import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.Status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByStatus(Status status, Pageable pageable);
}
