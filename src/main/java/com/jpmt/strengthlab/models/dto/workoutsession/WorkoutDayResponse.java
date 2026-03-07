package com.jpmt.strengthlab.models.dto.workoutsession;

import java.time.LocalDate;
import java.util.List;

public record WorkoutDayResponse(
        Long workoutId,
        LocalDate date,
        TemplateInfo template,
        List<Entry> entries
) {
    public record TemplateInfo(
            Long id,
            String blockName,
            Integer weekNumber,
            Integer dayInWeek,
            String conjugateDayType,
            String notes
    ) {}

    public record Entry(
            Long entryId,
            ExerciseInfo exercise,
            Target target,          // opcional: si viene de template
            String notes,
            Boolean isWarmup,
            List<Set> sets
    ) {}

    public record ExerciseInfo(
            Long id,
            String baseName,
            String mainPattern,
            String implement,
            String setup,
            String stance,
            String grip
    ) {}

    public record Target(
            Integer targetSets,
            Integer targetReps,
            Double targetWeight,
            String targetIntensityType,   // RIR/RPE/PERCENTAGE
            String targetIntensityValue,  // "8" o "2" o "75" (simple)
            String rest
    ) {}

    public record Set(
            Long id,
            Integer setNumber,
            Integer reps,
            Double weight,
            String intensityType,   // RIR/RPE/PERCENTAGE
            Double intensityValue,
            String createdAt,
            String updatedAt
    ) {}
}