package com.example.rqchallenge.employees.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Employee {
	private String id;
	@JsonProperty("employee_name")
	@NotBlank
	@NotNull
	private String name;
	@JsonProperty("employee_age")
	@Max(60)
	@Min(21)
	private int age;
	@JsonProperty("employee_salary")
	@Min(0)
	private int salary;
	@JsonProperty("profile_image")
	private String profileImage;

	public Employee() {

	}

	public Employee(String id, String name, int age, int salary) {
		this(id, name, age, salary, "");
	}

	public Employee(String id, String name, int age, int salary, String profileImage) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.salary = salary;
		this.profileImage = profileImage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(@Min(0) int salary) {
		this.salary = salary;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
}
