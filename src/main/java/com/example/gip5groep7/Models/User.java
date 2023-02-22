package com.example.gip5groep7.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER") //niet zeker of dit klopt
public class  User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    //todo add Roles: Collection<Role>

    @OneToMany
    private List<Role> roles;


    public int getId() {
        return id;
    }

    /*public void setId(int id) {
        this.id = id;
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public User() {
    }

    public User(String username, String password, String name, String phoneNumber, Date dateOfBirth) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setPhoneNumber(phoneNumber);
        setDateOfBirth(dateOfBirth);
    }

    public void addRole(String name){
        if (this.roles == null) this.roles = new ArrayList<Role>();
        Role role = new Role(name);
        this.roles.add(role);
    }

    public void deleteRole(String roleName){
        for (Role roleToDelete : this.roles) {
            if (roleToDelete.getName().equals(roleName)){
                this.roles.remove(roleToDelete);
            }
        }
    }

    public void deleteRoleById(Long roleId){
        for (Role roleToDelete : this.roles) {
            if (roleToDelete.getId().equals(roleId)){
                this.roles.remove(roleToDelete);
            }
        }
    }

}
