package com.test.myproject.web.api_models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Long id;
    @NotEmpty(message = "Fullname is required")
    @Pattern(regexp = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}", message = "Fullname should only contain letters")
    private String fullname;
    @NotEmpty(message = "Email is required")
    @Email(message = "Email not valid")
    private String email;
    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&^_-]{8,}$", message = "At least 1 alphabet, 1 digit, contains no space, minimum 8 characters long")
    private String password;

    public String getFullname() {
        return this.fullname.trim();
    }
}
