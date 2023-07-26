package com.example.taskforwork.entities;

import com.example.taskforwork.enumTypes.Label;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class PhoneNumber {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String phoneNumber;

    private String country;

    @Enumerated(EnumType.STRING)
    private Label label; // Enum for mobile, work, home

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PhoneNumber() {
    }

    public PhoneNumber(String phoneNumber, String country, Label label, User user) {
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.label = label;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(id, that.id) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(country, that.country) && Objects.equals(label, that.label) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneNumber, country, label, user);
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", label=" + label +
                ", user=" + user +
                '}';
    }
}
