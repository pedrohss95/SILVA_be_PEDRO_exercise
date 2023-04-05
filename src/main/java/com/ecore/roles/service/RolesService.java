package com.ecore.roles.service;

import com.ecore.roles.model.Role;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

public interface RolesService {

    Role CreateRole(Role role);

    Role GetRole(UUID id);

    List<Role> GetRoles();

    Role getRoleFromMembershipsByUserAndTeam(@NonNull UUID userId, @NonNull UUID teamId);

    List<Role> getRolesListFromMembershipsByUserAndTeam(
            @NonNull List<UUID> userIdList,
            @NonNull List<UUID> teamIdList);

}
