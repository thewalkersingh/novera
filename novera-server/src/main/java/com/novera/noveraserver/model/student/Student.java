package com.novera.noveraserver.model.student;

import com.novera.noveraserver.common.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "students")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;
	
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dateOfBirth;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Gender gender;
	
	@Column(length = 100)
	private String email;
	
	@Column(length = 20)
	private String phone;
	
	@Column(nullable = false, length = 20)
	private String grade;
	
	@Column(name = "admission_date", nullable = false)
	private LocalDate admissionDate;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StudentStatus status = StudentStatus.ACTIVE;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
}