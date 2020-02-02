package com.qcmoke.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto implements Serializable {
    private Integer id;
    private String name;
}
