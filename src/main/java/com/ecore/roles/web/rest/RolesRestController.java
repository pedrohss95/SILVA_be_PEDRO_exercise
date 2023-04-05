package com.ecore.roles.web.rest;

import com.ecore.roles.model.Role;
import com.ecore.roles.service.RoleService;
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

    private final RoleService roleService;

    @Override
    @PostMapping(
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<RoleDto> createRole(
            @Valid @RequestBody RoleDto roleDto) {
        return ResponseEntity
                .status(201)
                .body(fromModel(roleService.saveRole(roleDto.toModel())));
    }

    @Override
    @GetMapping(
            produces = {"application/json"})
    public ResponseEntity<List<RoleDto>> getRoles() {

        List<Role> getRoles = roleService.findAllRoles();

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
                .body(fromModel(roleService.getRole(roleId)));
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
                .body(fromModel(roleService.getRoleFromMembershipsByUserAndTeam(userId, teamId)));
    }

    @Override
    @PostMapping(
            path = "/search",
            produces = {"application/json"})
    public ResponseEntity<List<RoleDto>> getListOfRolesByUserIdAndTeamId(
            @Valid @RequestBody RoleSearchDto roleSearchDto) {
        List<Role> roles = roleService.getRolesListFromMembershipsByUserAndTeam(
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
