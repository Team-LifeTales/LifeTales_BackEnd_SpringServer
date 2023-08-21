package com.LifeTales.domain.test.repository;

import com.LifeTales.domain.test.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifeTalesTestRepository extends JpaRepository<Test, Long> {
    Test findByName(String name);
}
