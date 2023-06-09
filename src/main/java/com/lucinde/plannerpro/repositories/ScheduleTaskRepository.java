package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.ScheduleTask;
import com.lucinde.plannerpro.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long> {

    @Query("SELECT COUNT(st) FROM ScheduleTask st WHERE st.mechanic = :mechanic AND st.id <> :id AND st.date = :date " +
            "AND ((st.startTime <= :endTime AND st.endTime >= :startTime) OR (st.startTime >= :startTime AND st.endTime <= :endTime) OR (st.startTime <= :startTime AND st.endTime >= :endTime))")
    Long countConflictingTasks(User mechanic, LocalDate date, LocalTime startTime, LocalTime endTime, Long id);

    Page<ScheduleTask> findByMechanicUsername(String mechanicUsername, Pageable pageable);
    Page<ScheduleTask> findByMechanicUsernameAndDateAfter(String mechanicUsername, LocalDate currentDate, Pageable pageable);

    Page<ScheduleTask> findAllByDateAfter(LocalDate currentDate, Pageable pageable);

//    Deze query werkt wel in PG Admin maar niet in springboot
//    @Query("SELECT COUNT(st) FROM ScheduleTask st WHERE st.mechanic = :mechanic AND st.date = :date " +
//            "AND st.startTime <= :endTime AND st.endTime >= :startTime")
//    Long isMechanicConflict(User mechanic, LocalDate date, LocalTime endTime, LocalTime startTime);

}
