package com.novera.noveraserver.controller;

import com.novera.noveraserver.dto.StudentRequest;
import com.novera.noveraserver.dto.StudentResponse;
import com.novera.noveraserver.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
	
	private final StudentService service;
	
	public StudentController(StudentService service) {
		this.service = service;
	}
	
	@GetMapping("/{id}")
	public StudentResponse get(@PathVariable Long id) {
		return service.getById(id);
	}
	
	@PostMapping
	public StudentResponse create(@Valid @RequestBody StudentRequest request) {
		return service.create(request);
	}
	
	@PutMapping("/{id}")
	public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
		return service.update(id, request);
	}
	
	@DeleteMapping("/{id}")
	public void deactivate(@PathVariable Long id) {
		service.deactivate(id);
	}
	
}