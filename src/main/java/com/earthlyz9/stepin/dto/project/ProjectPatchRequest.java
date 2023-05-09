package com.earthlyz9.stepin.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "프로젝트 정보 수정을 위한 스키마")
public class ProjectPatchRequest {
    @Schema(description = "프로젝트 이름")
    private String name;
}
