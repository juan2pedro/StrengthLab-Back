package com.jpmt.strengthlab.repositories;

import com.jpmt.strengthlab.models.domain.Exercise;
import com.jpmt.strengthlab.models.domain.MainPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMainPattern(MainPattern mainPattern);
}
