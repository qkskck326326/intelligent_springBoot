package org.ict.intelligentclass.complete.model.service;


import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.complete.jpa.entity.CompleteEntity;
import org.ict.intelligentclass.complete.jpa.repository.CompleteRepository;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CompleteService {


    private final CompleteRepository completeRepository;
    private final UserRepository userRepository;

    public List<CompleteEntity> getCompletesByNickname(String nickname) {
        return completeRepository.findByNickname(nickname);
    }

    public Optional<UserEntity> getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }


}
