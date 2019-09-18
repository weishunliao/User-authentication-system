package com.develop.web_server.io.entity;

import com.develop.web_server.shared.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 51068186706127693L;


    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 30)
    private String addressId;

    @Column(nullable = false, length = 20)
    private String city;

    @Column(nullable = false, length = 20)
    private String country;

    @Column(nullable = false, length = 50)
    private String streetName;

    @Column(nullable = false, length = 10)
    private String postCode;

    @Column(nullable = false, length = 10)
    private String type;

    @ManyToOne // one person could have many addresses
    @JoinColumn(name="users_id") //database id, not userID
    private UserEntity userDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id=" + id +
                ", addressId='" + addressId + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", streetName='" + streetName + '\'' +
                ", postCode='" + postCode + '\'' +
                ", type='" + type + '\'' +
                ", userDetails=" + userDetails +
                '}';
    }
}
