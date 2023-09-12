package com.LifeTales.domain.point.Repository;

import com.LifeTales.domain.point.domain.Point;
import com.LifeTales.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    boolean existsByUser(User user);
    Point findByUser(User user);
}
