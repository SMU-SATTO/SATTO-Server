package com.example.satto.domain.event.repository;

import com.example.satto.domain.event.entity.PhotoContestDislike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoContestDislikeRepository extends JpaRepository<PhotoContestDislike, Long> {
}
