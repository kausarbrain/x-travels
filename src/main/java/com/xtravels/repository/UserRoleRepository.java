package com.xtravels.repository;

import com.xtravels.models.Role;
import com.xtravels.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> getByName(String name);
}
