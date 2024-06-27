package com.example.satto.domain.currentLecture.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CurrentLectureRequestDto(
        List<String> codeSection,
        List<Integer> grade,
        int elective,
        int normal,
        int essential,
        byte humanity,
        byte society,
        byte nature,
        byte engineering,
        byte art,
        byte isCyber,
        String timeZone
) {}

