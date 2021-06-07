package com.video.domain;

import java.util.Date;

public class User {
	
	private int userId;
	private String name;
	private String surName;
	private String password;
	private Date registrationDate;
	private static int users = 1;

	public User(String name, String surName, String password, Date registrationDate) {
		this.name = name;
		this.surName = surName;
		this.password = password;
		this.registrationDate = registrationDate;
		
		userId=users;
		users++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int getUserId() {
		return userId;
	}

	public static int getUsers() {
		return users;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", surName=" + surName + ", password=" + password
				+ ", registrationDate=" + registrationDate + "]";
	}
	
}
