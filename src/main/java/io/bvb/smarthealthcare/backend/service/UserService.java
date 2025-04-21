package io.bvb.smarthealthcare.backend.service;

import io.bvb.smarthealthcare.backend.entity.Doctor;
import io.bvb.smarthealthcare.backend.entity.Patient;
import io.bvb.smarthealthcare.backend.entity.User;
import io.bvb.smarthealthcare.backend.exception.AlreadyRegisteredException;
import io.bvb.smarthealthcare.backend.exception.InvalidDataException;
import io.bvb.smarthealthcare.backend.model.*;
import io.bvb.smarthealthcare.backend.repository.DoctorRepository;
import io.bvb.smarthealthcare.backend.repository.PatientRepository;
import io.bvb.smarthealthcare.backend.repository.UserRepository;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public UserService(PatientRepository patientRepository, DoctorRepository doctorRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    public void updatePatient(final PutPatientRequest putPatientRequest) {
        final UserResponse userResponse = CurrentUserData.getUser();
        final User user = getUser(userResponse.getEmail());
        validateSelfEdit(userResponse, user);

        final Patient patient = patientRepository.findById(user.getId()).get();
        patient.setAddress(putPatientRequest.getAddress());
        patient.setMaritalStatus(putPatientRequest.getMaritalStatus());
        patient.setPreConditions(putPatientRequest.getPreConditions());
        patient.setAllergies(putPatientRequest.getAllergies());
        patient.setBloodGroup(putPatientRequest.getBloodGroup());
        patient.setEmergencyNumber(putPatientRequest.getEmergencyNumber());
        patient.setEmergencyName(putPatientRequest.getEmergencyName());

        final Patient updatedPatient = patientRepository.saveAndFlush(patient);
        LOGGER.info("Patient is updated successfully :: Patient Id : {}, Email : {}", updatedPatient.getId(), updatedPatient.getEmail());
        CurrentUserData.setUser(PatientResponse.convertPatientToPatientResponse(updatedPatient));
    }

    public void updateAdmin(final PutAdminRequest putAdminRequest) {
        final UserResponse userResponse = CurrentUserData.getUser();
        final User user = getUser(userResponse.getEmail());
        validateSelfEdit(userResponse, user);
        validateAdminPhoneNumber(user, putAdminRequest);
        user.setPassword(putAdminRequest.getPassword());
        user.setPhoneNumber(putAdminRequest.getPhoneNumber());
        userRepository.saveAndFlush(user);
        CurrentUserData.setUser(UserResponse.mapUserToUserResponse(user));
        LOGGER.info("User is updated successfully :: User Id : {}, Email : {}", user.getId(), user.getEmail());
    }

    public void updateDoctor(final PutDoctorRequest putDoctorRequest) {
        final UserResponse userResponse = CurrentUserData.getUser();
        final User user = getUser(userResponse.getEmail());
        final Doctor doctor = doctorRepository.findById(user.getId()).get();
        validateSelfEdit(userResponse, user);
        doctor.setClinicName(putDoctorRequest.getClinicName());
        doctor.setClinicAddress(putDoctorRequest.getClinicAddress());
        doctor.setSpecialization(putDoctorRequest.getSpecialization());
        doctor.setExperience(putDoctorRequest.getExperience());
        doctor.setQualification(putDoctorRequest.getQualification());
        final Doctor updatedDoctor = doctorRepository.saveAndFlush(doctor);
        LOGGER.info("Doctor is updated successfully :: Doctor Id : {}, Email : {}", updatedDoctor.getId(), updatedDoctor.getEmail());
        CurrentUserData.setUser(DoctorResponse.convertDoctorToResponse(updatedDoctor));
    }

    private User getUser(final String username) {
        return userRepository.findByEmailOrPhoneNumber(username, username).orElseThrow(() -> new UsernameNotFoundException("User not found with email/phone: " + username));
    }

    private void validateSelfEdit(final UserResponse userResponse, final User user){
        if (userResponse.getId() != user.getId()) {
            LOGGER.error("Your trying to update other user data!!!!");
            throw new InvalidDataException("Your trying to update other user data!!!!");
        }
    }

    private void validateAdminPhoneNumber(final User user, final PutAdminRequest putAdminRequest) {
        if (!user.getPhoneNumber().equals(putAdminRequest.getPhoneNumber())) {
            final Optional<User> patientUser = userRepository.findByPhoneNumber(putAdminRequest.getPhoneNumber());
            if (patientUser.isPresent() && !Objects.equals(patientUser.get().getId(), user.getId())) {
                LOGGER.error("Phone Number is already registered :: Phone Number : {}", putAdminRequest.getPhoneNumber());
                throw new AlreadyRegisteredException("PhoneNumber", putAdminRequest.getPhoneNumber());
            }
        }
    }
}