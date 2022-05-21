package com.test.myproject.web.api_models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;

    private Integer quantity;

    private Double amount = 0.0;

    private ProductOrderItemDTO product;
}
