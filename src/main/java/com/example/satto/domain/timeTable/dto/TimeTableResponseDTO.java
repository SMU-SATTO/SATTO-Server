package com.example.satto.domain.timeTable.dto;

import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public record TimeTableResponseDTO(
        List<CurrentLectureResponseDTO> timeTable,
        String totalTime
) {
    public static String calculateTotalTime(List<CurrentLectureResponseDTO> timetable) {
        String tt = "";
        for (CurrentLectureResponseDTO lecture : timetable) {
            tt += lecture.lectTime();
        }
        return tt;
    }


    public static TimeTableResponseDTO from(List<CurrentLectureResponseDTO> timeTable){
        return new TimeTableResponseDTO(
                timeTable,
                calculateTotalTime(timeTable)
        );
    }

    public static List<TimeTableResponseDTO> fromList(List<List<CurrentLectureResponseDTO>> timeTableList){
        return
                timeTableList.stream()
                        .map(TimeTableResponseDTO::from)
                        .collect(Collectors.toList());
    }
}
