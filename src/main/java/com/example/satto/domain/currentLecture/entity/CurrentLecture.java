package com.example.satto.domain.currentLecture.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CurrentLecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currentLectureId;

    private String lectName;
    private String lectTime;
    private String lectRoom;
    private String cmpDiv;
    private Integer credit;
    private String isCyber;
    private Integer grade;
    private String semesterYear;
    private String department;
    private String professor;
    private String subjectType;
    private String code;
    private String codeSection;
    private String notice;
}

