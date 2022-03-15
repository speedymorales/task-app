package com.taskapp.taskapp.services;

import com.taskapp.taskapp.customObjects.AuthenticationObj;
import com.taskapp.taskapp.dto.CreateUserDto;
import com.taskapp.taskapp.dto.LoginDto;
import com.taskapp.taskapp.dto.LoginResponseDto;
import com.taskapp.taskapp.dto.UpdateUserDto;
import com.taskapp.taskapp.dto.UpdatedUserDto;
import com.taskapp.taskapp.dto.UserDto;
import com.taskapp.taskapp.entities.User;
import com.taskapp.taskapp.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
      @Autowired 
    private AuthenticationService authenticationService;

     @Autowired
    private UserRepository userRepository;

    public UserDto createUser(CreateUserDto userReq) {
        User user = new User();
        AuthenticationObj passwordAndSalt = authenticationService.hashPassword(userReq.password);
        user.setName(userReq.name);
        user.setEmail(userReq.email);
        user.setPasswordHash(passwordAndSalt.password);
        user.setPasswordSalt(passwordAndSalt.salt);
        User createdUser = userRepository.save(user);
        UserDto userResponse = new UserDto();
        userResponse.name = createdUser.getName();
        userResponse.id = createdUser.getId();
        userResponse.email = createdUser.getEmail();
        return userResponse;

    }
    
    public UpdatedUserDto updateUser(UpdateUserDto userReq) {
        List<User> userList = userRepository.findByEmail(userReq.email);
        Boolean saved = false;
        try {
            if(userList.size() > 0){
            User[] array = new User[userList.size()];
            userList.toArray(array); 
            User user = array[0];
            user.setName(userReq.name);
            userRepository.save(user);
            saved = true;
            }
            UpdatedUserDto userResponse = new UpdatedUserDto();
            if(saved){
                userResponse.status = "200";
            }
            else{
                userResponse.status = "400";
            }
            return userResponse;
            
        } catch (Exception e) {
          UpdatedUserDto userResponse = new UpdatedUserDto();
          userResponse.status = "404";
          return userResponse;
        }

    }
    
    public LoginResponseDto authenticateUser(LoginDto userReq) throws Exception{
        List<User> emailFound = userRepository.findByEmail(userReq.email);
        if((emailFound.size() > 0)){
            User[] array = new User[emailFound.size()];
            emailFound.toArray(array); 
            String password = array[0].getPasswordHash();
            String salt = array[0].getPasswordSalt();
            Boolean passwordsMatch = authenticationService.comparePassword(password, userReq.password, salt);
            LoginResponseDto response = new LoginResponseDto();
            if(passwordsMatch){
                response.message = "User accepted";
                response.status = 200; 
                return response;
            }
            response.message = "Incorrect username or password.";
            response.status = 400;
            return response;
        }
            LoginResponseDto failedLogin = new LoginResponseDto();
            failedLogin.message = "Email not found";
            failedLogin.status = 401;
            return failedLogin;
    }
}
