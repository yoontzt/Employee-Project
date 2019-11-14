  package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.axonactive.employeeui.dto.FakeContactDTO;
import com.axonactive.employeeui.dto.FakeDepartmentDTO;
import com.axonactive.employeeui.dto.FakeEmployeeDTO;
import com.axonactive.employeeui.dto.TokenDTO;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.helper.JavaScriptHelper;
import com.axonactive.employeeui.restclient.ContactRestClient;
import com.axonactive.employeeui.restclient.DepartmentRestClient;
import com.axonactive.employeeui.restclient.EmployeeRestClient;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = "employeeController")
@SessionScoped
public class EmployeeController implements Serializable {

	private static final String ADD_EMPLOYEE = "AddEmployee";

	private static final long serialVersionUID = 7889451587603628722L;

	private transient @Getter @Setter FakeDepartmentDTO department = new FakeDepartmentDTO();

	private transient @Getter @Setter FakeEmployeeDTO employee = new FakeEmployeeDTO();

	private transient @Getter @Setter FakeContactDTO contactDTO = new FakeContactDTO();

	private LoginController loginController;

	private @Getter @Setter int id;

	@EJB
	EmployeeRestClient empRestClient;

	@EJB
	ContactRestClient contactRestClient;

	@EJB
	DepartmentRestClient deptRestClient;

	@Inject
	Logger logger;

	private transient @Getter @Setter List<FakeEmployeeDTO> employeeList = new ArrayList<>();

	private transient @Getter @Setter List<FakeDepartmentDTO> departmentList = new ArrayList<>();

	private transient @Getter @Setter List<FakeContactDTO> contactList = new ArrayList<>();

	private transient @Getter @Setter List<FakeEmployeeDTO> employeeSearchList = new ArrayList<>();

	TokenDTO token = new TokenDTO();

	TokenController tokenController = new TokenController();

	private @Getter @Setter String input;

	private @Getter @Setter String tokenValue;

	@PostConstruct
	public void init() {
		logger.info("Get Employee list.");
		try {
			employeeList = empRestClient.getAllEmployeeList();
			departmentList = deptRestClient.getAllDepartmentList();
		} catch (IOException e) {
			logger.info("IO Exception occured");
		}
		if (departmentList.isEmpty()) {
			logger.info("An employee empty list is returned.");
			throw new NoResultException("No source found");
		} else {
			logger.info("Get Employee list successfully.");
			department = departmentList.get(0);
		}
	}

	public void searchingEmployee() throws IOException {
		input = input.trim();
		employeeList = empRestClient.searchEmployee(input);
	}

	public void deleteEmployeeFromPage(Integer id) {
		try {
			empRestClient.deleteEmployee(id);
			employeeList = empRestClient.getAllEmployeeList();
			logger.info("Deleted an employee successfully : id " + id);
		} catch (SystemException | IOException exception) {
			logger.error("Deleted an employee failed.", exception);
		}
	}

	public void viewEmployee(FakeEmployeeDTO emp) {
		setEmployee(emp);
		setId(emp.getDepartment().getDepartmentId());
		PrimeFaces.current().executeScript("PF('UpdateEmployee').show()");
	}

	public String viewUpdateEmployee(FakeEmployeeDTO emp) throws IOException {
		setEmployee(emp);
		setId(emp.getDepartment().getDepartmentId());
		return "views/view-employee.xhtml";
	}

	public void changeDepartment(ValueChangeEvent dept) {
		try {
			logger.info(Integer.parseInt(dept.getNewValue().toString()));
			department = deptRestClient.getDepartmentByID(Integer.parseInt(dept.getNewValue().toString()));
		} catch (IllegalArgumentException | IOException exception) {
			throw new SystemException("Change Deparment is failed");
		}
	}

	public void goToAddEmployeePage() {
		PrimeFaces.current().executeScript("goToAddPage();");
	}

	public void cancelOnUpdate() {
		JavaScriptHelper.hideDialog("UpdateEmployee");
	}

	public void cancelOnAdd() {
		employee = new FakeEmployeeDTO();
		department.setDepartmentId(1);
		JavaScriptHelper.hideDialog(ADD_EMPLOYEE);
	}

	public void openAddDialog() {
		JavaScriptHelper.showDialog(ADD_EMPLOYEE);
	}

	public void reload() {
		PrimeFaces.current().executeScript("reload();");
	}

}
