package com.ecommerce.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.websocket.OnOpen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;


    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 5 characters")
    private  String street;


    @Size(min = 5, message = "Building name must be  atleast 5 characters")
    private  String buildingName;

    @Size(min = 4, message = "City name must be  atleast 4 characters")
    private  String cityName;


    @Size(min = 2, message = "State name must be  atleast 2 characters")
    private  String stateName;

    @Size(min = 2, message = "Country name must be  atleast 2 characters")
    private  String countryName;

    @Size(min = 6, message = "Pincode must be  atleast 6 characters")
    private  String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> user = new ArrayList<>();

    public Address(String street, String cityName, String buildingName, String stateName, String countryName, String pincode) {
        this.street = street;
        this.cityName = cityName;
        this.buildingName = buildingName;
        this.stateName = stateName;
        this.countryName = countryName;
        this.pincode = pincode;
    }
}
