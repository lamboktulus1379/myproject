package com.test.myproject.web.api_models;

import java.util.Collection;

import com.test.myproject.core.order_aggregate.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CreateOrderDTO {
    private Long id;
    private Collection<OrderItem> orderItems;
}
