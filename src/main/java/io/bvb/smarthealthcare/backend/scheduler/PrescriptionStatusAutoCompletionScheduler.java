package io.bvb.smarthealthcare.backend.scheduler;

import io.bvb.smarthealthcare.backend.constant.PrescriptionStatus;
import io.bvb.smarthealthcare.backend.entity.Prescription;
import io.bvb.smarthealthcare.backend.repository.PrescriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PrescriptionStatusAutoCompletionScheduler {
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionStatusAutoCompletionScheduler(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Scheduled(cron = "0 59 23 * * *")
    public void markCompletedPrescriptions() {
        final List<Prescription> expired = prescriptionRepository.findByEndDateBeforeAndStatus(LocalDate.now(), PrescriptionStatus.IN_PROGRESS);

        for (Prescription prescription : expired) {
            prescription.setStatus(PrescriptionStatus.COMPLETED);
        }

        prescriptionRepository.saveAll(expired);
        System.out.println("Updated " + expired.size() + " prescriptions to EXPIRED at 23:59");
    }
}
