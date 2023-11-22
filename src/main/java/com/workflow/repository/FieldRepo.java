package com.workflow.repository;

import com.workflow.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FieldRepo extends JpaRepository<Field,Long> {
    public Field findByName(String name);

    public List<Field> findByDataType(String dataType);
}
