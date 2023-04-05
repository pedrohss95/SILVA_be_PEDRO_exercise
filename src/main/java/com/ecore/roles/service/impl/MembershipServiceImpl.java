package com.ecore.roles.service.impl;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipService;
import com.ecore.roles.service.TeamService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Log4j2
@Service
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    private final TeamService teamService;

    @Autowired
    public MembershipServiceImpl(
            MembershipRepository membershipRepository,
            RoleRepository roleRepository,
            TeamService teamService) {
        this.membershipRepository = membershipRepository;
        this.roleRepository = roleRepository;
        this.teamService = teamService;
    }

    @Override
    public Membership createMembership(@NonNull Membership membership) throws ResourceNotFoundException {

        UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                .orElseThrow(() -> new InvalidArgumentException(Role.class));

        if (membershipRepository.findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId())
                .isPresent()) {
            throw new ResourceExistsException(Membership.class);
        }

        Team team = teamService.getTeamById(membership.getTeamId());
        if (team == null) {
            throw new ResourceNotFoundException(Team.class, membership.getTeamId());
        }
        roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
        return membershipRepository.save(membership);
    }

    @Override
    public List<Membership> getMembershipsByRole(@NonNull UUID roleId) {
        return membershipRepository.findByRoleId(roleId);
    }

    @Override
    public List<Membership> getMemberships() {
        return membershipRepository.findAll();
    }

}
