package com.example.satto.domain.timeTableLecture.repository;

import com.example.satto.domain.timeTableLecture.entity.TimeTableLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableLectureRepository extends JpaRepository<TimeTableLecture, Long> {

    @Query("select l " +
            "from TimeTableLecture l " +
            "where l.currentLecture.codeSection = :codeSection and l.timeTable.timetableId = :timeTableId")
    TimeTableLecture findTimeTableLectureByTimeTableIdAndCodeSection(@Param("timeTableId")Long timeTableId,
                                                                     @Param("codeSection") String codeSection);

    List<TimeTableLecture> findTimeTableLecturesByTimeTableId(Long timeTableId);
}
