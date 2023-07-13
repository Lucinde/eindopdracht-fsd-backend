package com.lucinde.plannerpro.dtos;

import com.lucinde.plannerpro.models.Task;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class CustomerDto {
    public Long id;
    public String firstName;
    @NotBlank
    public String lastName;
    public String address;
    public String zip;
    @NotBlank
    public String city;
    public String phoneNumber;

    @NotBlank
    @Email
    public String email;

    public List<Task> taskList;
}
