package com.jpmt.strengthlab.repositories;

import com.jpmt.strengthlab.models.domain.WorkoutEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutEntryRepository extends JpaRepository<WorkoutEntry, Long> {

}
