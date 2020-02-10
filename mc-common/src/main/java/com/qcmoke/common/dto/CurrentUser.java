package com.qcmoke.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author qcmoke
 */
@Data
public class CurrentUser implements Serializable {
    private Long uid;
    private String username;
    private Set<String> authorities;
}