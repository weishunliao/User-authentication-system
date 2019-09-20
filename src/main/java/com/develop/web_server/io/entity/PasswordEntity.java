package com.develop.web_server.io.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class PasswordEntity implements Serializable {

    private static final long serialVersionUID = 3508240497217879955L;


    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    @JoinColumn(name="users_id")
    private UserEntity userDetails;

    @Column(nullable = false)
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
