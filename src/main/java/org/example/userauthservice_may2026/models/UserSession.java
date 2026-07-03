package org.example.userauthservice_may2026.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserSession extends BaseModel{

    private String token;
    @ManyToOne
    private User user;
}
