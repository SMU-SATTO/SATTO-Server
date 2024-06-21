package com.example.satto.domain.follow.service.impl;

import com.example.satto.domain.follow.entity.Follow;
import com.example.satto.domain.follow.repository.FollowRepository;
import com.example.satto.domain.follow.service.FollowService;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    public BaseResponse<String> followRequest(String followingId, String studentId) {
        if (followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentId(studentId, followingId) &&
                followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(studentId, followingId, 1)) {

            return BaseResponse.onSuccess("이미 팔로우");
        } else {
            Users followerUser = new Users();
            followerUser.setStudentId(studentId);

            Users followingUser = new Users();
            followingUser.setStudentId(followingId);

            Follow follow = new Follow();
            follow.setFollowerId(followerUser);
            follow.setFollowingId(followingUser);
            follow.setRequest(1);
            followRepository.save(follow);
        }
        return null;
    }

    @Transactional
    @Override
    public BaseResponse<?> acceptFollower(String followerId, String studentId) {

        Follow followerRequest = followRepository.findByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(followerId, studentId, 1);
        if (followerRequest != null){
            followerRequest.setRequest(2);
                followRepository.save(followerRequest);
        } else {
            return BaseResponse.onFailure("200", "요청 내역이 없습니다.", "");
        }
        return null;
    }

    @Override
    public void unFollower(String followerId, String studentId) {

        Follow follow = followRepository.findByFollowerIdStudentIdAndFollowingIdStudentId(followerId, studentId);
        if (follow != null) {
            followRepository.delete(follow);
        } else {
            System.out.println("유저가 없습니다.");
        }
    }

    @Override
    public void unFollowing(String followingId, String studentId) {
        Follow follow = followRepository.findByFollowerIdStudentIdAndFollowingIdStudentId(studentId, followingId);
        if (follow != null) {
            followRepository.delete(follow);
        } else {
            System.out.println("유저가 없습니다");
        }
    }


}
