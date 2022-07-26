package ru.gazintech.learningApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gazintech.learningApp.models.Manual;

@Repository
public interface ManualRepository extends JpaRepository<Manual, Long> {
}
