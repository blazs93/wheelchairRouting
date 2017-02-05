package com.wheelchair.db.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "user_role")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userRoleId;

	@ManyToOne
	@Cascade({CascadeType.ALL})
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	public long getUserRoleId() {
		return userRoleId;
	}

	public User getUser() {
		return user;
	}

	public Role getRole() {
		return role;
	}

	public void setUserRoleId(final long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setRole(final Role role) {
		this.role = role;
	}
}