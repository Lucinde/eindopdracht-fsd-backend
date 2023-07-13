package com.lucinde.plannerpro.config;

import com.lucinde.plannerpro.filters.JwtRequestFilter;
import com.lucinde.plannerpro.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    public final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;

    public SpringSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
    }


    // Authenticatie met customUserDetailsService en passwordEncoder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    // Authorizatie met jwt
    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        //JWT token authentication
        http
                .csrf().disable()
                .httpBasic().disable()
                .cors().and()
                .authorizeHttpRequests()
                // --------------------------- USERS ---------------------------
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/users/{username}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/users/mechanics").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.GET, "/users/auth/{username}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")

                // ------------------------- CUSTOMERS -------------------------
                .requestMatchers(HttpMethod.GET, "/customers").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/customers/{id}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/customers/pages").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.POST, "/customers").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.PUT, "/customers/{id}").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.DELETE, "/customers/{id}").hasAnyRole("ADMIN", "PLANNER")

                // --------------------------- TASKS ---------------------------
                .requestMatchers(HttpMethod.GET, "/tasks").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/tasks/{id}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/tasks/pages").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.GET, "/tasks/pages/{mechanic}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.POST, "/tasks").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.PUT, "/tasks/{id}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.DELETE, "/tasks/{id}").hasAnyRole("ADMIN", "PLANNER")

                // ----------------------- SCHEDULE TASKS -----------------------
                .requestMatchers(HttpMethod.GET, "/schedule-tasks").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/schedule-tasks/{id}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.GET, "/schedule-tasks/pages/{mechanic}").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.POST, "/schedule-tasks").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.PUT, "/schedule-tasks/{id}").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.PUT, "/schedule-tasks/{id}/task/{task_id}").hasAnyRole("ADMIN", "PLANNER")
                .requestMatchers(HttpMethod.DELETE, "/schedule-tasks/{id}").hasAnyRole("ADMIN", "PLANNER")

                // --------------------------- FILES ---------------------------
                .requestMatchers(HttpMethod.GET, "/files/**").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.POST, "/files/**").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.PUT, "/files/**").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")
                .requestMatchers(HttpMethod.DELETE, "/files/**").hasAnyRole("ADMIN", "PLANNER", "MECHANIC")

                // ----------------------- AUTHENTICATION ----------------------
                .requestMatchers("/authenticated").authenticated()
                .requestMatchers("/authenticate").permitAll()/*alleen dit punt mag toegankelijk zijn voor niet ingelogde gebruikers*/

                .anyRequest().denyAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}