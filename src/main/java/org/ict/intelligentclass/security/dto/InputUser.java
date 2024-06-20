package org.ict.intelligentclass.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputUser {
    private String userEmail;
    private String provider;
    private String userPwd;


    public InputUser(String userEmail) {
        this.userEmail = userEmail;
    }


    public InputUser(String userEmail, String provider) {
        this.userEmail = userEmail;
        this.provider = provider;
    }
}
