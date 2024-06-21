package com.example.satto.domain.follow.service;

import com.example.satto.global.common.BaseResponse;

public interface FollowService {


    BaseResponse<Object> followRequest(String followingId, String userId);

    BaseResponse<Object> acceptFollower(String followerId, String userId);


    BaseResponse<Object> unFollower(String followerId, String studentId);

    BaseResponse<Object> unFollowing(String followingId, String studentId);
}
