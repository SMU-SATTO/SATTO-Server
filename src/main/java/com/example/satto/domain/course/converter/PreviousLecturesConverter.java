package com.example.satto.domain.course.converter;

import com.example.satto.domain.course.dto.PreviousLectureListResponseDto;
import com.example.satto.domain.course.dto.PreviousLecturesResponseDto;
import com.example.satto.domain.course.entity.PreviousLecture;

import java.util.List;

public class PreviousLecturesConverter {

    //이전 강의 목록 조회 Dto 변환 메소드
    public static PreviousLectureListResponseDto toPreviousLecturesResponseDtoList(
            List<PreviousLecture> previousLectureList) {
        List<PreviousLecturesResponseDto> previousLecturesResponseDtoList =
                previousLectureList.stream()
                .map(PreviousLecturesConverter::toPreviousLecturesResponseDto)
                .toList();
        return PreviousLectureListResponseDto.builder()
                .previousLecturesResponseDtoList(previousLecturesResponseDtoList)
                .build();
    }

    //이전 강의 조회 Dto 변환 메소드
    public static PreviousLecturesResponseDto toPreviousLecturesResponseDto(PreviousLecture previousLecture) {
        return PreviousLecturesResponseDto.builder()
                .cmpDiv(previousLecture.getCmpDiv())
                .name(previousLecture.getName())
                .professor(previousLecture.getProfessor())
                .code(previousLecture.getCode())
                .build();
    }

}
