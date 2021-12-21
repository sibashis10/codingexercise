package com.navikenz.codingexercises.model;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
	
	private static final long serialVersionUID = 215243923368327414L;
	
	@Id
    @GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(name = "text")
    private String text;
	@Column(name = "user", nullable = false)
	@NotBlank(message = "No valid user provided")
    private String user;
	@CreationTimestamp
	@Column(name = "timestamp", nullable = false, updatable = false, insertable = false)
	private Timestamp timestamp;
    
    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_contact_comment"), name = "contact_id")
    @JsonIgnore
    private Contact contact;

	@Override
	public String toString() {
		return "Comment [id=" + id + ", text=" + text + ", user=" + user + ", timestamp=" + timestamp + ", contact="
				+ contact + "]";
	}
    
}
