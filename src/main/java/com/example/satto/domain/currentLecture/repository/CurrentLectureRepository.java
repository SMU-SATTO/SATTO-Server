package com.example.satto.domain.currentLecture.repository;

import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrentLectureRepository extends JpaRepository<CurrentLecture, String> {

}
