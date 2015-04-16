package com.marcelotozzi.address.api.business.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by marcelotozzi on 14/04/15.
 */
@Entity
@Table(name = "ADDRESS")
public class Address implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @NotNull
    @Column(name = "street")
    private String street;

    @NotEmpty
    @NotNull
    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;

    @NotEmpty
    @NotNull
    @Column(name = "zipcode")
    private String zipCode;

    @Column(name = "district")
    private String district;

    @NotEmpty
    @NotNull
    @Column(name = "city")
    private String city;

    @NotEmpty
    @NotNull
    @Column(name = "state")
    private String state;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Address() {
    }

    public Address(String street, String number, String zipCode, String city, String state, User user) {
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
        this.state = state;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
