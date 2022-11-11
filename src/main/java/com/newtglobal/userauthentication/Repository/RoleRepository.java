package com.newtglobal.userauthentication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.newtglobal.userauthentication.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
