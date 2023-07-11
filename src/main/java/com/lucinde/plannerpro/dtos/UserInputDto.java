package com.lucinde.plannerpro.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lucinde.plannerpro.models.Authority;
import com.lucinde.plannerpro.models.ScheduleTask;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Set;

public class UserInputDto {
    @NotBlank
    public String username;

    @NotBlank
    @Size(min = 6, message = "Het wachtwoord moet tussen de 6 en 25 tekens lang zijn")
    @Size(max = 25, message = "Het wachtwoord moet tussen de 6 en 25 tekens lang zijn")
    public String password;
    public Boolean enabled;
    public String apikey;
    @NotBlank
    @Email
    public String email;
    @JsonSerialize
    public Set<Authority> authorities;

    public List<ScheduleTask> scheduleTask;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getApikey() {
        return apikey;
    }

    public String getEmail() {
        return email;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<ScheduleTask> getScheduleTask() {
        return scheduleTask;
    }

    public void setScheduleTask(List<ScheduleTask> scheduleTask) {
        this.scheduleTask = scheduleTask;
    }
}
