package com.moodle.analytics.repository;

import com.moodle.analytics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndEnabled(String username, boolean isEnabled);

    User findByEmailAndEnabled(String email, boolean isEnabled);

}