package com.example.rqchallenge.employees;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.employees.model.CreateEmployeeDto;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.IEmployeeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class EmployeeController implements IEmployeeController {
	@Autowired
	private IEmployeeService employeeService;

	@Override
	public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
		return ResponseEntity.ok(employeeService.getAllEmployees());

	}

	@Override
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
		return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(searchString));
	}

	@Override
	public ResponseEntity<Employee> getEmployeeById(String id) {
		var emp = employeeService.getEmployeeById(id);

		if (emp != null)
			return ResponseEntity.ok(emp);

		log.info("Employee not found. id : [{}]", id);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());

	}

	@Override
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		return ResponseEntity.ok(employeeService.getTopTenHighestEarningEmployeeNames());
	}

	@Override
//	public ResponseEntity<Employee> createEmployee(@Valid @CreateEmployeeInput Map<String, Object> employeeInput) {
	public ResponseEntity<Employee> createEmployee(@Valid CreateEmployeeDto employeeInput) {
		return ResponseEntity.ok(employeeService.createEmployee(employeeInput));
	}

	@Override
	public ResponseEntity<String> deleteEmployeeById(String id) {
		var emp = employeeService.deleteEmployeeById(id);

		if (emp != null)
			return ResponseEntity.ok(emp);

		log.info("Employee not found. id : [{}]", id);

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
