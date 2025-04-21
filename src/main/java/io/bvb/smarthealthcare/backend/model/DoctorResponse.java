package io.bvb.smarthealthcare.backend.model;


import io.bvb.smarthealthcare.backend.constant.DoctorStatus;
import io.bvb.smarthealthcare.backend.constant.LoginUserType;
import io.bvb.smarthealthcare.backend.entity.Doctor;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoctorResponse extends UserResponse {
    private String clinicAddress;
    private String specialization;
    private String licenseNumber;
    private String clinicName;
    private Long experience;
    private String qualification;
    private DoctorStatus status;

    public static DoctorResponse convertDoctorToResponse(Doctor doctor) {
        DoctorResponse doctorResponse = new DoctorResponse();
        doctorResponse.setId(doctor.getId());
        doctorResponse.setEmail(doctor.getEmail());
        doctorResponse.setPhoneNumber(doctor.getPhoneNumber());
        doctorResponse.setFirstName(doctor.getFirstName());
        doctorResponse.setLastName(doctor.getLastName());
        doctorResponse.setGender(doctor.getGender());
        doctorResponse.setUserType(LoginUserType.getUserType(doctor.getRole()));
        doctorResponse.setDateOfBirth(doctor.getDateOfBirth());
        doctorResponse.setClinicName(doctor.getClinicName());
        doctorResponse.setStatus(doctor.getStatus());
        doctorResponse.setExperience(doctor.getExperience());
        doctorResponse.setSpecialization(doctor.getSpecialization());
        doctorResponse.setClinicAddress(doctor.getClinicAddress());
        doctorResponse.setQualification(doctor.getQualification());
        doctorResponse.setLicenseNumber(doctor.getLicenseNumber());
        return doctorResponse;
    }
}
