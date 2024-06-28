package com.example.satto.domain.users.repository;

import com.example.satto.domain.event.entity.PhotoContest;
import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {


    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    Users findByPhotoContest(PhotoContest photoContest);

    boolean existsByNickname(String nickname);

    boolean existsByStudentId(String studentId);

    Optional<Users> findByStudentId(String studentId);

    Users findByStudentId(Users studentId);
}

