package com.cyber.kitchen.repository;

import com.cyber.kitchen.entity.Achievement;
import com.cyber.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findAchievementById(Long id);

    List<Achievement> findAchievementsByUser(User user);

}
