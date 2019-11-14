package com.axonactive.employeecore.restconfiguration.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.department.DepartmentDTO;
import com.axonactive.employeecore.department.DepartmentResource;
import com.axonactive.employeecore.department.DepartmentService;


@RunWith(PowerMockRunner.class)
public class DepartmentResourceTest {

	@InjectMocks
	DepartmentResource departmentResource;
	
	@Mock
	DepartmentService departmentService;
	
	@Mock
	Logger logger;
	
	@Test
	public void testgetAllDepartmentList_ShouldReturnDepartmentList_WhenListExit() {
		//Init Variables
		List<DepartmentDTO> expected=Arrays.asList(createDepartmentDTO());
		//Mock when
		Mockito.when(departmentService.getAllDepartmentList()).thenReturn(Arrays.asList(createDepartmentDTO()));
		//AssertEquals
		assertEquals(expected,departmentService.getAllDepartmentList());
	}
	
	@Test
	public void testGetDepartmentByID_ShouldReturnResponse_WhenIdIsGiven() {
		//Mock when
		Mockito.when(departmentService.findDepartmentDTOById(1)).thenReturn(createDepartmentDTO());
		//Call function
		Response actual=departmentResource.getDepartmentById(1);
		//AssertEquals
		assertEquals(200, actual.getStatus());
	}
	public DepartmentDTO createDepartmentDTO() {
		return new DepartmentDTO(1,"ICT");
	}
}
