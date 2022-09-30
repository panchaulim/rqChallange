package com.example.rqchallenge.employees.model;

public class CreateEmployeeResponse {
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public CreateEmployeeDto getData() {
		return data;
	}

	public void setData(CreateEmployeeDto data) {
		this.data = data;
	}

	private CreateEmployeeDto data;
}
