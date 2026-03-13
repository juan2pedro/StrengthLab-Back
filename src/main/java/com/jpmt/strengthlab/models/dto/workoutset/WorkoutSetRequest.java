package com.jpmt.strengthlab.models.dto.workoutset;

import com.jpmt.strengthlab.models.domain.IntensityType;

public record WorkoutSetRequest(
        Integer sequenceNumber,
        Integer reps,
        Double weight,
        IntensityType intensityType,
        Integer intensityValue
) {
}