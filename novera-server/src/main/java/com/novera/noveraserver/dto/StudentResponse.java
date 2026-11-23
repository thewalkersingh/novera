package com.novera.noveraserver.dto;

import com.novera.noveraserver.common.enums.Gender;
import com.novera.noveraserver.model.student.StudentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Setter
@Getter
public class StudentResponse {
	
	private Long id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private Gender gender;
	private String email;
	private String phone;
	private String grade;
	private LocalDate admissionDate;
	private StudentStatus status;
	private Instant createdAt;
	private Instant updatedAt;
	
}