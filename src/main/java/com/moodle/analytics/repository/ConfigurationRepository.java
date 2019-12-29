package com.moodle.analytics.repository;

import com.moodle.analytics.entity.Configuration;
import com.moodle.analytics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

}