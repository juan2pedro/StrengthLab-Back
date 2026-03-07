package com.jpmt.strengthlab.models.dto.trainingsettemplate;

import com.jpmt.strengthlab.models.domain.IntensityType;

public record TrainingSetTemplateResponse(
        Long id,
        Integer targetSets,
        Integer targetReps,
        Double targetWeight,
        String targetIntensity,
        IntensityType targetIntensityType,
        String rest,
        String notes,
        Integer displayOrder,
        Long exerciseId
) {
}
