package com.example.satto.domain.currentLecture.controller;

import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.currentLecture.service.CurrentLectureService;
import com.example.satto.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lecture")
public class CurrentLectureController {

    private final CurrentLectureService currentLectureService;


    //TODO: QueryDSL을 이용한 강의 필터링 기능 추가 구현 필요
    @GetMapping("/search")
    public BaseResponse<List<CurrentLectureResponseDTO>> getCurrentLecture(){
        List<CurrentLectureResponseDTO> result =  currentLectureService.getCurrentLecture();
        return BaseResponse.onSuccess(result);
    }
}
