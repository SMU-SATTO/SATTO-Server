package com.example.satto.domain.timeTable.dto;

import com.example.satto.domain.timeTable.entity.TimeTable;
import com.example.satto.domain.users.entity.Users;

import java.util.List;

public record TimeTableSelectRequestDTO(
        List<String> codeSectionList,
        String semesterYear,
        String timeTableName,
        boolean isPublic,
        boolean isRepresented

) {
    public TimeTable to(Users users){
        return TimeTable.builder()
                .users(users)
                .isPublic(isPublic)
                .isRepresented(isRepresented)
                .timetableName(timeTableName)
                .semesterYear(semesterYear)
                .build();
    }
}
