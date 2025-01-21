package com.apollo.scentraapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderDetail {
    @Id
    private Long order_sub_id;
    private Long product_id;
    private Long order_id;
    private Integer quantity;
}
