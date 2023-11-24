package com.workflow.repository;

import com.workflow.entity.Field;
import com.workflow.entity.Rule;
import com.workflow.entity.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriggerRepo extends JpaRepository<Trigger,Long> {
    public List<Trigger> findByTriggerField(Field triggerField);
}
