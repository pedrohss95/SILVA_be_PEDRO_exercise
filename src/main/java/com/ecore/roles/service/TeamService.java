package com.ecore.roles.service;

import com.ecore.roles.client.model.Team;

import java.util.List;
import java.util.UUID;

public interface TeamService {

    Team getTeamById(UUID id);

    List<Team> getAllTeams();
}
