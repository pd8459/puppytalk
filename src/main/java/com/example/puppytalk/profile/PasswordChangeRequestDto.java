package com.example.puppytalk.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDto {
    public String currentPassword;
    public String newPassword;
    public String confirmPassword;
}
