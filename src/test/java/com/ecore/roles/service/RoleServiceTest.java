package com.ecore.roles.service;

import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ecore.roles.utils.TestData.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl rolesService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipService membershipService;

    @Test
    public void shouldCreateRole() {
        Role developerRole = DEVELOPER_ROLE();
        when(roleRepository.save(developerRole)).thenReturn(developerRole);

        Role role = rolesService.saveRole(developerRole);

        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> rolesService.saveRole(null));
    }

    @Test
    public void shouldReturnRoleWhenRoleIdExists() {
        Role developerRole = DEVELOPER_ROLE();
        when(roleRepository.findById(developerRole.getId())).thenReturn(Optional.of(developerRole));

        Role role = rolesService.getRole(developerRole.getId());

        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRole(UUID_1));

        assertEquals(format("Role %s not found", UUID_1), exception.getMessage());
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdIsNull() {
        assertThrows(NullPointerException.class,
                () -> rolesService.getRole(null));
    }

    @Test
    public void shouldReturnAllRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(DEVELOPER_ROLE()));

        List<Role> roles = rolesService.findAllRoles();

        assertNotNull(roles);
        assertEquals(1, roles.size());
    }

    @Test
    public void shouldReturnAllRolesWhenThereAreNoRoles() {
        when(roleRepository.findAll()).thenReturn(List.of());

        List<Role> roles = rolesService.findAllRoles();

        assertNotNull(roles);
        assertEquals(0, roles.size());
    }

    @Test
    public void shouldReturnDefaultRoleWhenThereAreNoRoles() {
        when(roleRepository.findByName(DEFAULT_ROLE_NAME))
                .thenReturn(Optional.of(DEVELOPER_ROLE()));

        Role role = rolesService.getDefaultRole();

        assertNotNull(role);
        assertEquals(DEVELOPER_ROLE().getName(), role.getName());
    }

    @Test
    public void shouldNotReturnDefaultRoleAndThrowExceptionWhenThereAreRoles() {
        when(roleRepository.findByName(DEFAULT_ROLE_NAME)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> rolesService.getDefaultRole());
    }

    @Test
    public void shouldReturnRoleListByUserIdAndTeamId() {
        List<UUID> userIdList = List.of(UUID_1, UUID_2, GIANNI_USER_UUID);
        List<UUID> teamIdList = List.of(UUID_1, UUID_2, ORDINARY_CORAL_LYNX_TEAM_UUID);
        when(membershipRepository.findByUserIdInAndTeamIdIn(userIdList, teamIdList))
                .thenReturn(List.of(DEFAULT_MEMBERSHIP(), INVALID_MEMBERSHIP()));

        List<Role> roles = rolesService.getRolesListFromMembershipsByUserAndTeam(userIdList, teamIdList);

        assertNotNull(roles);
        assertEquals(2, roles.size());
    }
}
