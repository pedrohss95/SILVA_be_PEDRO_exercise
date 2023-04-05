package com.ecore.roles.web.rest;

import com.ecore.roles.model.Role;
import com.ecore.roles.service.RolesService;
import com.ecore.roles.web.RolesApi;
import com.ecore.roles.web.dto.RoleDto;
import com.ecore.roles.web.dto.RoleSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ecore.roles.web.dto.RoleDto.fromModel;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/roles")
public class RolesRestController implements RolesApi {

    private final RolesService rolesService;

    @Override
    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<RoleDto> createRole(
            @Valid @RequestBody RoleDto roleDto) {
        return ResponseEntity
                .status(201)
                .body(fromModel(rolesService.CreateRole(roleDto.toModel())));
    }

    @Override
    @GetMapping(
            produces = {"application/json"})
    public ResponseEntity<List<RoleDto>> getRoles() {

        List<Role> getRoles = rolesService.GetRoles();

        List<RoleDto> roleDtoList = new ArrayList<>();

        for (Role role : getRoles) {
            RoleDto roleDto = fromModel(role);
            roleDtoList.add(roleDto);
        }

        return ResponseEntity
                .status(200)
                .body(roleDtoList);
    }

    @Override
    @GetMapping(
            path = "/{roleId}",
            produces = {"application/json"})
    public ResponseEntity<RoleDto> getRole(
            @PathVariable UUID roleId) {
        return ResponseEntity
                .status(200)
                .body(fromModel(rolesService.GetRole(roleId)));
    }

    @Override
    @GetMapping(
            path = "/{teamMemberId}/{teamId}",
            produces = {"application/json"})
    public ResponseEntity<RoleDto> getRolesByUserIdAndTeamId(
            @PathVariable("teamMemberId") UUID userId,
            @PathVariable UUID teamId) {

        return ResponseEntity
                .status(200)
                .body(fromModel(rolesService.getRoleFromMembershipsByUserAndTeam(userId, teamId)));
    }

    @Override
    @PostMapping(
            path = "/search",
            produces = {"application/json"})
    public ResponseEntity<List<RoleDto>> getListOfRolesByUserIdAndTeamId(
            @Valid @RequestBody RoleSearchDto roleSearchDto) {
        List<Role> roles = rolesService.getRolesListFromMembershipsByUserAndTeam(
                roleSearchDto.getUserIdList(), roleSearchDto.getTeamIdList());

        List<RoleDto> roleDtoList = new ArrayList<>();
        for (Role role : roles) {
            RoleDto roleDto = fromModel(role);
            roleDtoList.add(roleDto);
        }
        return ResponseEntity
                .status(200)
                .body(roleDtoList);
    }

}
