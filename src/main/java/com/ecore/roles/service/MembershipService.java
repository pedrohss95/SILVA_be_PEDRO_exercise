package com.ecore.roles.service;

import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;

import java.util.List;
import java.util.UUID;

public interface MembershipService {

    Membership createMembership(Membership membership) throws ResourceNotFoundException;

    List<Membership> getMembershipsByRole(UUID roleId);

    List<Membership> getMemberships();
}
