package de.sboe0705.users.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// "user" is a SQL keyword and cannot be used as table name,
// thus prefixing it with an underscore.
@Table(name = "_user")
public class User {

	@Id
	private String id;

	private String firstName;

	private String lastName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
