package com.navikenz.codingexercises.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.navikenz.codingexercises.dto.NameContactDTO;
import com.navikenz.codingexercises.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	@Query("SELECT new com.navikenz.codingexercises.dto.NameContactDTO(c.id, c.firstName, c.lastName) FROM Contact c")
	Page<NameContactDTO> findAllIdAndName(Pageable pageable);
	
	@Query("SELECT DISTINCT new com.navikenz.codingexercises.dto.NameContactDTO(c.id, c.firstName, c.lastName) "
			+ "FROM Contact c, Comment cm "
			+ "WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :input, '%')) "
			+ "AND LOWER(c.lastName) LIKE LOWER(CONCAT('%', :input, '%')) "
			+ "OR (c.id = cm.contact.id "
			+ "AND LOWER(cm.user) LIKE LOWER(CONCAT('%', :input, '%')))")
	Page<NameContactDTO> findAllNamesMatched(@Param("input") String input, Pageable pageable);
	
}
