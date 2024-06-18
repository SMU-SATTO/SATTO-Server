package com.example.satto.domain.course.entity;

import com.example.satto.domain.users.entity.Users;
import com.example.satto.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_lect_id")
    private PreviousLecture previousLecture;

    public void update(Users user, PreviousLecture previousLecture) {
        this.user = user;
        this.previousLecture = previousLecture;
    }
}
