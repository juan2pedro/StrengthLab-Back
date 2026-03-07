package com.jpmt.strengthlab.models.dto.workoutset;

import com.jpmt.strengthlab.models.domain.IntensityType;

public record WorkoutSetRequest(
        Integer setNumber,
        Integer reps,
        Double weight,
        IntensityType intensityType,
        Integer intensityValue
) {
}