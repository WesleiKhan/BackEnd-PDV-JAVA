package com.example.PDV.BoxCore;

import com.example.PDV.BoxCore.BoxDtos.BoxEntryDto;
import com.example.PDV.BoxCore.BoxEnums.StatusBox;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.OperatorAlreadyBoxOpened;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.UsersCore.UserEntity;
import com.example.PDV.UsersCore.UserRepository;
import com.example.PDV.UsersCore.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BoxService {

    private final BoxRepository boxRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public BoxService(BoxRepository boxRepository,
                      UserRepository userRepository,
                      UserService userService) {

        this.boxRepository = boxRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void startBox(BoxEntryDto boxEntry) {

        UserEntity operator = userRepository.findById(boxEntry.getIdOperator())
                .orElseThrow(UserNotFound::new);

        if (boxRepository.findByStatus(operator, StatusBox.OPEN).isPresent())
            throw new OperatorAlreadyBoxOpened();

        BoxEntity box = new BoxEntity(operator);

        boxRepository.save(box);
    }

    public BoxEntity BoxOpened() {

        UserEntity operator = userService.loggedInUser();

        return boxRepository.findByStatus(operator, StatusBox.OPEN)
                .orElseThrow(BoxNotFound::new);
    }

    public void finishBox(Integer boxId) {

        BoxEntity newBox = boxRepository.findById(boxId)
                .orElseThrow(BoxNotFound::new);

        newBox.setEndDate(LocalDateTime.now());
        newBox.setStatus_of_box(StatusBox.CLOSE);

        boxRepository.save(newBox);
    }


}
