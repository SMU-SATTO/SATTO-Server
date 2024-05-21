package com.example.satto.domain.event.repository;

import com.example.satto.domain.event.entity.PhotoContestLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoContestLikeRepository extends JpaRepository<PhotoContestLike, Long> {
}
