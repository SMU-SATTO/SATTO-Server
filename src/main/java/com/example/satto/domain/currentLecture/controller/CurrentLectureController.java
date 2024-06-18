package com.example.satto.domain.currentLecture.controller;

import com.example.satto.domain.currentLecture.dto.CurrentLectureListResponseDTO;
import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.currentLecture.service.CurrentLectureService;
import com.example.satto.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/current-lecture")
@CrossOrigin("*")
@Tag(name = "Current Lecture Domain Api", description = "현재 강의 조회 관련 Api입니다.")
public class CurrentLectureController {

    private final CurrentLectureService currentLectureService;


    //TODO: QueryDSL을 이용한 강의 필터링 기능 추가 구현 필요
    @Operation(method = "GET", summary = "현재 강의 목록 조회",
            description = "시간표 자동 생성 과정에서 현재 강의 정보를 검색할때 사용됩니다. 요청한 현재 강의 정보 목록을 반환합니다.")
    @GetMapping("")
    public BaseResponse<CurrentLectureListResponseDTO> getCurrentLecture(){
        CurrentLectureListResponseDTO result =  currentLectureService.getCurrentLectureList();
        return BaseResponse.onSuccess(result);
    }
}
