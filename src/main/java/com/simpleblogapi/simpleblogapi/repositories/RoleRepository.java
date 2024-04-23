package com.simpleblogapi.simpleblogapi.repositories;

import com.simpleblogapi.simpleblogapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByRoleName(String roleName);
    Role findByRoleName(String roleName);
}
