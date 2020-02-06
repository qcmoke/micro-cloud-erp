package com.qcmoke.oms.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto implements Serializable {
    private Integer id;
    private String name;
}
