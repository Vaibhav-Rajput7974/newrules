package com.workflow.repository;

import com.workflow.entity.Field;
import com.workflow.entity.Project;
import com.workflow.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepo extends JpaRepository<Rule , Long> {
//    List<Rule> findByTriggerFieldAndProject(Field triggerField, Project project);
//
//    List<Rule> findByTriggerField(Field triggerField);

    // Find rules by actionField
//    List<Rule> findByActionFieldAndProject(Field actionField);
//
//    // Find rules by both triggerField and actionField
//    List<Rule> findByTriggerFieldAndActionField(Field triggerField, Field actionField);

    @Query("SELECT r FROM Rule r JOIN r.triggerList t WHERE t.triggerField = :triggerField AND r.project.projectId = :projectId")
    List<Rule> findByTriggerFieldAndProjectId(@Param("triggerField") Field triggerField, @Param("projectId") Project project);

    List<Rule> findByTriggerList_TriggerFieldAndProject(Field triggerField, Project project);

    List<Rule> findByTriggerList_TriggerField(Field triggerField);


}