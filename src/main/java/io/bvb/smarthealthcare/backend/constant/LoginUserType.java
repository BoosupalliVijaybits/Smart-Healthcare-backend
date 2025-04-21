package io.bvb.smarthealthcare.backend.constant;

import io.bvb.smarthealthcare.backend.entity.Role;

public enum LoginUserType {
    PATIENT, DOCTOR, ADMIN;

    public static LoginUserType getUserType(Role role) {
        return switch (role) {
            case ADMIN -> LoginUserType.ADMIN;
            case PATIENT -> LoginUserType.PATIENT;
            case DOCTOR -> LoginUserType.DOCTOR;
        };
    }
}
