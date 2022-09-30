package com.example.rqchallenge.employees.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.rqchallenge.employees.model.CreateEmployeeDto;
import com.example.rqchallenge.employees.model.CreateEmployeeResponse;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import com.example.rqchallenge.employees.model.EmployeesResponse;

@FeignClient(name = "employees", url = "https://dummy.restapiexample.com", configuration = EmployeesClientConfguration.class)
public interface IEmployeeServiceProxy {
	@GetMapping(value = "/api/v1/employee/{id}")
	EmployeeResponse getEmployeeById(@PathVariable(name = "id") String id);

	@GetMapping(value = "/api/v1/employees")
	EmployeesResponse getAllEmployees();

	@PostMapping(value = "/api/v1/create")
	CreateEmployeeResponse createEmployee(@RequestBody CreateEmployeeDto input);

	@DeleteMapping("/api/v1/delete/{id}")
	String deleteEmployeeById(@PathVariable(name = "id") String id);
}
