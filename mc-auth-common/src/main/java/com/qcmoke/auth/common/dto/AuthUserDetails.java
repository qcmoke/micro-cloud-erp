package com.qcmoke.auth.common.dto;

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
public class AuthUserDetails extends User {
    private Long userId;
    private Integer status;

    public AuthUserDetails(String username, String password, Long userId, Integer status, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.status = status;
    }
}
