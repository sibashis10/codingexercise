package com.navikenz.codingexercises.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navikenz.codingexercises.model.IdentifiedEntity;

public interface IdentifiedEntityRepository extends JpaRepository<IdentifiedEntity, Long> {

}
