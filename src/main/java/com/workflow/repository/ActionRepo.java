package com.workflow.repository;

import com.workflow.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepo extends JpaRepository<Action,Long> {

}
