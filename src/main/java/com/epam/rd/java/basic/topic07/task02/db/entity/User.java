package com.epam.rd.java.basic.topic07.task02.db.entity;

import java.util.Objects;

public class User {

	private int id;

	private String login;

	private User(String login){ this.login = login; id = 0;}
	public void setId(int id){this.id = id;}
	public static User createUser(String login){
		return new User(login);
	}
	public String getLogin() { return login; }

	public int getId() { return id; }
	@Override
	public String toString() {
		return "User{" +
				"login='" + login + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return login.equals(user.login);
	}

	@Override
	public int hashCode() {
		return Objects.hash(login);
	}
}