package com.example.satto.domain.course.dto;

import lombok.Builder;

@Builder
public record PreviousLectureResponseDto(
        String cmpDiv,
        String name,
        String professor,
        String code
) {
}
