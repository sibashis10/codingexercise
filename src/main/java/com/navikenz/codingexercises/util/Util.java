package com.navikenz.codingexercises.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.navikenz.codingexercises.model.Contact;
import com.navikenz.codingexercises.model.IdentifiedEntity;

public class Util {
	
	public static Set<IdentifiedEntity> populateEntityList(Contact contact) throws ClassCastException, ClassNotFoundException, IOException {
		StringBuilder sb = new StringBuilder(contact.getNotes());
		contact.getComments().forEach(c -> {
			sb.append(c.getText());
		});
		
		Map<String, String> eMap = NERModel.classifyNamedEntity(NERModel.sentenceSplitter(sb.toString()));
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
