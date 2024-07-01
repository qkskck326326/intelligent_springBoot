package org.ict.intelligentclass.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordForm {
    private String userEmail;
    private String currentPassword;
    private String newPassword;

}
