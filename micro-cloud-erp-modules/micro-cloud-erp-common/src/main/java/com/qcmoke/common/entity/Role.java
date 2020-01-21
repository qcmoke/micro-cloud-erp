package com.qcmoke.common.entity;

import java.io.Serializable;

public class Role implements Serializable {
    private Long id;
    private String name;
    private String nameZh;

    public Role() {
    }

    public Role(Long id, String name, String nameZh) {
        this.id = id;
        this.name = name;
        this.nameZh = nameZh;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
