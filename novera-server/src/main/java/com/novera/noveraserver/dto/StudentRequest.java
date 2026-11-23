package com.novera.noveraserver.dto;

import com.novera.noveraserver.common.enums.Gender;
import com.novera.noveraserver.model.student.StudentStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/** Body for create (POST) and full update (PUT). {@code status} is optional (defaults to ACTIVE on create). */
@Getter
@Setter
public class StudentRequest {
	
	@NotBlank(message = "First name is required")
	@Size(max = 100, message = "First name must not exceed 100 characters")
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 100, message = "Last name must not exceed 100 characters")
	private String lastName;
	
	@NotNull(message = "Date of birth is required")
	@Past(message = "Date of birth must be in the past")
	private LocalDate dateOfBirth;
	
	@NotNull(message = "Gender is required")
	private Gender gender;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Size(max = 100, message = "Email must not exceed 100 characters")
	private String email;
	
	@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone must be 7-15 digits, optionally starting with +")
	private String phone;
	
	@NotBlank(message = "Grade is required")
	@Size(max = 20, message = "Grade must not exceed 20 characters")
	private String grade;
	
	@NotNull
	@NotBlank(message = "Admission date is required")
	@Past(message = "Admission date must be in the past")
	private LocalDate admissionDate;
	
	private StudentStatus status;
	
}