package com.example.satto.domain.course.converter;

import com.example.satto.domain.course.dto.CourseResponseDto;
import com.example.satto.domain.course.dto.CourseResponseListDto;
import com.example.satto.domain.course.dto.GraduationResponseDto;
import com.example.satto.domain.course.entity.PreviousLecture;

import java.util.List;

public class CourseConverter {
    //사용자 수강 목록 조회 Dto 변환 메소드
    public static CourseResponseListDto toCourseResponseDtoList(List<PreviousLecture> previousLectureList) {
        List<CourseResponseDto> courseResponseDtoList = previousLectureList.stream()
                .map(CourseConverter::toCourseResponseDto)
                .toList();
        return CourseResponseListDto.builder()
                .courseResponseDtoList(courseResponseDtoList)
                .build();
    }

    //사용자 수강 과목 조회 Dto 변환 메소드
    public static CourseResponseDto toCourseResponseDto(PreviousLecture previousLecture) {
        return CourseResponseDto.builder()
                .cmpDiv(previousLecture.getCmpDiv())
                .name(previousLecture.getName())
                .professor(previousLecture.getProfessor())
                .semesterYear(previousLecture.getSemesterYear())
                .code(previousLecture.getCode())
                .build();
    }

    //사용자 졸업 요건 충족도 조회 Dto 변환 메소드
    public static GraduationResponseDto toGraduationResponseDto(List<PreviousLecture> previousLectureList) {
        return GraduationResponseDto.builder()
                .advanceMajor(
                        previousLectureList.stream()
                                .filter(previousLecture -> "advancedMajor".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum()
                )
                .selectableMajor(
                        previousLectureList.stream()
                                .filter(previousLecture -> "selectableMajor".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum()
                )
                .thinkingAndExpression(
                        previousLectureList.stream()
                                .filter(previousLecture -> "thinkingAndExpression".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum()
                )
                .englishFoundation(
                        previousLectureList.stream()
                                .filter(previousLecture -> "englishFoundation".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .basicMath(
                        previousLectureList.stream()
                                .filter(previousLecture -> "basicMath".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .expertiseResearchSkill(
                        previousLectureList.stream()
                                .filter(previousLecture -> "expertiseResearchSkill".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .creativeProblemSolvingSkill(
                        previousLectureList.stream()
                                .filter(previousLecture -> "creativeProblemSolvingSkill".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .interdisciplinarySkill(
                        previousLectureList.stream()
                                .filter(previousLecture -> "interdisciplinarySkill".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .diversityRespectSkill(
                        previousLectureList.stream()
                                .filter(previousLecture -> "diversityRespectSkill".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .ethicalPracticeSkill(
                        previousLectureList.stream()
                                .filter(previousLecture -> "ethicalPracticeSkill".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .humanity(
                        previousLectureList.stream()
                                .filter(previousLecture -> "humanity".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .society(
                        previousLectureList.stream()
                                .filter(previousLecture -> "society".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .nature(
                        previousLectureList.stream()
                                .filter(previousLecture -> "nature".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .engineering(
                        previousLectureList.stream()
                                .filter(previousLecture -> "engineering".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .art(
                        previousLectureList.stream()
                                .filter(previousLecture -> "art".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .normal(
                        previousLectureList.stream()
                                .filter(previousLecture -> "normal".equals(previousLecture.getSubjectType()))
                                .mapToInt(PreviousLecture::getCredit)
                                .sum())
                .build();
    }

}
