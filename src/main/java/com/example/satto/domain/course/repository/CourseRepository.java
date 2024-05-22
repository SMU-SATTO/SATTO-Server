package com.example.satto.domain.course.repository;

import com.example.satto.domain.course.entity.Course;
import com.example.satto.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByUser(User user);
}
