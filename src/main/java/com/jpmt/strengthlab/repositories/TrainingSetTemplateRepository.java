package com.jpmt.strengthlab.repositories;

import com.jpmt.strengthlab.models.domain.TrainingSetTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingSetTemplateRepository extends JpaRepository<TrainingSetTemplate, Long> {
    List<TrainingSetTemplate> findBySessionTemplateId(Long sessionTemplateId);

    TrainingSetTemplate save(TrainingSetTemplate trainingSetTemplate);

    @Query("""
                SELECT s FROM TrainingSetTemplate s
                JOIN FETCH s.exercise
                WHERE s.sessionTemplate.id = :sessionId
            """)
    Optional<TrainingSetTemplate> findBySessionIdWithExercise(Long sessionId);

}
