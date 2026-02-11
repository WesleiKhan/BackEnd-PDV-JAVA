package com.example.PDV.UsersCore;

import com.example.PDV.Config.ConfigAuth.CustomUserDetails;
import com.example.PDV.Exceptions.UserNotFound;
import com.example.PDV.UsersCore.UserDtos.UserEntryDto;
import com.example.PDV.UsersCore.UserDtos.UserOutDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void createUser(UserEntryDto user) {

        String encryptPassword = new BCryptPasswordEncoder()
                .encode(user.getPassword().trim());

        user.setPassword(encryptPassword);

        UserEntity newUser = new UserEntity(user);

        userRepository.save(newUser);
    }

    public void updateUser(UserEntryDto userUpdate, Integer userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        user.updateUser(userUpdate);

        userRepository.save(user);
    }

    public List<UserOutDto> getUsers() {

        return userRepository.findAllBy();
    }

    public void deleteUser(Integer id) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        userRepository.delete(user);
    }

    public UserEntity loggedInUser() {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

       return userRepository.findById(userDetails.getId())
                .orElseThrow(UserNotFound::new);

    }

    public static Integer currentUserId() {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return userDetails.getId();
    }
}
