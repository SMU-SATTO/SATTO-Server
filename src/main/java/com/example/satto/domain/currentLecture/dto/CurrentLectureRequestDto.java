package com.example.satto.domain.currentLecture.dto;

import java.util.List;

public record CurrentLectureRequestDto(
        List<String> codeSectionList,
        List<Integer> grade,
        byte elective,
        byte normal,
        byte essential,
        byte humanity,
        byte society,
        byte nature,
        byte engineering,
        byte art,
        byte isCyber,
        String timezone
) {
}
