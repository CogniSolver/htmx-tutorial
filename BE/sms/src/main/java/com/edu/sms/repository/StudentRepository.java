package com.edu.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edu.sms.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findBySchoolId(Long schoolId);
}
