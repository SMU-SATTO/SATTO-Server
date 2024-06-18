package com.example.satto.domain.currentLecture.dto;

import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CurrentLectureResponseDTO(
        String department,
        String code,
        String lectName,
        String professor,
        String lectTime,
        String cmpDiv,
        String subjectType,
        int credit
) {
}
