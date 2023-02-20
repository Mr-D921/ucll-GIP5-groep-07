package com.example.gip5groep7.Repositories;

import com.example.gip5groep7.Models.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Integer> {

}
