package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long> {
}
