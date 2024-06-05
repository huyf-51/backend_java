package com.nam.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupEmployeeRequest {
    private String firstName;
    private String lastName;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private String password;

    private double monthSalary;
}