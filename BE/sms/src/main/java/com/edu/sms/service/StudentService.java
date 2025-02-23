package com.edu.sms.service;

import org.springframework.stereotype.Service;

import com.edu.sms.constants.StudentStatus;
import com.edu.sms.entity.School;
import com.edu.sms.entity.Student;
import com.edu.sms.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	private final SchoolService schoolService;

	public StudentService(StudentRepository studentRepository, SchoolService schoolService) {
		this.studentRepository = studentRepository;
		this.schoolService = schoolService;
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Optional<Student> getStudentById(Long id) {
		return studentRepository.findById(id);
	}

	public Student addStudent(Student student) {
		student.setSchool(null);
		student.setStatus(StudentStatus.RESTRICTED.name());
		return studentRepository.save(student);
	}

	public Student updateStudent(Long id, Student newData) {
		return studentRepository.findById(id).map(student -> {
			student.setName(newData.getName());
			student.setEmail(newData.getEmail());
			return studentRepository.save(student);
		}).orElseThrow();
	}

	public void deleteStudent(Long id) {
		studentRepository.deleteById(id);
	}

	public void admitStudent(Long studentId, Long schoolId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		if (student.getStatus().equals(StudentStatus.ADMITTED.name())) {
			throw new RuntimeException("Student is already admitted to a school.");
		}

		School school = schoolService.getSchoolById(schoolId);
		student.setSchool(school);
		student.setStatus(StudentStatus.ADMITTED.name());
		studentRepository.save(student);
	}

	public void transferStudent(Long studentId, Long schoolId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		if (student.getSchool() == null) {
			throw new RuntimeException("Student must be admitted first.");
		}

		School newSchool = schoolService.getSchoolById(schoolId);
		student.setSchool(newSchool);
		student.setStatus(StudentStatus.TRANSFERRED.name());
		studentRepository.save(student);
	}

	public Student restrictStudent(Long id) {
		Student student = studentRepository.findById(id).orElseThrow();
		student.setSchool(null);
		student.setStatus(StudentStatus.RESTRICTED.name());
		return studentRepository.save(student);
	}
}
