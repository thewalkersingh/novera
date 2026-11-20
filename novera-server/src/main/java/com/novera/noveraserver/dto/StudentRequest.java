package com.novera.noveraserver.dto;

import com.novera.noveraserver.common.enums.Gender;
import com.novera.noveraserver.model.student.StudentStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

/** Body for create (POST) and full update (PUT). {@code status} is optional (defaults to ACTIVE on create). */
@Getter
public class StudentRequest {
	
	@NotBlank
	@Size(max = 100)
	private String firstName;
	
	@NotBlank
	@Size(max = 100)
	private String lastName;
	
	@NotNull
	@Past
	private LocalDate dateOfBirth;
	
	@NotNull
	private Gender gender;
	
	@Email
	@Size(max = 255)
	private String email;
	
	@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone must be 7-15 digits, optionally starting with +")
	private String phone;
	
	@NotBlank
	@Size(max = 20)
	private String grade;
	
	@NotNull
	private LocalDate admissionDate;
	
	private StudentStatus status;
	
}