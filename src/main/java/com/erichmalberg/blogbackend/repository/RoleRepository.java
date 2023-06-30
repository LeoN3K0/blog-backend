package com.erichmalberg.blogbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erichmalberg.blogbackend.models.ERole;
import com.erichmalberg.blogbackend.models.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
