package com.navikenz.codingexercises.controller;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.navikenz.codingexercises.model.Contact;
import com.navikenz.codingexercises.service.ContactService;
import com.navikenz.codingexercises.util.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin(origins = "*")
@RestController
public class ContactController {

	private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

	@Autowired
	private ContactService service;

	@Operation(tags = "contacts", summary = "Add a new contact to the store", description = "Recognizable named entities should be extracted from both contact notes and comment descriptions and added to the linked entities array (if not already present in the array)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.STATUS_200, description = Constants.HTTP_200_DESC, content = @Content(schema = @Schema(implementation = Contact.class))),
			@ApiResponse(responseCode = Constants.STATUS_405, description = Constants.HTTP_405_DESC),
			@ApiResponse(responseCode = Constants.STATUS_500, description = Constants.HTTP_500_DESC) })
	@PostMapping(value = "/contacts", consumes = Constants.MEDIA_TYPE_JSON, produces = Constants.MEDIA_TYPE_JSON)
	public ResponseEntity<?> add(
			@Parameter(name = "body", description = "Contact object that needs to be added to the store", required = true, schema = @Schema(implementation = Contact.class)) @Validated @RequestBody Contact contact) {
		logger.info("Starting add method...");
		try {
			service.save(contact);
			return new ResponseEntity<>(Constants.HTTP_200_DESC, HttpStatus.OK);
		} catch (ConstraintViolationException e) {
			return new ResponseEntity<>(Constants.HTTP_405_DESC, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (ClassCastException | ClassNotFoundException | IOException e) {
			return new ResponseEntity<>(Constants.HTTP_500_DESC, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("Ending add method...");
		}
	}

	@Operation(tags = "contacts", summary = "Update an existing contact", description = "Recognizable named entities should be extracted from both contact notes and comment descriptions and added to the linked entities array (if not already present in the array)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.STATUS_200, description = Constants.HTTP_200_DESC, content = @Content(schema = @Schema(implementation = Contact.class))),
			@ApiResponse(responseCode = Constants.STATUS_400, description = Constants.HTTP_400_DESC),
			@ApiResponse(responseCode = Constants.STATUS_404, description = Constants.HTTP_404_DESC),
			@ApiResponse(responseCode = Constants.STATUS_405, description = Constants.HTTP_405_DESC),
			@ApiResponse(responseCode = Constants.STATUS_500, description = Constants.HTTP_500_DESC) })
	@PutMapping(value = "/contacts", consumes = Constants.MEDIA_TYPE_JSON, produces = Constants.MEDIA_TYPE_JSON)
	public ResponseEntity<?> update(
			@Parameter(name = "body", description = "Contact object to be updated to the store", required = true, schema = @Schema(implementation = Contact.class)) @Validated @RequestBody Contact contact) {
		logger.info("Starting update method...");
		try {
			Contact existContact = service.get(contact.getId());
			logger.debug(existContact.getFullName());
			service.save(contact);
			return new ResponseEntity<>(Constants.HTTP_200_DESC, HttpStatus.OK);
		} catch (NoSuchElementException | ClassCastException | ClassNotFoundException | IOException e) {
			return new ResponseEntity<>(Constants.HTTP_500_DESC, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("Ending update method...");
		}
	}

	@Operation(tags = "contacts", summary = "Gets the list of contacts", description = "Gets the list of contacts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.STATUS_200, description = Constants.HTTP_200_DESC, content = @Content(array = @ArraySchema(schema = @Schema(implementation = Contact.class)))),
			@ApiResponse(responseCode = Constants.STATUS_500, description = Constants.HTTP_500_DESC) })
	@GetMapping(value = "/contacts", produces = Constants.MEDIA_TYPE_JSON)
	public ResponseEntity<?> list(
			@Parameter(name = "page", in = ParameterIn.QUERY, description = "Page number", required = false) @RequestParam(name = "page", defaultValue = "0", required = false) int page, 
			@Parameter(name = "size", in = ParameterIn.QUERY, description = "Number of element per page", required = false) @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
		logger.info("Starting list method...");
		try {
			return new ResponseEntity<>(service.listAll(page, size), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.HTTP_500_DESC, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("Ending list method...");
		}
	}

	@Operation(tags = "contacts", summary = "Find contact by ID", description = "Returns a single contact")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.STATUS_200, description = Constants.HTTP_200_DESC, content = @Content(schema = @Schema(implementation = Contact.class))),
			@ApiResponse(responseCode = Constants.STATUS_400, description = Constants.HTTP_400_DESC),
			@ApiResponse(responseCode = Constants.STATUS_404, description = Constants.HTTP_404_DESC),
			@ApiResponse(responseCode = Constants.STATUS_500, description = Constants.HTTP_500_DESC) })
	@GetMapping(value = "/contacts/{contactId}", produces = Constants.MEDIA_TYPE_JSON)
	public ResponseEntity<?> get(
			@Parameter(name = "contactId", in = ParameterIn.PATH, description = "ID of the contact to fetch", required = true) @PathVariable Long contactId) {
		logger.info("Starting get method...");
		try {
			Contact contact = service.get(contactId);
			return new ResponseEntity<>(contact, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(Constants.HTTP_404_DESC, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.HTTP_500_DESC, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("Ending get method...");
		}
	}

	@Operation(tags = "contacts", summary = "Gets the list of contacts that match the specified name", description = "If contactName parameter is empty, return all contacts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.STATUS_200, description = Constants.HTTP_200_DESC, content = @Content(array = @ArraySchema(schema = @Schema(implementation = Contact.class)))),
			@ApiResponse(responseCode = Constants.STATUS_500, description = Constants.HTTP_500_DESC) })
	@GetMapping(value = "/contacts/findByName", produces = Constants.MEDIA_TYPE_JSON)
	public ResponseEntity<?> findByName(
			@Parameter(name = "contactName", in = ParameterIn.QUERY, description = "Name of the contact to search for", required = false) @RequestParam(name = "contactName", required = false) Optional<String> contactName,
			@Parameter(name = "page", in = ParameterIn.QUERY, description = "Page number", required = false) @RequestParam(name = "page", defaultValue = "0", required = false) int page, 
			@Parameter(name = "size", in = ParameterIn.QUERY, description = "Number of element per page", required = false) @RequestParam(name = "size", defaultValue = "10", required = false) int size) {
		logger.info("Starting findByName method...");
		try {
			if (contactName.isPresent()) {
				Map<String, Object> contacts = service.findByName(contactName.get(), page, size);
				return new ResponseEntity<>(contacts, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(service.listAll(page, size), HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.HTTP_500_DESC, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("Ending findByName method...");
		}
	}

	@Operation(tags = "contacts", summary = "Deletes a contact")
	@ApiResponses(value = { @ApiResponse(responseCode = Constants.STATUS_400, description = Constants.HTTP_400_DESC),
			@ApiResponse(responseCode = Constants.STATUS_404, description = Constants.HTTP_404_DESC),
			@ApiResponse(responseCode = Constants.STATUS_500, description = Constants.HTTP_500_DESC) })
	@DeleteMapping("/contacts/{contactId}")
	public ResponseEntity<?> delete(
			@Parameter(name = "contactId", in = ParameterIn.PATH, description = "ID of the contact to delete", required = true) @PathVariable Long contactId) {
		logger.info("Starting delete method...");
		try {
			service.delete(contactId);
			return new ResponseEntity<>(Constants.HTTP_200_DESC, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(Constants.HTTP_404_DESC, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(Constants.HTTP_500_DESC, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			logger.info("Ending delete method...");
		}
	}
}
