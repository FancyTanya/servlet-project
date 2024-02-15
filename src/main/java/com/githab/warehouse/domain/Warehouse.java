package com.githab.warehouse.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    private int id;
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private int inventoryQuantity;

}
