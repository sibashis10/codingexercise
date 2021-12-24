package com.navikenz.codingexercises.service;

import java.io.IOException;
import java.util.Map;

import com.navikenz.codingexercises.model.Contact;

public interface ContactService {
	
	public Map<String, Object> listAll(int page, int size);
	
	public void save(Contact contact) throws ClassCastException, ClassNotFoundException, IOException;
	
	public Contact get(Long id);
	
	public Map<String, Object> findByName(String name, int page, int size);
	
	public void delete(Long id);
}
