package com.jpmt.strengthlab.repositories;

import com.jpmt.strengthlab.models.domain.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    List<WorkoutSet> findByWorkoutEntryId(Long id);
    List<WorkoutSet> findByWorkoutEntryIdIn(Collection<Long> entryIds);

}
