package com.axonactive.employeeui.controller.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;

import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.primefaces.PrimeFaces;

import com.axonactive.employeeui.controller.EmployeeController;
import com.axonactive.employeeui.dto.FakeDepartmentDTO;
import com.axonactive.employeeui.dto.FakeEmployeeDTO;
import com.axonactive.employeeui.restclient.DepartmentRestClient;
import com.axonactive.employeeui.restclient.EmployeeRestClient;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PrimeFaces.class })
public class EmployeeControllerTest {

	@InjectMocks
	EmployeeController employeeController;

	@Mock
	ValueChangeEvent valueChangeEvent;

	@Mock
	PrimeFaces primeFaces;

	@Mock
	EmployeeRestClient employeeRestClient;

	@Mock
	Logger logger;

	@Mock
	DepartmentRestClient departmentRestClient;

	@Before
	public void init() {
		PowerMockito.mockStatic(PrimeFaces.class);
		Mockito.when(PrimeFaces.current()).thenReturn(primeFaces);
	}

	@Test
	public void testInit_ShouldShowList_WhenViewEmployeeisSuccessful() throws ClientProtocolException, IOException {
		// Init Variables

		FakeDepartmentDTO departmentDto = createDepartmentDTO();
		// Mock when
		Mockito.when(employeeRestClient.getAllEmployeeList()).thenReturn(Arrays.asList(createEmployee()));
		Mockito.when(departmentRestClient.getAllDepartmentList()).thenReturn(Arrays.asList(createDepartmentDTO()));
		// Call function
		employeeController.init();
		// AssertEquals
		assertEquals(departmentDto, employeeController.getDepartment());
	}

	@Test(expected = NoResultException.class)
	public void testInit_ShouldThrowException_WhenViewEmployeeIsSuccessful()
			throws ClientProtocolException, IOException {
		// Init Variables
		// Mock when
		Mockito.when(employeeRestClient.getAllEmployeeList()).thenReturn(Arrays.asList());
		Mockito.when(departmentRestClient.getAllDepartmentList()).thenReturn(Arrays.asList());
		// Call function
		employeeController.init();
	}

	@Test
	public void testViewEmployee_ShouldShowDialog_WhenViewEmployeeisSuccessful() {
		// Init Variables
		FakeEmployeeDTO employee = createEmployee();
		// Call function
		employeeController.viewEmployee(employee);
		// verify
		Mockito.verify(primeFaces).executeScript("PF('UpdateEmployee').show()");

	}

	/*
	 * @Test public void
	 * testDeleteEmployeeFromPage_ShouldDeleteEmployee_WhenDeleteingIsSuccessful()
	 * throws Exception { // Init Variables // Mock when
	 * Mockito.when(employeeRestClient.getAllEmployeeList()).thenReturn(Arrays.
	 * asList(createEmployee())); // Call function
	 * employeeController.deleteEmployeeFromPage(1); // verify
	 * Mockito.verify(employeeRestClient).deleteEmployee(1); }
	 */

	@Test
	public void testChangeDepartment_ShouldReturnDepartment_WhenValueChangeEvent() throws Exception {
		// Init Variables
		// Mock when
		Mockito.when(valueChangeEvent.getNewValue()).thenReturn(1);
		Mockito.when(departmentRestClient.getDepartmentByID(1)).thenReturn(createDepartmentDTO());
		// Call function
		employeeController.changeDepartment(valueChangeEvent);
		// verify
	}

	private FakeEmployeeDTO createEmployee() {
		FakeDepartmentDTO department = createDepartmentDTO();
		return new FakeEmployeeDTO(1, "thinzar", "Yoon", null, null, null, null, null, null, null, department);
	}

	private FakeDepartmentDTO createDepartmentDTO() {
		return new FakeDepartmentDTO(1, "ICT");
	}

}
