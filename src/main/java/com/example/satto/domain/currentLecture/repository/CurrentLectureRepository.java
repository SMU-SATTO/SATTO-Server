package com.example.satto.domain.currentLecture.repository;

import com.example.satto.domain.currentLecture.entity.CurrentLecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CurrentLectureRepository extends JpaRepository<CurrentLecture, String> {

//    List<CurrentLecture> findCurrentLectureByEstDeptInfoAndCrsShyr(String sbjNm, int year);
//
//    @Query( "select l " +
//            "from CurrentLecture l  " +
//            "where l.tlsnRmk not in ('외국인 유학생 전용분반', '융합경영학과 전용분반', '지능·데이터융합학부, 휴먼지능정보공학전공, 컴퓨터과학전공, 게임전공 학생 수강 불가', '인문사회과학대학 전용 분반', '경영경제대학 전용 분반', '문화예술대학 전용 분반')" +
//            "and l.sbjNm not like '%교양과인성%' " +
//            "and l.sbjNm not like '%사회봉사%' " +
//            "and l.sbjNm not like '성공학%' " +
//            "and l.cyberYn = 'N' " + //E러닝 강의 제외
//            "and l.cmpDivNm = :cmpdivnm   ")
//    List<CurrentLecture> findLectByCmpDivNm(@Param("cmpdivnm") String cmpdivnm);
//    CurrentLecture findCurrentLectureBySbjDivcls(String sbjDivcls);

}
