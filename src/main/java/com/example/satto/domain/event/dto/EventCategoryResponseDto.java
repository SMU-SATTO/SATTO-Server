package com.example.satto.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public record EventCategoryResponseDto(
        String category,
        Long participantsCount,
        LocalDateTime startWhen,
        LocalDateTime untilWhen
) {
}
