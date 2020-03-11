package com.qcmoke.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author qcmoke
 */
@Data
public class CurrentUser implements Serializable {
    private Long userId;
    private String username;
    private Set<String> authorities;
}