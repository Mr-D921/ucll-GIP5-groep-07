package com.example.gip5groep7.Services;

import com.example.gip5groep7.Models.User;
import com.example.gip5groep7.Models.UserDTO;
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

    public User getUserById(int id){
        return userRepository.findById(id).orElseThrow(UserNotFound::new);
    }

    public User updateUser(int id, UserDTO updateUser){
        //something
        User user = getUserById(id);
        user.setName(updateUser.getName());
        user.setPassword(updateUser.getPassword());
        user.setDateOfBirth(updateUser.getDateOfBirth());
        user.setPhoneNumber(updateUser.getPhoneNumber());
        return userRepository.save(user);
    }

    public void deleteUser(int id){
        User user = getUserById(id);
        userRepository.delete(user);
    }

}
