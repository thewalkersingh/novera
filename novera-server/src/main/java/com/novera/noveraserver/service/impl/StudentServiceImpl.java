package com.novera.noveraserver.service.impl;
import com.novera.noveraserver.dto.StudentRequest;
import com.novera.noveraserver.dto.StudentResponse;
import com.novera.noveraserver.exception.ResourceNotFoundException;
import com.novera.noveraserver.mapper.StudentMapper;
import com.novera.noveraserver.model.student.Student;
import com.novera.noveraserver.model.student.StudentStatus;
import com.novera.noveraserver.repository.StudentRepository;
import com.novera.noveraserver.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
	
	private final StudentRepository repository;
	
	public StudentServiceImpl(StudentRepository repository) {
		this.repository = repository;
	}
	
	public StudentResponse getById(Long id) {
		log.info("Getting student with id " + id);
		return repository.findById(id)
							  .map(StudentMapper::toResponse)
							  .orElseThrow(() -> new ResourceNotFoundException("Student", id));
	}
	
	@Transactional
	public StudentResponse create(StudentRequest request) {
		log.info("Creating student");
		Student saved = repository.saveAndFlush(StudentMapper.toEntity(request));
		return StudentMapper.toResponse(saved);
	}
	
	@Transactional
	public StudentResponse update(Long id, StudentRequest request) {
		Student student = findOrThrow(id);
		StudentMapper.apply(student, request);
		return StudentMapper.toResponse(repository.saveAndFlush(student));
	}
	
	/** Soft delete: the record is kept and marked INACTIVE. */
	@Transactional
	public void deactivate(Long id) {
		Student student = findOrThrow(id);
		student.setStatus(StudentStatus.INACTIVE);
		repository.saveAndFlush(student);
	}
	
	/** Helper method to find a student by ID or throw a ResourceNotFoundException if not found. */
	private Student findOrThrow(Long id) {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student", id));
	}
	
}