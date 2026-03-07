package com.jpmt.strengthlab.models.mappers;

import com.jpmt.strengthlab.models.domain.TrainingSessionTemplate;
import com.jpmt.strengthlab.models.dto.trainingsessiontemplate.TrainingSessionTemplateDetailResponse;
import com.jpmt.strengthlab.models.dto.trainingsessiontemplate.TrainingSessionTemplateRequest;
import com.jpmt.strengthlab.models.dto.trainingsessiontemplate.TrainingSessionTemplateSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = TrainingSetTemplateMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingSessionTemplateMapper {
    @Mapping(source = "conjugatedDayType", target = "conjugatedDayType")
    TrainingSessionTemplateDetailResponse toDetailResponse(TrainingSessionTemplate entity);

    TrainingSessionTemplateSummaryResponse toSummaryResponse(TrainingSessionTemplate entity);

    @Mapping(target = "id", ignore = true)
    TrainingSessionTemplate toCreateEntity(TrainingSessionTemplateRequest dto);

    @Mapping(target = "id", ignore = true)
    TrainingSessionTemplate toEntity(TrainingSessionTemplateRequest dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(
            TrainingSessionTemplateRequest request,
            @MappingTarget TrainingSessionTemplate entity
    );
}
