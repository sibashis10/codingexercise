package com.navikenz.codingexercises.model;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_contact")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class Contact implements Serializable {
	
	private static final long serialVersionUID = 1348622848492802037L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(name = "first_name", nullable = false)
	@NotBlank(message = "First Name is mandatory")
	private String firstName;
	@Column(name = "last_name", nullable = false)
	@NotBlank(message = "Last Name is mandatory")
	private String lastName;
	@Transient
	@Setter(value = NONE)
	private String fullName;
	@Column(name = "email", unique = true, nullable = false)
	@NotBlank(message = "Email is mandatory")
	@Email
	private String email;
	@Column(name = "phone")
	private String phone;
	@Column(name = "notes")
	private String notes;
	@CreationTimestamp
	@Column(name = "created_on", nullable = false, updatable = false, insertable = false)
	private Timestamp createdOn;
	@UpdateTimestamp
	@Column(name = "updated_on", nullable = false, updatable = false, insertable = false)
	private Timestamp updatedOn;

	@OneToMany(fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
	@JoinColumn(name = "contact_id")
	@OrderBy(value = "id asc")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Set<Comment> comments;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
	@JoinColumn(name = "contact_id", referencedColumnName = "id")
	@OrderBy(value = "id asc")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Set<IdentifiedEntity> identifiedEntities;

	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", fullName=" + fullName
				+ ", email=" + email + ", phone=" + phone + ", notes=" + notes + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", comments=" + comments + ", identifiedEntities=" + identifiedEntities
				+ "]";
	}
	
}
