package com.example.gip5groep7.Models;

import jakarta.persistence.*;

import java.util.Collection;
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    //@ManyToMany
    //private Collection<Privilege> priveleges = new Collection<Privilege>();

    //@ManyToMany
    //private Collection<User> users = new Collection<User>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
