package com.example.satto.domain.timeTable.dto;

import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.timeTable.entity.TimeTable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record TimeTableResponseDTO(
) {
    public static record EntireTimeTableResponseDTO(
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


        public static TimeTableResponseDTO.EntireTimeTableResponseDTO from(List<CurrentLectureResponseDTO> timeTable){
            return new TimeTableResponseDTO.EntireTimeTableResponseDTO(
                    timeTable,
                    calculateTotalTime(timeTable)
            );
        }

        public static List<TimeTableResponseDTO.EntireTimeTableResponseDTO> fromList(List<List<CurrentLectureResponseDTO>> timeTableList){
            return
                    timeTableList.stream()
                            .map(TimeTableResponseDTO.EntireTimeTableResponseDTO::from)
                            .collect(Collectors.toList());
        }
    }

    public static record timeTableListDTO(
            String semesterYear,
            String timeTableName
    ){
        public static TimeTableResponseDTO.timeTableListDTO from(TimeTable timeTable){
            return new TimeTableResponseDTO.timeTableListDTO(
                    timeTable.getSemesterYear(),
                    timeTable.getTimetableName()
            );
        }

        public static List<TimeTableResponseDTO.timeTableListDTO> fromList(List<TimeTable> timeTableList){
            return
                    timeTableList.stream()
                            .map(TimeTableResponseDTO.timeTableListDTO::from)
                            .collect(Collectors.toList());
        }

    }

    public static record MajorCombinationResponseDTO(
            Set<String> combination
    ) {
    }

    public static record SelectTimeTableResponseDTO(
            List<CurrentLectureResponseDTO> lects,
            String semesterYear,
            String timeTableName,
            boolean isPublic,
            boolean isRepresented

    ){
        public static TimeTableResponseDTO.SelectTimeTableResponseDTO from(List<CurrentLectureResponseDTO> lects, TimeTable timeTable){
            return new TimeTableResponseDTO.SelectTimeTableResponseDTO(
                    lects,
                    timeTable.getSemesterYear(),
                    timeTable.getTimetableName(),
                    timeTable.getIsPublic(),
                    timeTable.getIsRepresented()
            );
        }

    }


}
