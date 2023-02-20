package com.example.gip5groep7.Services;

import com.example.gip5groep7.Models.User;
import com.example.gip5groep7.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User addUser(String username, String password, String name, String phoneNumber, Date dateOfBirth){
        return userRepository.save(new User(username, password, name, phoneNumber, dateOfBirth));
    }

}
