package com.example.rqchallenge.employees;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.employees.model.CreateEmployeeDto;
import com.example.rqchallenge.employees.model.Employee;

@RestController
public interface IEmployeeController {
	@GetMapping()
	ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

	@GetMapping("/search/{searchString}")
	ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

	@GetMapping("/{id}")
	ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

	@GetMapping("/highestSalary")
	ResponseEntity<Integer> getHighestSalaryOfEmployees();

	@GetMapping("/topTenHighestEarningEmployeeNames")
	ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

	@PostMapping()
	// ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object>
	// employeeInput);
	// ReadMe mentions to return success message, but the signature is to return
	// Employee created record, returning the employee record.
	// Changed the signature for adding validation.
	ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeDto employeeInput);

	@DeleteMapping("/{id}")
	ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}
