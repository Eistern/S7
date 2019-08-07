package com.contacts.demo.security;

import com.contacts.demo.security.data.JpaRoleLinkRepository;
import com.contacts.demo.security.data.JpaRoleRepository;
import com.contacts.demo.security.data.types.Role;
import com.contacts.demo.security.data.types.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleHelper {
    private final JpaRoleLinkRepository roleLinkRepository;
    private final JpaRoleRepository roleRepository;

    @Autowired
    public RoleHelper(JpaRoleLinkRepository roleLinkRepository, JpaRoleRepository roleRepository) {
        this.roleLinkRepository = roleLinkRepository;
        this.roleRepository = roleRepository;
    }

    public UserRole addRoleTo(Integer uid, Role.Roles role) {
        Optional<Role> foundRole = roleRepository.findByRoleName(role.toString());
        if (foundRole.isEmpty()) {
            roleRepository.save(new Role(null, role.toString(), null));
            foundRole = roleRepository.findByRoleName(role.toString());
            if (foundRole.isEmpty())
                throw new Error("Internal database error");
        }
        return roleLinkRepository.save(new UserRole(null, uid, foundRole.get().getRoleId()));
    }
}
