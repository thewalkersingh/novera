package com.novera.noveraserver.student;

import com.jayway.jsonpath.JsonPath;
import com.novera.noveraserver.TestcontainersConfiguration;
import com.novera.noveraserver.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class StudentApiIT {

	private static final String BASE = "/api/v1/students";

	private static final String AARAV = """
		{"firstName":"Aarav","lastName":"Sharma","dateOfBirth":"2014-05-12","gender":"MALE",
		 "email":"aarav@example.com","phone":"9876543210","grade":"5-A","admissionDate":"2024-06-15"}
		""";

	private static final String DIYA = """
		{"firstName":"Diya","lastName":"Patel","dateOfBirth":"2013-09-03","gender":"FEMALE",
		 "email":"diya@example.com","phone":"+919812345678","grade":"6-B","admissionDate":"2023-06-10"}
		""";

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private StudentRepository studentRepository;

	@BeforeEach
	void cleanDatabase() {
		studentRepository.deleteAll();
	}

	@Test
	void create_returns201_withLocationAndActiveStatus() throws Exception {
		mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(AARAV))
				 .andExpect(status().isCreated())
				 .andExpect(header().exists("Location"))
				 .andExpect(jsonPath("$.success").value(true))
				 .andExpect(jsonPath("$.data.id").isNumber())
				 .andExpect(jsonPath("$.data.firstName").value("Aarav"))
				 .andExpect(jsonPath("$.data.status").value("ACTIVE"));
	}

	@Test
	void create_withInvalidBody_returns400_withFieldErrors() throws Exception {
		String invalid = """
			{"firstName":"","lastName":"Sharma","dateOfBirth":"2999-01-01","gender":"MALE",
			 "email":"not-an-email","grade":"5-A","admissionDate":"2024-06-15"}
			""";

		mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(invalid))
				 .andExpect(status().isBadRequest())
				 .andExpect(jsonPath("$.success").value(false))
				 .andExpect(jsonPath("$.fieldErrors.firstName").exists())
				 .andExpect(jsonPath("$.fieldErrors.dateOfBirth").exists())
				 .andExpect(jsonPath("$.fieldErrors.email").exists());
	}

	@Test
	void create_withUnknownGender_returns400() throws Exception {
		String badEnum = AARAV.replace("MALE", "ROBOT");

		mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(badEnum))
				 .andExpect(status().isBadRequest())
				 .andExpect(jsonPath("$.success").value(false));
	}

	@Test
	void get_returnsStudent_andUnknownIdReturns404() throws Exception {
		long id = createStudent(AARAV);

		mockMvc.perform(get(BASE + "/" + id))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.data.lastName").value("Sharma"));

		mockMvc.perform(get(BASE + "/999999"))
				 .andExpect(status().isNotFound())
				 .andExpect(jsonPath("$.success").value(false))
				 .andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void list_supportsSearch_gradeFilter_andPaging() throws Exception {
		createStudent(AARAV);
		createStudent(DIYA);

		mockMvc.perform(get(BASE))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.data.totalElements").value(2))
				 .andExpect(jsonPath("$.data.content[0].lastName").value("Patel")); // sorted by last name

		mockMvc.perform(get(BASE).param("q", "aar"))
				 .andExpect(jsonPath("$.data.totalElements").value(1))
				 .andExpect(jsonPath("$.data.content[0].firstName").value("Aarav"));

		mockMvc.perform(get(BASE).param("grade", "6-B"))
				 .andExpect(jsonPath("$.data.totalElements").value(1))
				 .andExpect(jsonPath("$.data.content[0].firstName").value("Diya"));

		mockMvc.perform(get(BASE).param("size", "1").param("page", "1"))
				 .andExpect(jsonPath("$.data.content.length()").value(1))
				 .andExpect(jsonPath("$.data.totalPages").value(2));
	}

	@Test
	void list_withInvalidPageSizeOrSortField_returns400() throws Exception {
		mockMvc.perform(get(BASE).param("size", "1000")).andExpect(status().isBadRequest());
		mockMvc.perform(get(BASE).param("sortBy", "PASSWORD")).andExpect(status().isBadRequest());
	}

	@Test
	void update_replacesFields() throws Exception {
		long id = createStudent(AARAV);
		String changed = AARAV.replace("5-A", "6-A").replace("Aarav", "Aarush");

		mockMvc.perform(put(BASE + "/" + id).contentType(MediaType.APPLICATION_JSON).content(changed))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.data.firstName").value("Aarush"))
				 .andExpect(jsonPath("$.data.grade").value("6-A"));
	}

	@Test
	void delete_deactivatesStudent_butKeepsRecord() throws Exception {
		long id = createStudent(AARAV);

		mockMvc.perform(delete(BASE + "/" + id))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.success").value(true));

		mockMvc.perform(get(BASE + "/" + id))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.data.status").value("INACTIVE"));

		mockMvc.perform(get(BASE).param("status", "ACTIVE"))
				 .andExpect(jsonPath("$.data.totalElements").value(0));
	}

	private long createStudent(String json) throws Exception {
		String body = mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json))
									.andExpect(status().isCreated())
									.andReturn().getResponse().getContentAsString();
		return ((Number) JsonPath.read(body, "$.data.id")).longValue();
	}

}