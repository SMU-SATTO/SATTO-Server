package com.example.satto.domain.follow.service;

import com.example.satto.global.common.BaseResponse;

public interface FollowService {


    BaseResponse<String> followRequest(String followingId, String userId);

    BaseResponse<?> acceptFollower(String followerId, String userId);


    void unFollower(String followerId, String studentId);

    void unFollowing(String followingId, String studentId);
}
