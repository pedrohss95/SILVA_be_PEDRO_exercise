package com.ecore.roles.service;

import com.ecore.roles.model.Role;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface RoleService {

    Role saveRole(Role role);

    Role getRole(UUID roleId);

    List<Role> findAllRoles();

    Role getRoleFromMembershipsByUserAndTeam(@NonNull UUID userId, @NonNull UUID teamId);

    List<Role> getRolesListFromMembershipsByUserAndTeam(
            @NonNull List<UUID> userIdList,
            @NonNull List<UUID> teamIdList);

}
