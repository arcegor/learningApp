package ru.gazintech.learningApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gazintech.learningApp.models.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
}
