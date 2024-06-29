package com.example.satto.domain.users.service.Impl;

import com.example.satto.config.security.token.TokenRepository;
import com.example.satto.domain.follow.entity.Follow;
import com.example.satto.domain.follow.repository.FollowRepository;
import com.example.satto.domain.mail.dto.EmailRequestDTO;
import com.example.satto.domain.users.dto.UsersRequestDTO;
import com.example.satto.domain.users.entity.Users;
import com.example.satto.domain.users.repository.UsersRepository;
import com.example.satto.domain.users.service.UsersService;
import com.example.satto.global.common.code.status.ErrorStatus;
import com.example.satto.global.common.exception.handler.UsersHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final FollowRepository followRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) {
                return usersRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };

    }

    @Override
    public Object viewFollowerList(String studentId) {
        Map<String, String> followerMap = new HashMap<>();
        Optional<Users> optionalUser = usersRepository.findByStudentId(studentId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            for (Follow followerId : user.getFollowingList()) {
                if ((followerId.getRequest() == 2) && (!followerId.getFollowingId().equals(user.getStudentId()))) {
                    followerMap.put(followerId.getFollowerId().getStudentId(), followerId.getFollowerId().getName());
//                    followerList.add(followerId.getFollowerId().getEmail());
                }
            }
            return followerMap;
        } else {
            return new UsersHandler(ErrorStatus._NOT_FOUND_USER);
        }
    }

    @Override
    public Object viewFollowingList(String studentId) {
        Map<String, String> followingMap = new HashMap<>();
        Optional<Users> optionalUser = usersRepository.findByStudentId(studentId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            for (Follow followingId : user.getFollowerList()) {
                if ((followingId.getRequest() == 2) && (!followingId.getFollowerId().equals(user.getStudentId()))) {
                    followingMap.put(followingId.getFollowingId().getStudentId(), followingId.getFollowingId().getName());
//                    followingList.add(followingId.getFollowingId().getStudentId());
                }
            }
            return followingMap;
        } else {
            return new UsersHandler(ErrorStatus._NOT_FOUND_USER);
        }
    }

    @Override
    public Object followerListNum(String studentId) {
        List<String> followerList = new ArrayList<>();
        Optional<Users> optionalUser = usersRepository.findByStudentId(studentId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            for (Follow followerId : user.getFollowingList()) {
                if ((followerId.getRequest() == 2) && (!followerId.getFollowingId().equals(user.getStudentId()))) {
                    followerList.add(followerId.getFollowerId().getEmail());
                }
            }
            return followerList;
        } else {
            return new UsersHandler(ErrorStatus._NOT_FOUND_USER);
        }

    }

    @Override
    public Object followingListNum(String studentId) {
        List<String> followingList = new ArrayList<>();
        Optional<Users> optionalUser = usersRepository.findByStudentId(studentId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            for (Follow followingId : user.getFollowerList()) {
                if ((followingId.getRequest() == 2) && (!followingId.getFollowerId().equals(user.getStudentId()))) {
                    followingList.add(followingId.getFollowingId().getStudentId());
                }
            }
            return followingList;
        } else {
            return new UsersHandler(ErrorStatus._NOT_FOUND_USER);
        }

    }

    @Override
    public Users userProfile(Long userId) {
        return usersRepository.findById(userId).orElseThrow(() -> new UsersHandler(ErrorStatus._NOT_FOUND_USER));
    }

    @Override
    public void privateAccount(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        user.setPublic(false);
        usersRepository.save(user);
    }

    @Override
    public void publicAccount(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        user.setPublic(true);
        usersRepository.save(user);
    }

    @Override
    public Users updateAccount(UsersRequestDTO.UpdateUserDTO updateUserDTO, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();

        user.setName(updateUserDTO.getName());
        user.setNickname(updateUserDTO.getNickname());
        user.setDepartment(updateUserDTO.getDepartment());
        user.setGrade(updateUserDTO.getGrade());

        usersRepository.save(user);
        return user;
}

    @Override
    public boolean emailDuplicate(String email) {
        return (usersRepository.existsByEmail(email));

    }

    @Override
    public Users uploadProfileImg(String url, String email) {
        Optional<Users> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setProfileImg(url);

            return usersRepository.save(user);
        } else {
            return null;
        }
    }

    @Override
    public boolean nicknameDuplicate(String nickname) {
        return (usersRepository.existsByNickname(nickname));
    }

    @Override
    @Transactional
    public void withdrawal(Users user) {
        Long userId = user.getUserId();
        followRepository.deleteByFollowingId(user);
        followRepository.deleteByFollowerId(user);
        usersRepository.deleteById(userId);

    }

    @Override
    public Users beforeUpdateInformation(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();

        Users information = new Users();
        information.setName(user.getUsername());
        information.setNickname(user.getNickname());
        information.setDepartment(user.getDepartment());
        information.setGrade(user.getGrade());

        return information;

    }

    @Override
    public boolean studentIdDuplicate(EmailRequestDTO.EmailCheckRequest emailCheckRequest) {
        return usersRepository.existsByStudentId(emailCheckRequest.getStudentId());
    }

    // 상대방 프로필 페이지 방문시 사용하는 함수
    @Override
    public Users userProfile(String studentId) {
        return usersRepository.findByStudentId(studentId).orElseThrow(() -> new UsersHandler(ErrorStatus._NOT_FOUND_USER));
    }

    @Override
    public void resetPassword(UsersRequestDTO.UpdateUserPasswordDTO updateUserPasswordDTO, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new UsersHandler(ErrorStatus._NOT_FOUND_USER));
        user.setPassword(passwordEncoder.encode(updateUserPasswordDTO.getPassword()));
        usersRepository.save(user);
    }

}
