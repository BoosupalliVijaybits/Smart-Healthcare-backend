package io.bvb.smarthealthcare.backend.repository;

import io.bvb.smarthealthcare.backend.entity.ReminderTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderTrackerRepository extends JpaRepository<ReminderTracker, String> {
    boolean existsByAppointmentId(String appointmentId);
}

