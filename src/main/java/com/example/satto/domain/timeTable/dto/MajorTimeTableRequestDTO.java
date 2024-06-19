package com.example.satto.domain.timeTable.dto;

import java.util.List;

public record MajorTimeTableRequestDTO(
        int GPA,
        List<String> requiredLect,
        int majorCount,
        int cyberCount,
        String impossibleTimeZone
) {
}
