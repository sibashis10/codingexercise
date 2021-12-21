package com.navikenz.codingexercises.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navikenz.codingexercises.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	List<Comment> findByUserIgnoreCaseContaining(String name);

}
