package com.example.satto.domain.currentLecture.service;

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

    public List<CurrentLectureResponseDTO> getCurrentLecture(){
        List<CurrentLecture> allLecture = currentLectureRepository.findAll();
        List<CurrentLectureResponseDTO> currentLectureResponseDTOList = CurrentLectureResponseDTO.from(allLecture);
        return currentLectureResponseDTOList;
    }
}
