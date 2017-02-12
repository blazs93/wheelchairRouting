package com.wheelchair.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(unique = true)
	private String username;
	private String name;
	private String password;
	private boolean active = true;

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public boolean isActive() {
		return active;
	}

	public void setUsername(final String loginName) {
		this.username = loginName;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}
}
