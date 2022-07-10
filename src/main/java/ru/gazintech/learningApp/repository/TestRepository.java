package ru.gazintech.learningApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gazintech.learningApp.models.Test;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
}
