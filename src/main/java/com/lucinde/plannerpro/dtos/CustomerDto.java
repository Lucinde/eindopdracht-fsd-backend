package com.lucinde.plannerpro.dtos;

import jakarta.validation.constraints.NotBlank;

public class CustomerDto {
    public Long id;
    public String firstName;
    @NotBlank
    public String lastName;
    public String address;
    public String zip;
    public String city;
    public String phoneNumber;
    public String email;
}
