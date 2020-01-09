package com.moodle.analytics.form;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class UserForm {

    @NotBlank(message = "{username.empty}")
    private String username;

    @NotBlank(message = "{password.empty}")
    private String password;

    @NotBlank(message = "{first.name.empty}")
    private String firstName;

    @NotBlank(message = "{last.name.empty}")
    private String lastName;

    @Email(message = "{email.empty}")
    @NotBlank(message = "{email.empty}")
    private String email;

    @Positive
    private int roleId;

}
