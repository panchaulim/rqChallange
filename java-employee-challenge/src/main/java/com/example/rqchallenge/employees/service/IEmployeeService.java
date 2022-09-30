package com.example.rqchallenge.employees.service;

import java.util.List;

import com.example.rqchallenge.employees.model.CreateEmployeeDto;
import com.example.rqchallenge.employees.model.Employee;

public interface IEmployeeService {
	List<Employee> getAllEmployees();

	Employee getEmployeeById(String id);

	List<Employee> getEmployeesByNameSearch(String searchString);

	int getHighestSalaryOfEmployees();

	List<String> getTopTenHighestEarningEmployeeNames();

	Employee createEmployee(CreateEmployeeDto employeeInput);
//	Employee createEmployee(Map<String, Object> employeeInput);

	String deleteEmployeeById(String id);

}
