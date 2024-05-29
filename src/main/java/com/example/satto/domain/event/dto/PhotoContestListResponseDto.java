package com.example.satto.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public record PhotoContestListResponseDto(
        List<PhotoContestResponseDto> photoContestResponseDtoList
) {
}
