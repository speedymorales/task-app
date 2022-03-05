package com.taskapp.taskapp.services;

import com.taskapp.taskapp.dto.CreateUserDto;
import com.taskapp.taskapp.dto.UserDto;
import com.taskapp.taskapp.entities.User;
import com.taskapp.taskapp.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
     @Autowired
    private UserRepository userRepository;

    public UserDto createUser(CreateUserDto userReq) {
        User user = new User();
        user.setName(userReq.name);
        user.setEmail(userReq.email);
        user.setPasswordHash(userReq.password);
        User createdUser = userRepository.save(user);
        UserDto userResponse = new UserDto();
        userResponse.name = createdUser.getName();
        userResponse.id = createdUser.getId();
        userResponse.email = createdUser.getEmail();
        return userResponse;

    }
}
