package com.ecore.roles.web;

import com.ecore.roles.web.dto.RoleDto;
import com.ecore.roles.web.dto.RoleSearchDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface RolesApi {

    ResponseEntity<RoleDto> createRole(
            RoleDto role);

    ResponseEntity<List<RoleDto>> getRoles();

    ResponseEntity<RoleDto> getRole(
            UUID roleId);

    @GetMapping(
            path = "/{userId}/{teamId}",
            produces = {"application/json"})
    ResponseEntity<RoleDto> getRolesByUserIdAndTeamId(
            @PathVariable UUID userId,
            @PathVariable UUID teamId);

    @PostMapping(
            path = "/search",
            produces = {"application/json"})
    ResponseEntity<List<RoleDto>> getListOfRolesByUserIdAndTeamId(
            @Valid @RequestBody RoleSearchDto role);

}
