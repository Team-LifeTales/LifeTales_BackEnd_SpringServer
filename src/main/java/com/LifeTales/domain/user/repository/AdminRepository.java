package com.LifeTales.domain.user.repository;

import com.LifeTales.domain.user.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
