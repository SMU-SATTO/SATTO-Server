package com.example.satto.domain.event.repository;

import com.example.satto.domain.event.entity.PhotoContest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoContestRepository extends JpaRepository<PhotoContest, Long> {
}
