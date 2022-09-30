package com.example.rqchallenge.employees.model;

import java.util.Collection;

public class EmployeesResponse {
	private String status;
	private Collection<Employee> data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Collection<Employee> getData() {
		return data;
	}

	public void setData(Collection<Employee> data) {
		this.data = data;
	}
}
