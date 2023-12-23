package com.WorkBuddy.app.repository;

import com.WorkBuddy.app.model.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
}
