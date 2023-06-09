package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByTask_Id(Long taskId);
}
