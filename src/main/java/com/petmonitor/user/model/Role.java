package com.petmonitor.user.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode
@Getter
@Entity(name = "Role")
@Table(name = "role")
@NoArgsConstructor
public class Role implements GrantedAuthority, Serializable {

    public static final Role ADMIN = new Role(RoleName.ADMIN);
    public static final Role OWNER = new Role(RoleName.OWNER);

    @Id
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName roleName;

    private Role(RoleName roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return this.roleName.name();
    }

    public enum RoleName {
        ADMIN, OWNER
    }

}
