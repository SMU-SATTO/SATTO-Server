package com.example.satto.domain.event.repository;

import com.example.satto.domain.event.entity.PhotoContest;
import com.example.satto.domain.event.entity.PhotoContestLike;
import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoContestLikeRepository extends JpaRepository<PhotoContestLike, Long> {
    Optional<PhotoContestLike> findByUserAndPhotoContest(Users user, PhotoContest photoContest);
}
