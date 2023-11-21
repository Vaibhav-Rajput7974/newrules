package com.workflow.repository;

import com.workflow.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepo extends JpaRepository<Field,Long> {
    public Field findByName(String name);

    public  Field findByDataType(String dataType);
}
