package com.example.satto.domain.timeTable.dto;

import java.util.List;

public record EntireTimeTableRequestDTO(
        int GPA,
        List<String> requiredLect,
        int majorCount,
        int cyberCount,
        String impossibleTimeZone,
        List<List<String>> majorList //학수번호
) {
}
