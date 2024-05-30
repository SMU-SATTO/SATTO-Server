package com.example.satto.domain.event.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PhotoContestListResponseDto(
        List<PhotoContestResponseDto> photoContestResponseDtoList
) {
}
