package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
