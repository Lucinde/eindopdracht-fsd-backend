package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u JOIN u.authorities a WHERE a.authority = :authority")
    List<User> findByAuthority(String authority);
}
