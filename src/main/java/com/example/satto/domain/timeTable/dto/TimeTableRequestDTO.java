package com.example.satto.domain.timeTable.dto;

import java.util.List;

public record TimeTableRequestDTO(
        int maxGPA,
        int minGPA,
        List<String> requiredLect,
        int majorCount,
        int cyberCount,
        String impossibleTimeZone
) {
}
