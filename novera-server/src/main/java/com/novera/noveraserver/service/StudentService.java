package com.novera.noveraserver.service;

import com.novera.noveraserver.dto.StudentRequest;
import com.novera.noveraserver.dto.StudentResponse;

public interface StudentService {
	
	StudentResponse getById(Long id);
	
	StudentResponse create(StudentRequest request);
	
	StudentResponse update(Long id, StudentRequest request);
	
	void deactivate(Long id);
	
}