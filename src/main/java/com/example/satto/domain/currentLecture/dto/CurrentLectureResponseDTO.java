package com.example.satto.domain.currentLecture.dto;

import com.example.satto.domain.currentLecture.entity.CurrentLecture;

import java.util.List;
import java.util.stream.Collectors;

public record CurrentLectureResponseDTO(
        String major,
        String sbjNo,
        String sbjDivcls,
        String sbjName,
        String prof,
        String time,
        String category,
        int cdt
) {
    public static CurrentLectureResponseDTO from(CurrentLecture currentLecture){
        return new CurrentLectureResponseDTO(
                currentLecture.getEstDeptInfo(),
                currentLecture.getSbjNo(),
                currentLecture.getSbjDivcls(),
                currentLecture.getSbjNm(),
                currentLecture.getStaffNm(),
                currentLecture.getLectTimeRoom(),
                currentLecture.getCmpDivNm(),
                currentLecture.getCdt()
        );
    }
    public static List<CurrentLectureResponseDTO> from(List<CurrentLecture> currentLectureList){
        return
                currentLectureList.stream()
                        .map(CurrentLectureResponseDTO::from)
                        .collect(Collectors.toList());
    }
}
