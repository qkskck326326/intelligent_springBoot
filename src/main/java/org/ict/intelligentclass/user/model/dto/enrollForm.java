package org.ict.intelligentclass.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class enrollForm {
    private String userEmail;
    private String provider;
    private String userName;
    private String userPwd;
    private String phone;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
    private String profileImageUrl;
    private int userType;
    private int reportCount;
    private char loginOk;
    private char faceLoginYn;
    private String snsAccessToken;

    private List<Long> interests; // 추가된 부분
    //학력 리스트
    //경력 리스트

}
