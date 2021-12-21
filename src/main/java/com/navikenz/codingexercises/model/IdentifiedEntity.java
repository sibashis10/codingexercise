package com.navikenz.codingexercises.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_identified_entity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentifiedEntity implements Serializable {
	
	private static final long serialVersionUID = 2836518213425620561L;
	
	@Id
    @GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(name = "entity_type", nullable = false)
	@NotBlank(message = "Entity Type is mandatory")
	private String entityType;
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_contact_identified_entity"), name = "contact_id")
	@JsonIgnore
    private Contact contact;

	@Override
	public String toString() {
		return "IdentifiedEntity [id=" + id + ", entityType=" + entityType + ", name=" + name + ", contact=" + contact
				+ "]";
	}
	
}
