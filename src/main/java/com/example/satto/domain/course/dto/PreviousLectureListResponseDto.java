package com.example.satto.domain.course.dto;

import java.util.List;

public record PreviousLectureListResponseDto(
        List<PreviousLecturesResponseDto> previousLecturesResponseDtoList
) {
}
