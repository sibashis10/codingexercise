package com.navikenz.codingexercises.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.navikenz.codingexercises.dto.NameContactDTO;
import com.navikenz.codingexercises.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	@Query("SELECT new com.navikenz.codingexercises.dto.NameContactDTO(c.id, c.firstName, c.lastName) FROM Contact c")
	List<NameContactDTO> findAllIdAndName();
	
	@Query("SELECT new com.navikenz.codingexercises.dto.NameContactDTO(c.id, c.firstName, c.lastName) "
			+ "FROM Contact c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :input, '%')) AND LOWER(c.lastName) LIKE LOWER(CONCAT('%', :input, '%')) ")
	Set<NameContactDTO> findAllNamesMatched(@Param("input") String input);
	
}
