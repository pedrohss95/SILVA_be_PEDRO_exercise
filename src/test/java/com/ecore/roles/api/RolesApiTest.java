package com.ecore.roles.api;

import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.RoleDto;
import com.ecore.roles.web.dto.RoleSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static com.ecore.roles.utils.TestData.*;
import static io.restassured.RestAssured.when;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RolesApiTest {

    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;

    private MockRestServiceServer mockServer;

    @LocalServerPort
    private int port;

    @Autowired
    public RolesApiTest(RestTemplate restTemplate, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
        this.roleRepository = roleRepository;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
        Optional<Role> devOpsRole = roleRepository.findByName(DEVOPS_ROLE().getName());
        devOpsRole.ifPresent(roleRepository::delete);
    }

    @Test
    void shouldFailWhenPathDoesNotExist() {
        sendRequest(when()
                .get("/v1/role")
                .then())
                        .validate(404, "Not Found");
    }

    @Test
    void shouldCreateNewRole() {
        Role expectedRole = DEVOPS_ROLE();

        RoleDto actualRole = createRole(expectedRole)
                .statusCode(201)
                .extract().as(RoleDto.class);

        assertThat(actualRole.getName()).isEqualTo(expectedRole.getName());
    }

    @Test
    void shouldFailToCreateNewRoleWhenNull() {
        createRole(null)
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenMissingName() {
        createRole(Role.builder().build())
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenBlankName() {
        createRole(Role.builder().name("").build())
                .validate(400, "Bad Request");
    }

    @Test
    void shouldFailToCreateNewRoleWhenNameAlreadyExists() {
        createRole(DEVELOPER_ROLE())
                .validate(400, "Role already exists");
    }

    @Test
    void shouldGetAllRoles() {
        RoleDto[] roles = getRoles()
                .extract().as(RoleDto[].class);

        assertThat(roles.length).isGreaterThanOrEqualTo(3);
        assertThat(roles).contains(RoleDto.fromModel(DEVELOPER_ROLE()));
        assertThat(roles).contains(RoleDto.fromModel(PRODUCT_OWNER_ROLE()));
        assertThat(roles).contains(RoleDto.fromModel(TESTER_ROLE()));
    }

    @Test
    void shouldGetRoleById() {
        Role expectedRole = DEVELOPER_ROLE();

        getRole(expectedRole.getId())
                .statusCode(200)
                .body("name", equalTo(expectedRole.getName()));
    }

    @Test
    void shouldFailToGetRoleById() {
        getRole(UUID_1)
                .validate(404, format("Role %s not found", UUID_1));
    }

    @Test
    void shouldGetRoleByUserIdAndTeamId() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        mockGetTeamById(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, ORDINARY_CORAL_LYNX_TEAM());

        getRoleByUserIdAndTeamId(expectedMembership.getUserId(), expectedMembership.getTeamId())
                .statusCode(200)
                .body("name", equalTo(expectedMembership.getRole().getName()));
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenUserIdIsInvalid() {
        getRoleByUserIdAndTeamId(UUID.fromString(INVALID_UUID), ORDINARY_CORAL_LYNX_TEAM_UUID)
                .validate(404, format("Role %s %s not found", UUID.fromString(INVALID_UUID),
                        ORDINARY_CORAL_LYNX_TEAM_UUID));
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenTeamIdIsInvalid() {
        getRoleByUserIdAndTeamId(GIANNI_USER_UUID, UUID.fromString(INVALID_UUID))
                .validate(404,
                        format("Role %s %s not found", GIANNI_USER_UUID, UUID.fromString(INVALID_UUID)));
    }

    @Test
    void shouldFailToGetRoleByUserIdAndTeamIdWhenItDoesNotExist() {
        mockGetTeamById(mockServer, UUID_1, null);
        getRoleByUserIdAndTeamId(GIANNI_USER_UUID, UUID_1)
                .validate(404, format("Role %s %s not found", GIANNI_USER_UUID, UUID_1));
    }

    @Test
    void shouldGetRoleListByUserIdAndTeamId() {
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        mockGetTeamById(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, ORDINARY_CORAL_LYNX_TEAM());

        RoleDto[] roles = getRoleListByUserIdAndTeamId(ROLE_SEARCH_DTO())
                .extract().as(RoleDto[].class);

        assertThat(roles).contains(RoleDto.fromModel(DEVELOPER_ROLE()));
    }

    @Test
    void shouldFailToGetRoleListByUserIdAndTeamIdWhenUserIdIsInvalid() {
        RoleSearchDto roleSearchDto = INVALID_ROLE_SEARCH_DTO();
        Membership expectedMembership = DEFAULT_MEMBERSHIP();
        mockGetTeamById(mockServer, ORDINARY_CORAL_LYNX_TEAM_UUID, ORDINARY_CORAL_LYNX_TEAM());
        createMembership(expectedMembership)
                .statusCode(201);

        RoleDto[] roles = getRoleListByUserIdAndTeamId(roleSearchDto)
                .extract().as(RoleDto[].class);

        assertThat(roles).doesNotContain(RoleDto.fromModel(DEVELOPER_ROLE()));
    }
}
