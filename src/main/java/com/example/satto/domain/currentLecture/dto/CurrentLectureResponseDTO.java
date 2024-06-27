package com.example.satto.domain.currentLecture.dto;

import lombok.Builder;

@Builder
public record CurrentLectureResponseDTO(
        String department,
        String major,
        String code,
        String codeSection,
        String lectName,
        String professor,
        String lectTime,
        String cmpDiv,
        String subjectType,
        Integer credit
) {}
