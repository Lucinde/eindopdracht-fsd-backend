package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lucinde.plannerpro.models.Authority;
import com.lucinde.plannerpro.models.ScheduleTask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public class UserOutputDto {
    public String username;
    public Boolean enabled;
    public String apikey;
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;
    public List<ScheduleTask> scheduleTask;
}
