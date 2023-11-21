package com.workflow.repository;

import com.workflow.entity.Field;
import com.workflow.entity.Project;
import com.workflow.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepo extends JpaRepository<Rule , Long> {
    List<Rule> findByTriggerFieldAndProject(Field triggerField, Project project);

    List<Rule> findByTriggerField(Field triggerField);

    // Find rules by actionField
//    List<Rule> findByActionFieldAndProject(Field actionField);
//
//    // Find rules by both triggerField and actionField
//    List<Rule> findByTriggerFieldAndActionField(Field triggerField, Field actionField);

}