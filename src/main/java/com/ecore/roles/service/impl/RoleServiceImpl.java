package com.ecore.roles.service.impl;

import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.RoleService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {

    public static final String DEFAULT_ROLE = "Developer";

    private final RoleRepository roleRepository;
    private final MembershipRepository membershipRepository;

    @Autowired
    public RoleServiceImpl(
            RoleRepository roleRepository,
            MembershipRepository membershipRepository) {
        this.roleRepository = roleRepository;
        this.membershipRepository = membershipRepository;
    }

    @Override
    public Role saveRole(@NonNull Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ResourceExistsException(Role.class);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role getRole(@NonNull UUID roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public Role getDefaultRole() {
        return roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Default role is not configured"));
    }

    @Override
    public Role getRoleFromMembershipsByUserAndTeam(@NonNull UUID userId, @NonNull UUID teamId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId)
                .map(Membership::getRole)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class, userId, teamId));
    }

    @Override
    public List<Role> getRolesListFromMembershipsByUserAndTeam(
            @NonNull List<UUID> userIdList,
            @NonNull List<UUID> teamIdList) {
        List<Membership> memberships = membershipRepository.findByUserIdInAndTeamIdIn(userIdList, teamIdList);
        List<Role> roleList = new ArrayList<>();
        for (Membership membership : memberships) {
            Role role = membership.getRole();
            if (!roleList.contains(role))
                roleList.add(role);
        }
        return roleList;
    }

}
