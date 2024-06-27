package com.example.satto.domain.follow.service.Impl;

import com.example.satto.domain.follow.entity.Follow;
import com.example.satto.domain.follow.repository.FollowRepository;
import com.example.satto.domain.follow.service.FollowService;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.domain.users.repository.UsersRepository;
import com.example.satto.global.common.BaseResponse;
import com.example.satto.global.common.code.status.ErrorStatus;
import com.example.satto.global.common.exception.handler.UsersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UsersRepository usersRepository;

    @Override
    public BaseResponse<Object> followRequest(String followingId, String studentId) {
        if (followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentId(studentId, followingId) &&
                followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(studentId, followingId, 1)) {
            return BaseResponse.onSuccess("이미 Follow 요청을 보냈습니다.");
        } else {
            // Follower 사용자 조회
            Users followerUser = usersRepository.findByStudentId(studentId);
            if (followerUser == null) {
                return BaseResponse.onFailure("Follower 사용자를 찾을 수 없습니다.");
            }

            // Following 사용자 조회
            Users followingUser = usersRepository.findByStudentId(followingId);
            if (followingUser == null) {
                return BaseResponse.onFailure("Following 사용자를 찾을 수 없습니다.");
            }

            // Follow 엔티티 생성 및 저장
            Follow follow = new Follow();

            follow.setFollowerId(followerUser);
            follow.setFollowingId(followingUser);
            follow.setRequest(1);
            followRepository.save(follow);

        }
        return BaseResponse.onSuccess("Follow 요청을 보냈습니다.");
    }

    @Override
    public Map<String, String> followRequestList(String studentId) {
        // 1. 팔로우 요청을 보낸 사람들의 목록을 가져온다
        List<Follow> followerRequests = followRepository.findByFollowingIdStudentIdAndRequest(studentId, 1);
//        System.out.println("팔로우 요청 목록 크기: " + followerRequests.size());

        Map<String, String> followRequesters = new HashMap<>();
        List<String> candidateStudentId = new ArrayList<>();

        for (Follow candidate1 : followerRequests) {
            candidateStudentId.add(candidate1.toString());
        }

        for (String student : candidateStudentId) {
            Optional<Users> optionalUser = Optional.ofNullable(usersRepository.findByStudentId(student));
            if(optionalUser.isPresent()) {
                Users user = optionalUser.get();
                followRequesters.put(student, user.getName());

            } else {
                throw new UsersHandler(ErrorStatus._NOT_FOUND_USER);
            }
        }
        return followRequesters;
    }

    @Transactional
    @Override
    public BaseResponse<Object> acceptFollower(String followerId, String studentId) {

        Follow followerRequest = followRepository.findByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(followerId, studentId, 1);
        boolean followerRequestExists = followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentIdAndRequest(followerId, studentId, 1);

        if (followerRequestExists) {
            followerRequest.setRequest(2);
            followRepository.save(followerRequest);
            return BaseResponse.onSuccess("Follow 요청을 수락하였습니다.");
        } else {
            return BaseResponse.onFailure("Follow 요청 내역이 없습니다.");
        }

    }

    @Override
    public BaseResponse<Object> unFollower(String followerId, String studentId) {

        Follow follow = followRepository.findByFollowerIdStudentIdAndFollowingIdStudentId(followerId, studentId);
        boolean followerExist = followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentId(followerId, studentId);

        if (followerExist) {
            followRepository.delete(follow);
            return BaseResponse.onSuccess("해당 Follower를 삭제하였습니다.");
        } else {
            return BaseResponse.onFailure("해당 Follower가 존재하지 않습니다.");
        }
    }

    @Override
    public BaseResponse<Object> unFollowing(String followingId, String studentId) {
        Follow follow = followRepository.findByFollowerIdStudentIdAndFollowingIdStudentId(studentId, followingId);
        boolean followingExist = followRepository.existsByFollowerIdStudentIdAndFollowingIdStudentId(studentId, followingId);
        System.out.println("어떤가요"+followingExist);

        if (followingExist) {
            followRepository.delete(follow);
            return BaseResponse.onSuccess("해당 Following을 삭제하였습니다.");
        } else {
            return BaseResponse.onFailure("해당 Following이 존재하지 않습니다.");
        }
    }

}
