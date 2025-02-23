package com.edu.sms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.edu.sms.entity.School;
import com.edu.sms.repository.SchoolRepository;

@Service
public class SchoolService {

	private final SchoolRepository schoolRepository;

	public SchoolService(SchoolRepository schoolRepository) {
		this.schoolRepository = schoolRepository;
	}

	public List<School> getAllSchools() {
		List<School> schools = schoolRepository.findAll();
		return schools;
	}

	public School getSchoolById(Long id) {
		return schoolRepository.findById(id).orElseThrow(() -> new RuntimeException("School not found"));
	}

	public School addSchool(School school) {
		return schoolRepository.save(school);
	}
	
	public void deleteSchool(Long id) {
		schoolRepository.deleteById(id);
	}
}
