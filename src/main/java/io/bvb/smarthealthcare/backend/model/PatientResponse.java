package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.LoginUserType;
import io.bvb.smarthealthcare.backend.constant.MaritalStatus;
import io.bvb.smarthealthcare.backend.entity.Patient;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PatientResponse extends UserResponse {
    private String address;
    private MaritalStatus maritalStatus;
    private String preConditions;
    private String allergies;
    private String bloodGroup;
    private String emergencyNumber;
    private String emergencyName;

    public static PatientResponse convertPatientToPatientResponse(Patient patient) {
        final PatientResponse patientResponse = new PatientResponse();
        patientResponse.setId(patient.getId());
        patientResponse.setEmail(patient.getEmail());
        patientResponse.setPhoneNumber(patient.getPhoneNumber());
        patientResponse.setFirstName(patient.getFirstName());
        patientResponse.setLastName(patient.getLastName());
        patientResponse.setUserType(LoginUserType.PATIENT);
        patientResponse.setGender(patient.getGender());
        patientResponse.setDateOfBirth(patient.getDateOfBirth());
        patientResponse.setAddress(patient.getAddress());
        patientResponse.setMaritalStatus(patient.getMaritalStatus());
        patientResponse.setEmergencyNumber(patient.getEmergencyNumber());
        patientResponse.setEmergencyName(patient.getEmergencyName());
        patientResponse.setAllergies(patient.getAllergies());
        patientResponse.setBloodGroup(patient.getBloodGroup());
        patientResponse.setMaritalStatus(patient.getMaritalStatus());
        patientResponse.setPreConditions(patient.getPreConditions());
        return patientResponse;
    }

    public static List<PatientResponse> convertPatientsToPatientResponses(List<Patient> patients) {
        return patients.stream().map(PatientResponse::convertPatientToPatientResponse).collect(Collectors.toList());
    }
}
