package com.example.rqchallenge;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.employees.model.CreateEmployeeDto;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.IEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IEmployeeController.class)
public class EmployeeControllerTest {
	@MockBean
	IEmployeeService employeeService;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	MockMvc mockMvc;

	@Test
	public void createEmployeeReturnsSuccessForValidInput() throws Exception {
		String name = "employee 1", id = "1";
		int salary = 40000, age = 30;
		Employee employee = new Employee(id, name, salary, age);
		Map<String, Object> input = createEmployeRequest(name, salary, age);

		Mockito.when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(employee);

		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.employee_name", Matchers.is("employee 1")));
	}

	@Test
	public void createEmployeeReturnsBadRequestForInavlidName() throws Exception {
		String name = " ", id = "1";
		int salary = 40000, age = 35;
		Map<String, Object> input = createEmployeRequest(name, salary, age);
		Employee employee = new Employee(id, name, salary, age);

		// ideally this shouldn't be called in this case
		Mockito.when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(employee); 

		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test()
	public void createEmployeeReturnsBadRequestForInavlidSalary() throws Exception {
		String name = "employee 1", id = "1";
		int salary = -40000, age = 25;
		Map<String, Object> input = createEmployeRequest(name, salary, age);
		Employee employee = new Employee(id, name, salary, age);

		// Ideally this shouldn't be called in this case
		Mockito.when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(employee); 

		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@ParameterizedTest()
	@ValueSource(ints = { 20, 61 }) // Minimum and Maximum age allowed 
	public void createEmployeeReturnsBadRequestForInavlidAge(int age) throws Exception {
		String name = "employee 1", id = "1";
		int salary = 40000;
		Map<String, Object> input = createEmployeRequest(name, salary, age);
		Employee employee = new Employee(id, name, salary, age);

		// Ideally this shouldn't be called in this case
		Mockito.when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(employee);

		mockMvc.perform(post("/").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(input)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void getEmployeeByIdReturnsNotFoundWhenEmployeeDoesNotExist() throws Exception {
		String id = "1";
		Mockito.when(employeeService.getEmployeeById(id)).thenReturn(null);

		mockMvc.perform(get("/{id}", id).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void getEmployeeByIdReturnsEmployeeOnSuccess() throws Exception {
		String id = "1";
		String name = "test employee";
		Employee employee = new Employee(id, name, 35, 40000);
		Mockito.when(employeeService.getEmployeeById("1")).thenReturn(employee);

		mockMvc.perform(get("/1")

				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", Matchers.is("1")))
				.andExpect(jsonPath("$.employee_name", Matchers.is("test employee")))
				.andExpect(jsonPath("$.employee_salary", Matchers.is(40000)))
				.andExpect(jsonPath("$.employee_age", Matchers.is(35)));
	}

	@Test
	void getHighestSalaryOfEmployeesReturnsSucesss() throws Exception {
		Mockito.when(employeeService.getHighestSalaryOfEmployees()).thenReturn(500000);

		mockMvc.perform(get("/highestSalary")

				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("500000"));
	}

	@Test
	void getAllEmployeesReturnsEmployeesReturnsSuccess() throws Exception {

		Employee[] employees = new Employee[] { new Employee("1", "Employee 1", 33, 1102044),
				new Employee("3", "Employee 3", 43, 305654), new Employee("5", "Employee 5", 45, 345654),
				new Employee("8", "Employee 8", 48, 385654), new Employee("12", "Employee 12", 35, 502054) };

		Mockito.when(employeeService.getAllEmployees()).thenReturn(Arrays.asList(employees));

		mockMvc.perform(get("/").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	void getTopTenHighestEarningEmployeeNamesReturnsSuccess() throws Exception {
		String[] employeeNames = { "Emp1", "Emp2", "Emp4" };
		Mockito.when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(Arrays.asList(employeeNames));

		mockMvc.perform(get("/topTenHighestEarningEmployeeNames").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	void getEmployeesByNameSearchReturnsSucess() throws Exception {
		Employee[] employees = new Employee[] { new Employee("1", "Employee 1", 33, 1102044),
				new Employee("3", "Employee 3", 43, 305654), new Employee("5", "Employee 5", 45, 345654),
				new Employee("8", "Employee 8", 48, 385654), new Employee("12", "Employee 12", 35, 502054) };

		Mockito.when(employeeService.getEmployeesByNameSearch("Employee")).thenReturn(Arrays.asList(employees));

		mockMvc.perform(get("/search/{searchString}", "Employee").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(5)));
	}

	@Test
	void deleteEmployeeByIdReturnsNotFoundWhenEmployeeDoesNotExist() throws Exception {
		String id = "1";
		Mockito.when(employeeService.getEmployeeById(id)).thenReturn(null);

		mockMvc.perform(delete("/1").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteEmployeeByIdReturnsNotFoundWhenEmployeeReturnsNameWhenDeleteSucces() throws Exception {
		String name = "test employee";
		Mockito.when(employeeService.deleteEmployeeById("1")).thenReturn(name);

		mockMvc.perform(delete("/1")

				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(name));
	}

	private Map<String, Object> createEmployeRequest(String name, int salary, int age) {
		Map<String, Object> input = new HashMap<>();
		input.put("name", name);
		input.put("age", age);
		input.put("salary", salary);
		return input;
	}
}
