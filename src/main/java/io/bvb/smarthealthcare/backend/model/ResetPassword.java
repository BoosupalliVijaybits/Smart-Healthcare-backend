package io.bvb.smarthealthcare.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPassword implements Serializable {
    @NotBlank(message = "Email/Phone number is required")
    private String email;
}
