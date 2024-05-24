package com.example.satto.domain.course.repository;

import com.example.satto.domain.course.entity.Course;
import com.example.satto.domain.course.entity.PreviousLecture;
import com.example.satto.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByUser(Users user);

    Optional<Course> findByUserAndPreviousLecture(Users user, PreviousLecture previousLecture);
}
