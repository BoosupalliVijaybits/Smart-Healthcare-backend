package io.bvb.smarthealthcare.backend.model;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StringResponse implements Serializable {
    private String message;
}
