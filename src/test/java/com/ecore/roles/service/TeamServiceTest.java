package com.ecore.roles.service;

import com.ecore.roles.client.TeamClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ecore.roles.utils.TestData.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamServiceImpl TeamsService;
    @Mock
    private TeamClient TeamClient;

    @Test
    void shouldGetTeamWhenTeamIdExists() {
        Team ordinaryCoralLynxTeam = ORDINARY_CORAL_LYNX_TEAM();
        when(TeamClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ordinaryCoralLynxTeam));
        assertNotNull(TeamsService.getTeamById(ORDINARY_CORAL_LYNX_TEAM_UUID));
    }

    @Test
    void shouldNotGetTeamWhenTeamIdDoesNotExist() {
        when(TeamClient.getTeam(ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
        assertNull(TeamsService.getTeamById(ORDINARY_CORAL_LYNX_TEAM_UUID));
    }

    @Test
    void shouldGetAllTeams() {
        List<Team> teamList = List.of(ORDINARY_CORAL_LYNX_TEAM());
        when(TeamClient.getAllTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.OK)
                        .body(teamList));
        assertNotNull(TeamsService.getAllTeams());
    }

    @Test
    void shouldNotGetAllTeams() {
        when(TeamClient.getAllTeams())
                .thenReturn(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
        assertNull(TeamsService.getAllTeams());
    }
}
