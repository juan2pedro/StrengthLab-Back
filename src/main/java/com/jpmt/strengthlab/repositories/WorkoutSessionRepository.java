package com.jpmt.strengthlab.repositories;

import com.jpmt.strengthlab.models.domain.WorkoutSession;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    List<WorkoutSession> findByDateBetweenOrderByDateDesc(LocalDate from, LocalDate to);

    @EntityGraph(attributePaths = {
            "trainingSessionTemplate",
            "entries",
            "entries.exercise"
    })
    Optional<WorkoutSession>findHeaderById(Long id);

}
