package com.example.satto.domain.currentLecture.service;

import com.example.satto.domain.currentLecture.converter.CurrentLectureConverter;
import com.example.satto.domain.currentLecture.dto.CurrentLectureListResponseDTO;
import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import com.example.satto.domain.currentLecture.repository.CurrentLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentLectureService {

    private final CurrentLectureRepository currentLectureRepository;

    public CurrentLectureListResponseDTO getCurrentLectureList(){
        List<CurrentLecture> allLecture = currentLectureRepository.findAll();
        CurrentLectureListResponseDTO currentLectureResponseDTOList = CurrentLectureConverter.toCurrentLectureResponseDtoList(allLecture);
        return currentLectureResponseDTOList;
    }
}
