package com.example.satto.domain.event.repository;

import com.example.satto.domain.event.entity.PhotoContest;
import com.example.satto.domain.event.entity.PhotoContestDislike;
import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoContestDislikeRepository extends JpaRepository<PhotoContestDislike, Long> {
    Optional<PhotoContestDislike> findByUserAndPhotoContest(Users user, PhotoContest photoContest);
}
