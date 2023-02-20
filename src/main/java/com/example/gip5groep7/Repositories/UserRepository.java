package com.example.gip5groep7.Repositories;

import com.example.gip5groep7.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
