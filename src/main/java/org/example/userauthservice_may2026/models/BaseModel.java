package org.example.userauthservice_may2026.models;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import javax.management.MXBean;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private Date lastUpdatedAt;

    private State state;

    public BaseModel(){
        this.createdAt=new Date();
        this.lastUpdatedAt=new Date();
        this.state=State.ACTIVE;
    }

}
