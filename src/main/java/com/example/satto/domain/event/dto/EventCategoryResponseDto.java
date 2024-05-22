package com.example.satto.domain.event.dto;

import java.time.LocalDateTime;

public record EventCategoryResponseDto(
        String category,
        int participantsCount,
        LocalDateTime startWhen,
        LocalDateTime untilWhen
) {
}
