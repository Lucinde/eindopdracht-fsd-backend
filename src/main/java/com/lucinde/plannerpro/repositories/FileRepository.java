package com.lucinde.plannerpro.repositories;

import com.lucinde.plannerpro.models.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
