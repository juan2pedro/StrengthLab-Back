package com.jpmt.strengthlab.models.dto.trainingsessiontemplate;

import com.jpmt.strengthlab.models.domain.ConjugatedDayType;
import com.jpmt.strengthlab.models.dto.trainingsettemplate.TrainingSetTemplateResponse;

import java.util.List;


public record TrainingSessionTemplateDetailResponse(
        Long id,
        String blockName,
        Integer weekNumber,
        Integer dayInWeek,
        ConjugatedDayType conjugatedDayType,
        String notes,
        List<TrainingSetTemplateResponse> setTemplates
) {

}
