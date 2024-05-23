package com.example.satto.domain.course.entity;

import com.example.satto.domain.user.entity.User;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_lect_id")
    private PreviousLecture previousLecture;

    public void setUser(User user) {
        this.user = user;
    }

    public void setPreviousLecture(PreviousLecture previousLecture) {
        this.previousLecture = previousLecture;
    }
}
