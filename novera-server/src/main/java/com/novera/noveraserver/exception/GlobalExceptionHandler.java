package com.novera.noveraserver.exception;

import com.novera.noveraserver.common.api.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
		}
		return build(HttpStatus.BAD_REQUEST, "Validation failed", request, fieldErrors);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
		return build(HttpStatus.BAD_REQUEST, "Malformed or unreadable request body '" + ex.getMessage() + "'", request,
			Map.of());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
		HttpServletRequest request) {
		return build(HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + ex.getName() + "'", request, Map.of());
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleConflict(DataIntegrityViolationException ex, HttpServletRequest request) {
		log.warn("Data integrity violation on {}: {}", request.getRequestURI(),
			ex.getMostSpecificCause().getMessage());
		return build(HttpStatus.CONFLICT, "Request conflicts with existing data", request, Map.of());
	}
	
	/**
	 * Catch-all. Spring's own web exceptions (404 for unknown URLs, 405, 415, ...) already carry
	 * the right status, so we keep it instead of turning them into a 500.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest request) {
		if (ex instanceof org.springframework.web.ErrorResponse framework) {
			String detail = framework.getBody().getDetail();
			return build(framework.getStatusCode(), detail != null ? detail : "Request failed", request, Map.of());
		}
		log.error("Unhandled exception on {}", request.getRequestURI(), ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again later.", request,
			Map.of());
	}
	
	private ResponseEntity<ApiError> build(HttpStatusCode status, String message,
		HttpServletRequest request, Map<String, String> fieldErrors) {
		HttpStatus resolved = HttpStatus.resolve(status.value());
		String reason = resolved != null ? resolved.getReasonPhrase() : "Error";
		ApiError body = new ApiError(false, status.value(), reason, message,
			request.getRequestURI(), fieldErrors, Instant.now());
		return ResponseEntity.status(status).body(body);
	}
	
}