package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.models.User;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleTaskDto {
    public Long id;
    @FutureOrPresent
    public LocalDate date;
    public LocalTime startTime;
    public LocalTime endTime;

    public Task task;

    public User mechanic;

    // todo: Dit voor de output DTO gebruiken en voor de input wel de oude versie houden
//    @JsonIgnore
//    public User mechanic;
//
//    @JsonProperty("mechanic")
//    public String getMechanicUsername() {
//        return mechanic.getUsername();
//    }
}
