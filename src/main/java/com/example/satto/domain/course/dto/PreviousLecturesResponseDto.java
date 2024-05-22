package com.example.satto.domain.course.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public record PreviousLecturesResponseDto(
        String cmpDiv,
        String name,
        String professor,
        String code
) {
}
