package com.example.satto.domain.event.entity;

import com.example.satto.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class PhotoContest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoContestId;

    private String photoImg;

    @OneToMany(mappedBy = "photoContest", cascade = CascadeType.ALL)
    List<PhotoContestLike> photoContestLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "photoContest", cascade = CascadeType.ALL)
    List<PhotoContestDislike> photoContestDislikeList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
