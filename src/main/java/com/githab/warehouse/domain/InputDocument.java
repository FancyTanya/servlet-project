package com.githab.warehouse.domain;

import lombok.Data;

import java.util.Date;

@Data
public class InputDocument {

    private int id;
    private String name;
    private Date createdAt;
    private byte[] fileData;

}
