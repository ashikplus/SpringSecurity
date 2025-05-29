package com.example.SpringSecurity.model;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    public void setname(String name) {
		this.name = name;
	}

    @Override
    public String getAuthority() {
        return name;
    }
}
