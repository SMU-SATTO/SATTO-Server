package com.example.satto.domain.follow.repository;

import com.example.satto.domain.follow.entity.Follow;
import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdStudentIdAndFollowingIdStudentId(String studentId, String followingId);

    boolean existsByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(String studentId, String followingId, int i);

    Follow findByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(String followerId, String studentId, int i);

    Follow findByFollowerIdStudentIdAndFollowingIdStudentId(String followerId, String studentId);

    @Transactional
    void deleteByFollowingId(Users followingId);

    @Transactional
    void deleteByFollowerId(Users followerId);
}
