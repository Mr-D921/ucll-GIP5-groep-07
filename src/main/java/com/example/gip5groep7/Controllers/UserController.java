package com.example.gip5groep7.Controllers;

import com.example.gip5groep7.Models.User;
import com.example.gip5groep7.Models.UserDTO;
import com.example.gip5groep7.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserDTO userDTO){
        return userService.addUser(userDTO.getUsername(), userDTO.getPassword(), userDTO.getName(), userDTO.getPhoneNumber(), userDTO.getDateOfBirth());
    }

    @PutMapping
    public User updateUser(@PathVariable(name = "id") int id, @RequestBody UserDTO userDTO){
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping
    public void deleteUser(@PathVariable(name = "id") int id){
        userService.deleteUser(id);
    }
}
