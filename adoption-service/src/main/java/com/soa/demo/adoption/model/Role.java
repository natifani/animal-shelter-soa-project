package com.soa.demo.adoption.model;

import com.soa.demo.adoption.util.RoleE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    protected Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private RoleE name;

    public Role(RoleE name) {
        this.name = name;
    }
}
