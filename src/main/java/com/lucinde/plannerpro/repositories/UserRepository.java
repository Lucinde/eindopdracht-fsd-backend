package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
