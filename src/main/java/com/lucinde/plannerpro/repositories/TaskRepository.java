package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.ScheduleTask;
import com.lucinde.plannerpro.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
