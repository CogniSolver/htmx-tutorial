package com.edu.sms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.sms.entity.School;
import com.edu.sms.service.SchoolService;

@RestController
@RequestMapping("/schools")
@CrossOrigin("*")
public class SchoolController {

	private final SchoolService schoolService;

	public SchoolController(SchoolService schoolService) {
		this.schoolService = schoolService;
	}

	@GetMapping("/")
	public ResponseEntity<String> getAllSchools() {
	    List<School> schools = schoolService.getAllSchools();

	    StringBuilder html = new StringBuilder("<table>");
	    html.append("<tr><th>ID</th><th>Name</th><th>Location</th><th>Action</th></tr>");

	    for (School school : schools) {
	        html.append("<tr>")
	                .append("<td>").append(school.getId()).append("</td>")
	                .append("<td>").append(school.getName()).append("</td>")
	                .append("<td>").append(school.getAddress()).append("</td>")
	                .append("</td>").append("<td class='btn-container'>")
	                .append("<button class='action-btn delete-btn' hx-delete='http://localhost:8080/schools/delete/").append(school.getId())
	    			.append("' hx-target='#content'>Delete</button>")
	    			.append("</td></tr>");
	    }
	    html.append("</table>");

	    return ResponseEntity.ok(html.toString());
	}

	
	@GetMapping("/dropdown")
    public ResponseEntity<String> getSchoolDropdown() {
        List<School> schools = schoolService.getAllSchools();
        
        StringBuilder dropdown = new StringBuilder("<select id='school-select'>");
        for (School school : schools) {
            dropdown.append("<option value='")
                    .append(school.getId())
                    .append("'>")
                    .append(school.getName())
                    .append("</option>");
        }
        dropdown.append("</select>");

        return ResponseEntity.ok(dropdown.toString());
    }
	
	@PostMapping("/add")
	public ResponseEntity<String> addSchool(@RequestBody School school) {
	    schoolService.addSchool(school);
	    return getAllSchools(); // Return updated school table as HTML
	}
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
		schoolService.deleteSchool(id);
        return getAllSchools();
    }

}
