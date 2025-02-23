package com.edu.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.sms.entity.School;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
