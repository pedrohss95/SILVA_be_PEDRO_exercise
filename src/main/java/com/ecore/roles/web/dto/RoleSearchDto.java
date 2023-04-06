package com.ecore.roles.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class RoleSearchDto {

    @JsonProperty(value = "teamMemberIds")
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private List<UUID> userIdList;

    @JsonProperty
    @Valid
    @NotNull
    @EqualsAndHashCode.Include
    private List<UUID> teamIdList;
}
