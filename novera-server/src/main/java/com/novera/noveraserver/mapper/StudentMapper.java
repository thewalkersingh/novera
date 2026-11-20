package com.novera.noveraserver.mapper;

import com.novera.noveraserver.dto.StudentRequest;
import com.novera.noveraserver.dto.StudentResponse;
import com.novera.noveraserver.model.student.Student;
import com.novera.noveraserver.model.student.StudentStatus;

public final class StudentMapper {
	
	private StudentMapper() {
	}
	
	public static Student toEntity(StudentRequest request) {
		Student student = new Student();
		apply(student, request);
		student.setStatus(request.getStatus() != null ? request.getStatus() : StudentStatus.ACTIVE);
		return student;
	}
	
	/** Copies request values onto the entity. Status changes only when the request carries one. */
	public static void apply(Student student, StudentRequest request) {
		student.setFirstName(request.getFirstName().trim());
		student.setLastName(request.getLastName().trim());
		student.setDateOfBirth(request.getDateOfBirth());
		student.setGender(request.getGender());
		student.setEmail(blankToNull(request.getEmail()));
		student.setPhone(blankToNull(request.getPhone()));
		student.setGrade(request.getGrade().trim());
		student.setAdmissionDate(request.getAdmissionDate());
		if (request.getStatus() != null) {
			student.setStatus(request.getStatus());
		}
	}
	
	public static StudentResponse toResponse(Student s) {
		StudentResponse response = new StudentResponse();
		response.setId(s.getId());
		response.setFirstName(s.getFirstName());
		response.setLastName(s.getLastName());
		response.setDateOfBirth(s.getDateOfBirth());
		response.setGender(s.getGender());
		response.setEmail(s.getEmail());
		response.setPhone(s.getPhone());
		response.setGrade(s.getGrade());
		response.setAdmissionDate(s.getAdmissionDate());
		response.setStatus(s.getStatus());
		response.setCreatedAt(s.getCreatedAt());
		response.setUpdatedAt(s.getUpdatedAt());
		return response;
	}
	
	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}
	
}