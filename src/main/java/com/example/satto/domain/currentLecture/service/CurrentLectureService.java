package com.example.satto.domain.currentLecture.service;

import com.example.satto.domain.currentLecture.dto.CurrentLectureRequestDto;
import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import com.example.satto.domain.currentLecture.repository.CurrentLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentLectureService {

    private final CurrentLectureRepository currentLectureRepository;

    public List<CurrentLectureResponseDTO> getLectures(CurrentLectureRequestDto currentLectureRequestDto) {
        return currentLectureRepository.findLectures(
                currentLectureRequestDto.codeSection(),
                currentLectureRequestDto.grade(),
                currentLectureRequestDto.elective(),
                currentLectureRequestDto.normal(),
                currentLectureRequestDto.essential(),
                currentLectureRequestDto.humanity(),
                currentLectureRequestDto.society(),
                currentLectureRequestDto.nature(),
                currentLectureRequestDto.engineering(),
                currentLectureRequestDto.art(),
                currentLectureRequestDto.isCyber(),
                List.of(currentLectureRequestDto.timeZone().split(" "))
        );
    }
}
