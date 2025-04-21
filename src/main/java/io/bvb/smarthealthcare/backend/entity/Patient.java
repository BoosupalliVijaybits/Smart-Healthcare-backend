package io.bvb.smarthealthcare.backend.entity;

import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User {
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "maritalStatus", nullable = false)
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column
    private String preConditions;

    @Column
    private String allergies;

    @Column(name = "bloodGroup", nullable = false)
    private String bloodGroup;

    @Column(name = "emergencyContactNumber", nullable = false)
    private String emergencyNumber;

    @Column(name = "emergencyContactName", nullable = false)
    private String emergencyName;
}
