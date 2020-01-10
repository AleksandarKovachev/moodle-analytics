package com.moodle.analytics.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordForm {

    private String oldPassword;

    @NotBlank(message = "{password.size}")
    @Size(min = 6, message = "{password.size}")
    private String newPassword;

    private String repeatNewPassword;

}