package com.example.satto.domain.currentLecture.converter;

import com.example.satto.domain.currentLecture.dto.CurrentLectureListResponseDTO;
import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.currentLecture.entity.CurrentLecture;

import java.util.List;

public class CurrentLectureConverter {

    //현재 학기 강의 조회 응답 Dto 변환 메소드
    public static CurrentLectureResponseDTO toCurrentLectureResponseDto(CurrentLecture currentLecture) {
        return CurrentLectureResponseDTO.builder()
                .department(currentLecture.getDepartment())
                .lectName(currentLecture.getLectName())
                .code(currentLecture.getCode())
                .professor(currentLecture.getProfessor())
                .lectTime(currentLecture.getLectTime())
                .cmpDiv(currentLecture.getCmpDiv())
                .subjectType(currentLecture.getSubjectType())
                .credit(currentLecture.getCredit())
                .build();
    }

    //현재 학기 강의 조회 리스트 응답 Dto 변환 메소드
    public static CurrentLectureListResponseDTO toCurrentLectureResponseDtoList(List<CurrentLecture> currentLectureList) {
        List<CurrentLectureResponseDTO> currentLectureResponseDTOList = currentLectureList.stream()
                .map(CurrentLectureConverter::toCurrentLectureResponseDto)
                .toList();
        return CurrentLectureListResponseDTO.builder()
                .currentLectureResponseDTOList(currentLectureResponseDTOList)
                .build();
    }
}
