package com.example.satto.domain.course.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PreviousLectureListResponseDto(
        List<PreviousLecturesResponseDto> previousLecturesResponseDtoList
) {
}
