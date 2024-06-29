package com.example.satto.domain.currentLecture.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CurrentLectureRequestDto(
        List<String> codeSection,
        List<Integer> grade,
        Byte elective,
        Byte normal,
        Byte essential,
        Byte humanity,
        Byte society,
        Byte nature,
        Byte engineering,
        Byte art,
        Byte isCyber,
        String timeZone
) {}

