package com.ecore.roles.web;

import com.ecore.roles.web.dto.MembershipDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

public interface MembershipsApi {

    ResponseEntity<MembershipDto> createMembership(
            MembershipDto membership);

    ResponseEntity<List<MembershipDto>> getMembershipsByRole(
            UUID roleId);

    @GetMapping(
            path = "/",
            produces = {"application/json"})
    ResponseEntity<List<MembershipDto>> getMemberships();
}
