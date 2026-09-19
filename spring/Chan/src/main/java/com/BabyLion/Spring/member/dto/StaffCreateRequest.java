package com.BabyLion.Spring.member.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StaffCreateRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전공은 필수입니다.")
    private String major;

    @NotBlank(message = "파트는 필수입니다.")
    private String part;

    @Min(value = 1, message = "기수는 1 이상이어야 합니다.")
    private int generation;

    @NotBlank(message = "직책은 필수입니다.")
    private String position;
}
