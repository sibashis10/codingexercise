package com.navikenz.codingexercises.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.navikenz.codingexercises.dto.NameContactDTO;
import com.navikenz.codingexercises.model.Contact;
import com.navikenz.codingexercises.model.IdentifiedEntity;
import com.navikenz.codingexercises.repository.CommentRepository;
import com.navikenz.codingexercises.repository.ContactRepository;
import com.navikenz.codingexercises.service.ContactService;
import com.navikenz.codingexercises.util.Util;

@Service
@Transactional
public class ContactServiceImpl implements ContactService {

	@Autowired
	ContactRepository contactRepo;
	
	@Autowired
	CommentRepository commentRepo;
	
	@Override
	public Map<String, Object> listAll(int page, int size) {
		
		List<NameContactDTO> contacts = new ArrayList<>();
		Pageable paging = PageRequest.of(page, size);
		
		Page<NameContactDTO> pageContacts = contactRepo.findAllIdAndName(paging);
		
		contacts = pageContacts.getContent();
		
		Map<String, Object> response = new HashMap<>();
		response.put("contacts", contacts);
		response.put("currentPage", pageContacts.getNumber());
		response.put("totalItems", pageContacts.getTotalElements());
		response.put("totalPages", pageContacts.getTotalPages());
		
		return response;
	}

	@Override
	public void save(Contact contact) throws ClassCastException, ClassNotFoundException, IOException {
		Set<IdentifiedEntity> entityList = Util.populateEntityList(contact);
		contact.setIdentifiedEntities(entityList);
		contact.getComments().stream().forEach(c -> {
			c.setContact(contact);
		});
		contact.getIdentifiedEntities().stream().forEach(ie -> {
			ie.setContact(contact);
		});
		contactRepo.save(contact);
	}

	@Override
	public Contact get(Long id) {
		return contactRepo.findById(id).get();
	}

	@Override
	public Map<String, Object> findByName(String name, int page, int size) {
		
		List<NameContactDTO> contacts = new ArrayList<>();
		Pageable paging = PageRequest.of(page, size);
		
		Page<NameContactDTO> pageContacts = contactRepo.findAllNamesMatched(name, paging);
		contacts = pageContacts.getContent();
		
		Map<String, Object> response = new HashMap<>();
		response.put("contacts", contacts);
		response.put("currentPage", pageContacts.getNumber());
		response.put("totalItems", pageContacts.getTotalElements());
		response.put("totalPages", pageContacts.getTotalPages());
		
		return response;
	}

	@Override
	public void delete(Long id) {
		Contact contact = contactRepo.findById(id).get();
		contactRepo.delete(contact);
	}

}
