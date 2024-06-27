package com.example.satto.domain.currentLecture.repository;

import com.example.satto.domain.currentLecture.dto.CurrentLectureResponseDTO;
import java.util.List;

public interface CurrentLectureRepositoryCustom {
    List<CurrentLectureResponseDTO> findLectures(
            List<String> codeSection, List<Integer> grade, int elective,
            int normal, int essential, byte humanity, byte society,
            byte nature, byte engineering, byte art, byte isCyber, String timeZone);
}

