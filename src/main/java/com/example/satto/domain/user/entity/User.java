package com.example.satto.domain.user.entity;

import com.example.satto.domain.course.entity.Course;
import com.example.satto.domain.event.entity.PhotoContest;
import com.example.satto.domain.event.entity.PhotoContestDislike;
import com.example.satto.domain.event.entity.PhotoContestLike;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String department;

    @Column(name = "student_id", nullable = false)
    private int studentId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_public", nullable = false)
    private Byte isPublic;

//    // 팔로우
//    @OneToMany(mappedBy = "followedId")
//    private List<Follow> followedList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "followerId")
//    private List<Follow> followerList = new ArrayList<>();
//
//    // 시간표
//    @OneToMany(mappedBy = "userId")
//    private List<Timetable> timetableList = new ArrayList<>();
//

    // 이벤트 학교사진콘테스트
    @OneToOne(mappedBy = "user")
    private PhotoContest photoContest;

    // 시간표 좋아요
    @OneToMany(mappedBy = "user")
    private List<PhotoContestLike> likeList = new ArrayList<>();

    // 시간표 싫어요
    @OneToMany(mappedBy = "user")
    private List<PhotoContestDislike> dislikeList = new ArrayList<>();

    // 수강한 강의 목록
    @OneToMany(mappedBy = "user")
    private List<Course> courseList = new ArrayList<>();

}
