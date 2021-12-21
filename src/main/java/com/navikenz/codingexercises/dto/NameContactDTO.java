package com.navikenz.codingexercises.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NameContactDTO implements Serializable {
	
	private static final long serialVersionUID = 2032472778928024333L;
	
	private Long id;
	private String firstName;
	private String lastName;

	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
}
