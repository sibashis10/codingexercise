package com.navikenz.codingexercises.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.navikenz.codingexercises.dto.NameContactDTO;
import com.navikenz.codingexercises.model.Comment;
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
	public List<NameContactDTO> listAll() {
		List<NameContactDTO> allContacts = contactRepo.findAllIdAndName();
		allContacts.sort(Comparator.comparing(e -> e.getFullName()));
		return allContacts;
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
	public List<NameContactDTO> findByName(String name) {
		Set<NameContactDTO> contacts = contactRepo.findAllNamesMatched(name);
		List<Comment> comments = commentRepo.findByUserIgnoreCaseContaining(name);
		comments.stream().forEach(c -> {
			Contact contact = contactRepo.findById(c.getContact().getId()).get();
			NameContactDTO nameCon = new NameContactDTO(contact.getId(), contact.getFirstName(), contact.getLastName());
			contacts.add(nameCon);
		});
		List<NameContactDTO> allContacts = new ArrayList<>(contacts);
		allContacts.sort(Comparator.comparing(e -> e.getFullName()));
		return allContacts;
	}

	@Override
	public void delete(Long id) {
		Contact contact = contactRepo.findById(id).get();
		contactRepo.delete(contact);
	}

}
