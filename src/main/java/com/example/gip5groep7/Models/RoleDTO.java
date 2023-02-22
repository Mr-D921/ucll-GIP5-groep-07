package com.example.gip5groep7.Models;


import java.util.Collection;

public class RoleDTO {
    private String name;
    private Collection<Privilege> privileges;



    //private Collection<User> users;
    private User user;

    public RoleDTO() {
    }

    /*public RoleDTO(String name, Collection<Privilege> privileges, Collection<User> users) {
        this.name = name;
        this.privileges = privileges;
        this.users = users;
    }*/

    public RoleDTO(String name, Collection<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    /*public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }*/

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
