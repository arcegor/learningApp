package ru.gazintech.learningApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gazintech.learningApp.models.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
