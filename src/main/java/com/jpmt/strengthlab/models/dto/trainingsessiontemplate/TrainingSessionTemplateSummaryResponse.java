package com.jpmt.strengthlab.models.dto.trainingsessiontemplate;

public record TrainingSessionTemplateSummaryResponse(
        Long id,
        String blockName,
        Integer weekNumber,
        Integer dayInWeek
) {
}

