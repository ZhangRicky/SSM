package com.mark.entity;

import java.io.Serializable;

public class User implements Serializable{
	
	private String username;
	private String password;
	private String idCard;
	private Integer age;
	private String gender;
	
	
	
	public User() {
	}


	public User(String username, String password, String idCard, Integer age, String gender) {
		super();
		this.username = username;
		this.password = password;
		this.idCard = idCard;
		this.age = age;
		this.gender = gender;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getIdCard() {
		return idCard;
	}


	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", idCard=" + idCard + ", age=" + age
				+ ", gender=" + gender + "]";
	}
	
	

}
