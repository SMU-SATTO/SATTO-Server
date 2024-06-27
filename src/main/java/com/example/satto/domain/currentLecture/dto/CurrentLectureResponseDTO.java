package com.example.satto.domain.currentLecture.dto;

import lombok.Builder;

@Builder
public record CurrentLectureResponseDTO(
        String department,
        String code,
        String lectName,
        String professor,
        String lectTime,
        String cmpDiv,
        String subjectType,
        Integer credit
) {}
