package com.cyber.kitchen.repository;


import com.cyber.kitchen.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    Theme findThemeById(Long id);
}
