package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lucinde.plannerpro.models.Authority;
import com.lucinde.plannerpro.models.ScheduleTask;

import java.util.List;
import java.util.Set;

public class UserOutputDto {
    public String username;
    public Boolean enabled;
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;
    public List<ScheduleTask> scheduleTask;
}
