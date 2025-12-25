package com.example.PDV.BoxCore;

import com.example.PDV.BoxCore.BoxDtos.BoxEntryDto;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.UsersCore.UserEntity;
import com.example.PDV.UsersCore.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BoxService {

    private final BoxRepository boxRepository;

    private final UserRepository userRepository;

    public BoxService(BoxRepository boxRepository, UserRepository userRepository) {

        this.boxRepository = boxRepository;
        this.userRepository = userRepository;
    }

    public void startBox(BoxEntryDto boxEntry) {

        UserEntity operator = userRepository.findById(boxEntry.getIdOperator())
                .orElseThrow(UserNotFound::new);

        BoxEntity box = new BoxEntity(operator);

        boxRepository.save(box);
    }

    public void finishBox(Integer boxId) {

        BoxEntity newBox = boxRepository.findById(boxId)
                .orElseThrow(BoxNotFound::new);

        newBox.setEndDate(LocalDateTime.now());

        boxRepository.save(newBox);
    }
}
