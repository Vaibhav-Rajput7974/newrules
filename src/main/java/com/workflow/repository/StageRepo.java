package com.workflow.repository;

import com.workflow.entity.Stage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StageRepo extends JpaRepository<Stage , Long> {
}