package com.jpmt.strengthlab.services;

import com.jpmt.strengthlab.models.domain.MainPattern;
import com.jpmt.strengthlab.models.dto.exercise.ExerciseRequest;
import com.jpmt.strengthlab.models.dto.exercise.ExerciseResponse;

import java.util.List;

public interface ExerciseService {
    List<ExerciseResponse> findAll();

    List<ExerciseResponse> findAllByMainPattern(MainPattern mainPattern);

    ExerciseResponse findById(Long id);

    ExerciseResponse save(ExerciseRequest exercise);

    ExerciseResponse update(Long id, ExerciseRequest exercise);

    void deleteById(Long id);
}
