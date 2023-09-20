package com.LifeTales.domain.user.repository;

import com.LifeTales.domain.family.domain.Family;
import com.LifeTales.domain.user.domain.User;
import com.LifeTales.domain.user.repository.DAO.admin.DeletedUserDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(String id);
    boolean existsByEmail(String email);
    User findSeqById(String id);
    List<User> findByIdContaining(String id);
    User findById(String id);

    Page<User> findByIsDELETED(boolean isDeleted, Pageable pageable);

}
