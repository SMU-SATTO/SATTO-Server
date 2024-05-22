package com.example.satto.domain.course.dto;

import lombok.Builder;

@Builder
public record GraduationResponseDto(
        int advanceMajor,
        int selectableMajor,
        int thinkingAndExpression,
        int englishFoundation,
        int basicMath,
        int expertiseResearchSkill,
        int creativeProblemSolvingSkill,
        int interdisciplinarySkill,
        int diversityRespectSkill,
        int ethicalPracticeSkill,
        int humanity,
        int society,
        int nature,
        int engineering,
        int art,
        int normal
) {
}
