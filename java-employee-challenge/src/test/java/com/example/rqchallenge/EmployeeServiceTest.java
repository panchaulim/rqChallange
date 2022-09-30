package com.example.rqchallenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.model.EmployeeResponse;
import com.example.rqchallenge.employees.model.EmployeesResponse;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.employees.service.IEmployeeService;
import com.example.rqchallenge.employees.service.IEmployeeServiceProxy;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class EmployeeServiceTest {
	IEmployeeService employeeService;
	IEmployeeServiceProxy mockServiceProxy;

	@BeforeEach
	void initialize() {
		this.mockServiceProxy = mock(IEmployeeServiceProxy.class);
		employeeService = new EmployeeService(mockServiceProxy);
	}

	@Autowired
	ObjectMapper objectMapper;

	@Test
	void getAllEmployeesTest() throws Exception {
		EmployeesResponse mockEmployees = new EmployeesResponse();
		mockEmployees.setData(getTestEmployeeCollection());

		when(mockServiceProxy.getAllEmployees()).thenReturn(mockEmployees);

		var employeeRetrived = employeeService.getAllEmployees();
		assertThat(objectMapper.writeValueAsString(employeeRetrived))
				.isEqualTo(objectMapper.writeValueAsString(mockEmployees.getData()));

	}

	@Test
	void getEmployeeByIdSuccessTest() throws Exception {
		String employeeId = "10";
		Employee emp = new Employee(employeeId, "testEmployee", 4000, 40);
		EmployeeResponse mockEmployee = new EmployeeResponse();
		mockEmployee.setData(emp);

		when(mockServiceProxy.getEmployeeById(employeeId)).thenReturn(mockEmployee);
		Employee empReturned = employeeService.getEmployeeById(employeeId);
		assertThat(objectMapper.writeValueAsString(empReturned))
			.isEqualTo(objectMapper.writeValueAsString(emp));
	}

	@Test
	void getHighestSalarySuccessTest() throws Exception {
		var employees = new Employee[] {new Employee("201", "Emp  Middle Last", 25, 100000)
										, new Employee("202", "Emp2  Middle1 Last1", 30, 100011)
										, new Employee("203", "Emp  Middle2 Last2", 45, 10001)
										};

		EmployeesResponse mockEmployees = new EmployeesResponse();
		mockEmployees.setData(Arrays.asList(employees));

		when(mockServiceProxy.getAllEmployees()).thenReturn(mockEmployees);

		
		var highestSalary = employeeService.getHighestSalaryOfEmployees();

		assertThat(highestSalary).isEqualTo(100011);
	}

	@Test
	void getTopNHighestEarningEmployeeNamesSuccessTest() throws Exception {
		var mockEmployees = new EmployeesResponse();
		var expectedNames = new String[] { "Employee 1", "Employee 2", "Employee 3", "Employee 11", "Employee 12",
				"Employee 21", "Employee 22", "Employee 23", "Employee 31", "Employee 32" };
		var employees = new Employee[] { new Employee("1", "Employee 1", 33, 1102044),
				new Employee("2", "Employee 2", 35, 202054), new Employee("3", "Employee 3", 45, 305654),
				new Employee("11", "Employee 11", 33, 4402044), new Employee("12", "Employee 12", 35, 502054),
				new Employee("13", "Employee 13", 45, 10565), new Employee("21", "Employee 21", 33, 602044),
				new Employee("22", "Employee 22", 35, 702054), new Employee("23", "Employee 23", 45, 895655),
				new Employee("31", "Employee 31", 33, 902046), new Employee("32", "Employee 32", 35, 10102057),
				new Employee("33", "Employee 33", 45, 105654) };
		mockEmployees.setData(Arrays.asList(employees));

		when(mockServiceProxy.getAllEmployees()).thenReturn(mockEmployees);

		var topTenEarningEmployees = employeeService.getTopTenHighestEarningEmployeeNames();
		assertThat(topTenEarningEmployees).containsAll(Arrays.asList(expectedNames));

	}

	@Test
	void getEmployeesByNameSearchReturnsMatchingEmployeesWithExactMatchSearchString() throws Exception {
		var mockEmployees = new EmployeesResponse();
		var commonName = "Employee 1";
		var expectedNames = new String[] { commonName, commonName, commonName };
		var employees = new Employee[] { new Employee("1", commonName, 33, 1102044),
				new Employee("2", commonName, 35, 202054), new Employee("1", commonName, 45, 305654) };
		var empCol = getTestEmployeeCollection();
		empCol.addAll(Arrays.asList(employees));
		mockEmployees.setData(empCol);

		when(mockServiceProxy.getAllEmployees()).thenReturn(mockEmployees);

		var empNameList = employeeService.getEmployeesByNameSearch(commonName);
		assertThat(empNameList.stream().map(e -> e.getName()))
		.containsAll(Arrays.asList(expectedNames));

	}

	@ParameterizedTest
	@CsvSource(value = { "Emp:3", "emp:3", "test:0", "'':3", "' ' :3", "Emp  Middle Last:1" }, delimiter = ':')
	void getEmployeesByNameSearchReturnsMatchingEmployees(String searchString, int result) throws Exception {
		var mockEmployees = new EmployeesResponse();
		mockEmployees.setData(getTestEmployeeCollection());

		when(mockServiceProxy.getAllEmployees()).thenReturn(mockEmployees);

		var empList = employeeService.getEmployeesByNameSearch(searchString);
		assertThat(empList.size()).isEqualTo(result);

	}

	@Test
	void getEmployeeByIdReturnsNullWhenEmployeDoesNotExist() throws Exception {
		String employeeId = "10";
		EmployeeResponse mockEmployee = new EmployeeResponse();

		when(mockServiceProxy.getEmployeeById(employeeId)).thenReturn(mockEmployee);

		String empName = employeeService.deleteEmployeeById(employeeId);

		assertThat(empName).isNull();
	}

	@Test
	void deleteEmployeeByIdReturnsEmployeeNameOnSuccess() throws Exception {
		String employeeId = "10";
		Employee e = new Employee(employeeId, "testEmployee", 4000, 40);
		EmployeeResponse mockEmployee = new EmployeeResponse();
		mockEmployee.setData(e);

		when(mockServiceProxy.getEmployeeById(employeeId)).thenReturn(mockEmployee);
		when(mockServiceProxy.deleteEmployeeById(employeeId)).thenReturn("{\"status:success\"}");

		String empName = employeeService.deleteEmployeeById(employeeId);

		assertThat(e.getName()).isEqualTo(empName);
	}

	@Test
	void deleteEmployeeByIdReturnsNullWhenEmployeDoesNotExist() throws Exception {
		String employeeId = "10";
		EmployeeResponse mockEmployee = new EmployeeResponse();

		when(mockServiceProxy.getEmployeeById(employeeId)).thenReturn(mockEmployee);

		String empName = employeeService.deleteEmployeeById(employeeId);

		assertThat(empName).isNull();
	}

	private Collection<Employee> getTestEmployeeCollection() {
		Collection<Employee> employeeList = new ArrayList<>();
		employeeList.add(new Employee("201", "Emp  Middle Last", 25, 100000));
		employeeList.add(new Employee("202", "Emp2  Middle1 Last1", 30, 100011));
		employeeList.add(new Employee("203", "Emp  Middle2 Last2", 45, 10001));
		return employeeList;
	}
}
