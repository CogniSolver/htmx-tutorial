package com.edu.sms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.edu.sms.constants.StudentStatus;
import com.edu.sms.entity.Student;
import com.edu.sms.service.StudentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@CrossOrigin("*")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/")
	public ResponseEntity<String> getAllStudents() {
		List<Student> students = studentService.getAllStudents();

		StringBuilder html = new StringBuilder("<table>");
		html.append("<tr><th>ID</th><th>Name</th><th>Status</th><th>School</th><th>Actions</th></tr>");

		for (Student student : students) {
			html.append("<tr>").append("<td>").append(student.getId()).append("</td>").append("<td>")
					.append(student.getName()).append("</td>").append("<td>").append(student.getStatus())
					.append("</td>").append("<td>")
					.append(student.getSchool() != null ? student.getSchool().getName() : "Not Assigned")
					.append("</td>").append("<td class='btn-container'>");

			if (StudentStatus.ADMITTED.name().equalsIgnoreCase(student.getStatus())) {
				html.append("<button class='action-btn transfer-btn' onclick='openModal(").append(student.getId())
						.append(", \"transfer\")'>Transfer</button>");
				html.append("<button class='action-btn restrict-btn' hx-post='http://localhost:8080/students/")
						.append(student.getId()).append("/restrict' hx-target='#content'>Restrict</button>");
			} else {
				html.append("<button class='action-btn admit-btn' onclick='openModal(").append(student.getId())
						.append(", \"admit\")'>Admit</button>");
			}
			
			html.append("<button class='action-btn delete-btn' hx-delete='http://localhost:8080/students/delete/").append(student.getId())
			.append("' hx-target='#content'>Delete</button>");
			html.append("</td></tr>");
		}
		html.append("</table>");

		// Modal with an empty div that will be filled by the backend via HTMX
		html.append("<div id='modal' class='modal'>").append("<div class='modal-content'>")
				.append("<span class='close' onclick='closeModal()'>&times;</span>")
				.append("<h2 id='modal-title'></h2>")
				.append("<div id='school-dropdown' hx-get='http://localhost:8080/schools/dropdown' hx-trigger='load' hx-target='#school-dropdown'></div>")
				.append("<button id='confirm-action' onclick='confirmAction()'>Confirm</button>").append("</div>")
				.append("</div>");

		return ResponseEntity.ok(html.toString());
	}

	@PostMapping("/add")
	public ResponseEntity<String> addStudent(@RequestBody Student student) {
		student.setStatus("restricted"); // New students start as restricted
		studentService.addStudent(student);
		return getAllStudents(); // Return updated student table as HTML
	}

	@PutMapping("/{id}/admit")
	public ResponseEntity<String> admitStudent(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
		studentService.admitStudent(id, requestBody.get("schoolId"));
		return ResponseEntity.ok("Student admitted successfully.");
	}

	@PutMapping("/{id}/transfer")
	public ResponseEntity<String> transferStudent(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
		studentService.transferStudent(id, requestBody.get("schoolId"));
		return ResponseEntity.ok("Student transferred successfully.");
	}

	@PostMapping("/{id}/restrict")
	public ResponseEntity<String> restrictStudent(@PathVariable Long id) {
		studentService.restrictStudent(id);
		return getAllStudents();
	}
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return getAllStudents();
    }

}
