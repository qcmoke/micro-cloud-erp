package com.qcmoke.auth.common.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * @author qcmoke
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class AuthUser extends User {
    private Long uid;
    private Integer status;

    public AuthUser(String username, String password, Long uid, Integer status, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.uid = uid;
        this.status = status;
    }
}
