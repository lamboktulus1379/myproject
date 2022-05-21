package com.test.myproject.web.api_models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.test.myproject.core.order_aggregate.OrderItem;
import com.test.myproject.core.order_aggregate.Status;
import com.test.myproject.core.user_aggreate.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;

    private Collection<OrderItemDTO> orderItems = new ArrayList<>();

    private Status status;

    private UserDTO user;

    private Double amount = 0.0;

    private Date createdAt;

    private Date updatedAt;
}
