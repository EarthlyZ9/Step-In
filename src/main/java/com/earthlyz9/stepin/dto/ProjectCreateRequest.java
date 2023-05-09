package com.earthlyz9.stepin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "프로젝트 생성을 위한 스키마")
@AllArgsConstructor
@Builder
public class ProjectCreateRequest {
    @JsonIgnore
    private Integer id;

    @JsonIgnore
    private Integer ownerId;

    @Schema(description = "프로젝트 이름")
    private String name;
}
