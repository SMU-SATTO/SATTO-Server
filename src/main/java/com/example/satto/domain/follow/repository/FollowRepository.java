package com.example.satto.domain.follow.repository;

import com.example.satto.domain.follow.entity.Follow;
import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerIdStudentIdAndFollowingIdStudentId(String studentId, String followingId);

    boolean existsByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(String studentId, String followingId, int i);

    Follow findByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(String followerId, String studentId, int i);

    Follow findByFollowerIdStudentIdAndFollowingIdStudentId(String followerId, String studentId);

    @Transactional
    void deleteByFollowingId(Users followingId);

    @Transactional
    void deleteByFollowerId(Users followerId);
    List<Follow> findByFollowingIdStudentIdAndRequest(String studentId, int i);

    // Fetch Join : followRequestList 최적화
    @Query("SELECT f FROM Follow f JOIN FETCH f.followerId WHERE f.followingId.studentId = :studentId AND f.request = :request")
    List<Follow> findFollowRequestWithUserByFollowingId(@Param("studentId") String studentId, @Param("request") int request);

//    boolean existsByFollowerIdStudentIdAndFollowingIdStudentId(String followingId, String studentId);
}
