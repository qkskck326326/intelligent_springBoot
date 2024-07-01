package org.ict.intelligentclass.user.scheduler;

import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.repository.LoginTokenRepository;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceScheduler {

    private final UserService userService;
    private final LoginTokenRepository loginTokenRepository;

    public AttendanceScheduler(UserService userService, LoginTokenRepository loginTokenRepository) {
        this.userService = userService;
        this.loginTokenRepository = loginTokenRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
//    @Scheduled(cron = "0 0 11 * * ?")
    public void checkDailyAttendance() {
        List<LoginTokenEntity> activeUsers = loginTokenRepository.findAll();
        LocalDate today = LocalDate.now();

        for (LoginTokenEntity activeUser : activeUsers) {
            // 출석 체크 로직
            userService.checkAttendance(activeUser.getUserId().getUserEmail(), activeUser.getUserId().getProvider());
        }
    }
}
