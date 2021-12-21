package com.navikenz.codingexercises.service;

import java.io.IOException;
import java.util.List;

import com.navikenz.codingexercises.dto.NameContactDTO;
import com.navikenz.codingexercises.model.Contact;

public interface ContactService {
	
	public List<NameContactDTO> listAll();
	
	public void save(Contact contact) throws ClassCastException, ClassNotFoundException, IOException;
	
	public Contact get(Long id);
	
	public List<NameContactDTO> findByName(String name);
	
	public void delete(Long id);
}
