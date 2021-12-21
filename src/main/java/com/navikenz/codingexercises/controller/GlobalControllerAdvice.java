package com.navikenz.codingexercises.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.navikenz.codingexercises.bean.ErrorMessage;
import com.navikenz.codingexercises.bean.Problem;

@ControllerAdvice
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class GlobalControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerAdvice.class);

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(code = INTERNAL_SERVER_ERROR)
	public ResponseEntity<Problem> problem(final Throwable e) {
		String message = e.getMessage();

		message = "Problem occured";
		UUID uuid = UUID.randomUUID();
		String logRef = uuid.toString();
		logger.error("logRef=" + logRef, message, e);
		return new ResponseEntity<Problem>(new Problem(logRef, message), INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
		String error;
		for (FieldError fieldError : fieldErrors) {
			error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
			errors.add(error);
		}
		for (ObjectError objectError : globalErrors) {
			error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
			errors.add(error);
		}
		ErrorMessage errorMessage = new ErrorMessage(errors);

		return new ResponseEntity<ErrorMessage>(errorMessage, METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleConstraintViolatedException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

		List<String> errors = new ArrayList<>(constraintViolations.size());
		String error;
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			error = constraintViolation.getMessage();
			errors.add(error);
		}

		ErrorMessage errorMessage = new ErrorMessage(errors);
		return new ResponseEntity<ErrorMessage>(errorMessage, METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(code = BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleMissingServletRequestParameterException(
			MissingServletRequestParameterException ex) {

		List<String> errors = new ArrayList<>();
		String error = ex.getParameterName() + ", " + ex.getMessage();
		errors.add(error);
		ErrorMessage errorMessage = new ErrorMessage(errors);
		return new ResponseEntity<ErrorMessage>(errorMessage, BAD_REQUEST);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(code = UNSUPPORTED_MEDIA_TYPE)
	public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
		String unsupported = "Unsupported content type: " + ex.getContentType();
		String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
		ErrorMessage errorMessage = new ErrorMessage(unsupported, supported);
		return new ResponseEntity<ErrorMessage>(errorMessage, UNSUPPORTED_MEDIA_TYPE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(code = BAD_REQUEST)
	public ResponseEntity<ErrorMessage> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		Throwable mostSpecificCause = ex.getMostSpecificCause();
		ErrorMessage errorMessage;
		if (mostSpecificCause != null) {
			String exceptionName = mostSpecificCause.getClass().getName();
			String message = mostSpecificCause.getMessage();
			errorMessage = new ErrorMessage(exceptionName, message);
		} else {
			errorMessage = new ErrorMessage(ex.getMessage());
		}
		return new ResponseEntity<ErrorMessage>(errorMessage, BAD_REQUEST);
	}

}
