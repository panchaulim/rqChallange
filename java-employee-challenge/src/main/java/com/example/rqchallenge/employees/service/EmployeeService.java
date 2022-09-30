package com.example.rqchallenge.employees.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rqchallenge.employees.model.CreateEmployeeDto;
import com.example.rqchallenge.employees.model.Employee;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

	@Autowired
	private final IEmployeeServiceProxy serviceProxy;

	public EmployeeService(IEmployeeServiceProxy proxy) {
		this.serviceProxy = proxy;
	}

	@Override
	public List<Employee> getAllEmployees() {
		log.info("Calling external api to get all the employees");
		var employees = serviceProxy.getAllEmployees();
		return employees.getData().stream().toList();
	}

	@Override
	public Employee getEmployeeById(String id) {
		log.info("Calling external api to get empployee info with employee id [{}]", id);
		var emp = serviceProxy.getEmployeeById(id).getData();

		return (emp != null)
				? new Employee(emp.getId(), emp.getName(), emp.getAge(), emp.getSalary(), emp.getProfileImage())
				: null;
	}

	@Override
	public List<Employee> getEmployeesByNameSearch(String searchString) {
		log.trace("Calling external api to perform name search with search string: [{}]", searchString);
		var employees = getAllEmployees();
		// Assuming that name search should ignore case.
		var lowerCaseSearchString = searchString.toLowerCase();
		if (employees == null) {
			log.info("Total employee returned by external api :[{}].", 0);
			return new ArrayList<>();
		} else {
			log.info("Total employee returned by external api :[{}].", employees.size());
		}
		return employees.stream().filter((e) -> e.getName().toLowerCase().contains(lowerCaseSearchString)).toList();
	}

	@Override
	public int getHighestSalaryOfEmployees() {
		log.debug("Getting empployee with heightest salary");
		return getTopNHighestEarningEmployee(1).get(0).getSalary();
	}

	@Override
	public List<String> getTopTenHighestEarningEmployeeNames() {
		log.debug("Getting 10 hihest earning employees.");
		return getTopNHighestEarningEmployee(10).stream().map((e) -> e.getName()).toList();
	}

	@Override
	public Employee createEmployee(CreateEmployeeDto employeeInput) {
		log.info("Calling external api to create empployee with details: [{}] ", employeeInput);
		var emp = serviceProxy.createEmployee(employeeInput).getData();
		return new Employee(emp.getId(), emp.getName(), emp.getAge(), emp.getSalary());
	}

	@Override
	public String deleteEmployeeById(String id) {
		log.info("Calling external api to delete empployee with id: [{}] ", id);
		// first get the employee for returning employee name.
		var emp = getEmployeeById(id);

		if (emp == null) {
			log.info("employee doesn not exist with id: " + id);

			return null;
		}

		var deleteEmpResponse = serviceProxy.deleteEmployeeById(id);

		log.debug("deleteEmployeeById response = " + deleteEmpResponse);

		return emp.getName();
	}

	private List<Employee> getTopNHighestEarningEmployee(int count) {
		ArrayList<Employee> empList = new ArrayList<>(getAllEmployees());
		log.info("Sorting employees based don their salary.");
		empList.sort((e1, e2) -> e2.getSalary() - e1.getSalary());
		log.info("Returning " + count + " employees from the total " + empList.size());
		return empList.stream().limit(count).toList();
	}

}
