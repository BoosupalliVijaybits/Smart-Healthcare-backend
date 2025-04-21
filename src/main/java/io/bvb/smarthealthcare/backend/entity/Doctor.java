package io.bvb.smarthealthcare.backend.entity;

import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends User {
    @Column(name = "clinicAddress", nullable = false)
    private String clinicAddress;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "licensenumber", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "clinicname", nullable = false)
    private String clinicName;

    @Column(name = "experience", nullable = false)
    private Long experience;

    @Column(name = "qualification", nullable = false)
    private String qualification;

    @Enumerated(EnumType.STRING)
    private DoctorStatus status = DoctorStatus.PENDING;
}
