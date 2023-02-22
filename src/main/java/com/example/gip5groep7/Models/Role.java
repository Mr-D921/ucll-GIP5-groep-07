package com.example.gip5groep7.Models;

import jakarta.persistence.*;

import java.util.Collection;
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @ManyToMany
    private Collection<Privilege> privileges;

    @ManyToOne
    private User user;

    public Role() {
    }

    /*public Role(String name, Collection<Privilege> privileges, Collection<User> users) {
        this.name = name;
        this.privileges = privileges;
        this.users = users;
    }*/

    public Role(String name, Collection<Privilege> privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public Role(String name){
    this.name = name;
    }

    public Long getId() {
        return id;
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
}
