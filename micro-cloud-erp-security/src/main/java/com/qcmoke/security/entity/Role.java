package com.qcmoke.security.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Role implements Serializable {
    private Long id;
    private String name;
    private String nameZh;
}
