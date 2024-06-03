package com.example.satto.domain.timeTable.controller;

import com.example.satto.domain.timeTable.dto.MajorCombinationResponseDTO;
import com.example.satto.domain.timeTable.dto.TimeTableRequestDTO;
import com.example.satto.domain.timeTable.dto.TimeTableResponseDTO;
import com.example.satto.domain.timeTable.service.TimeTableService;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/timetable")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping
    public BaseResponse<List<MajorCombinationResponseDTO>> createTimeTable(TimeTableRequestDTO createDTO, @AuthenticationPrincipal Users users){
        List<MajorCombinationResponseDTO> result = timeTableService.createMajorTimeTable(createDTO, users);
        return BaseResponse.onSuccess(result);
    }
}