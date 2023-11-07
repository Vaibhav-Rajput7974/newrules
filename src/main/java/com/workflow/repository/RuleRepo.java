package com.workflow.repository;

import com.workflow.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepo extends JpaRepository<Rule , Long> {
}