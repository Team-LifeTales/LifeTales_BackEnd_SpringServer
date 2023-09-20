package com.LifeTales.domain.user.repository;

import com.LifeTales.domain.user.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findById(String id);

    boolean existsByIdAndIsDELETED(String id , boolean check);
}
