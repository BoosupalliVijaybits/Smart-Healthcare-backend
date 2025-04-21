package io.bvb.smarthealthcare.backend.model;

import io.bvb.smarthealthcare.backend.constant.Gender;
import io.bvb.smarthealthcare.backend.constant.LoginUserType;
import io.bvb.smarthealthcare.backend.entity.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {
    private Long id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String profileImageUrl;
    private LoginUserType userType;

    public static UserResponse mapUserToUserResponse(User user) {
        final UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setDateOfBirth(user.getDateOfBirth());
        userResponse.setGender(user.getGender());
        userResponse.setId(user.getId());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setUserType(LoginUserType.getUserType(user.getRole()));
        return userResponse;
    }
}
