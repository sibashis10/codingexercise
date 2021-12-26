package com.navikenz.codingexercises.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navikenz.codingexercises.model.Contact;
import com.navikenz.codingexercises.model.IdentifiedEntity;

public class Util {
	
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	
	public static Set<IdentifiedEntity> populateEntityList(Contact contact) throws ClassCastException, ClassNotFoundException, IOException {
		StringBuilder sb = new StringBuilder(contact.getNotes());
		contact.getComments().forEach(c -> {
			sb.append(c.getText());
		});
		
		Map<String, String> eMap = NERModel.classifyNamedEntity(NERModel.sentenceSplitter(sb.toString()));
		logger.info(eMap.toString());
		Set<IdentifiedEntity> entityList = new HashSet<>();
		eMap.forEach((k, v) -> {
			IdentifiedEntity ie = new IdentifiedEntity();
			ie.setName(k);
			ie.setEntityType(v);
			ie.setContact(contact);
			entityList.add(ie);
		});
		
		return entityList;
	}

}
