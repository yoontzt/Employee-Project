package com.axonactive.employeecore.service.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.department.DepartmentDTO;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.department.DepartmentService;
import com.axonactive.employeecore.employee.BasicEmployeeDTO;
import com.axonactive.employeecore.employee.EmployeeConverter;
import com.axonactive.employeecore.employee.EmployeeEntity;
import com.axonactive.employeecore.employee.EmployeeService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.presistence.GenericService;

@RunWith(PowerMockRunner.class)
public class EmployeeServiceTest {

	@InjectMocks
	EmployeeService employeeService;

	@Mock
	EntityManager em;

	@Mock
	EmployeeConverter employeeConverter;

	@Mock
	GenericService<EmployeeEntity> genericService;

	@Mock
	TypedQuery<EmployeeEntity> query;

	@Mock
	private DepartmentService departmentService;

	@Test
	public void testGetAllEmployee_ShouldReturnEmployeeList_WhenEntityIsExisted() {
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_ALL, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());

		List<BasicEmployeeDTO> actual = employeeService.getAllEmployeeList();
		List<BasicEmployeeDTO> expected = Arrays.asList();
		assertEquals(expected, actual);
	}

	@Test
	public void testFindEmployeeEntity_ShouldReturnEmployeeEntity_WhenIdIsGiven() {
		EmployeeEntity expected = createEmployeeEntity();
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(expected);
		EmployeeEntity actual = employeeService.findEmployeeEntityById(1);
		assertEquals(expected, actual);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindEmployeeEntity_ShouldthrowException_WhenEmployeeIsNull() {
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(null);
		Mockito.when(employeeConverter.toDTO(employeeEntity)).thenReturn(null);
		employeeService.findEmployeeEntityById(1);
	}

	@Test
	public void testFindEmployeeByID_ShouldReturnEmployeeDto_WhenIdIsGiven() {
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);
		Mockito.when(employeeConverter.toDTO(employeeEntity)).thenReturn(createEmployee());
		BasicEmployeeDTO actual = employeeService.findEmployeeById(1);
		assertEquals(createEmployee(), actual);

	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindEmployeeByID_ShouldthrowException_WhenEmployeeIsNull() {
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(null);
		Mockito.when(employeeConverter.toDTO(employeeEntity)).thenReturn(null);
		employeeService.findEmployeeById(1);
	}

	@Test
	public void testAddEmployee_ShouldAddEmployee_WhenEmployeeDtoIsGiven() {
		BasicEmployeeDTO employeeDTO = createEmployee();
		DepartmentDTO department = createDepartment();
		DepartmentEntity deptEntity = createDepartmentEntity();
		employeeDTO.setEmployeeId(null);
		employeeDTO.setDepartment(department);
		employeeDTO.setPrimaryEmail("gg@email.com");
		employeeDTO.setMobilePhone("110");
		employeeDTO.setSkype("skype");
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_EMAIL, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("primaryEmail", employeeDTO.getPrimaryEmail())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_PHONE, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("mobilePhone", employeeDTO.getMobilePhone())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_SKYPE, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("skype", employeeDTO.getSkype())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());
		Mockito.when(employeeConverter.toEntity(employeeDTO)).thenReturn(employeeEntity);
		Mockito.when(departmentService.findDepartmentEntityById(1)).thenReturn(deptEntity);
		employeeService.addEmployee(employeeDTO);
	}

	@Test(expected = ValidationException.class)
	public void testAddEmployee_ShouldThrowException_WhenEmployeeDtoIsGiven() {
		BasicEmployeeDTO employeeDTO = createEmployee();
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_EMAIL, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("primaryEmail", employeeDTO.getPrimaryEmail())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(employeeEntity));
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_PHONE, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("mobilePhone", employeeDTO.getMobilePhone())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(employeeEntity));
		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_SKYPE, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("skype", employeeDTO.getSkype())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(employeeEntity));
		Mockito.when(employeeConverter.toEntity(employeeDTO)).thenReturn(employeeEntity);
		employeeService.addEmployee(employeeDTO);
	}

	@Test
	public void testUpdateEmployee_ShouldUpdateEmployee_WhenEmployeeDtoIsGiven() {
		EmployeeEntity employeeEntity = createEmployeeEntity();
		BasicEmployeeDTO employeeDTO = createEmployee();
		DepartmentEntity deptEntity = createDepartmentEntity();

		employeeDTO.setPrimaryEmail("gg@email.com");
		employeeDTO.setMobilePhone("110");
		employeeDTO.setSkype("skype");

		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_EMAIL_FOR_UPDATE, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("primaryEmail", employeeDTO.getPrimaryEmail())).thenReturn(query);
		Mockito.when(query.setParameter("employeeId", employeeDTO.getEmployeeId())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());

		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);
		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(departmentService.findDepartmentEntityById(1)).thenReturn(deptEntity);

		employeeService.updateEmployee(employeeDTO);
	}

	// TODO-Check this again
	@Test
	public void testUpdateEmployee_ShouldThrowException_WhenEmployeeDtoIsEmpty() {
		EmployeeEntity employeeEntity = createEmployeeEntity();
		BasicEmployeeDTO employeeDTO = createEmployee();
		DepartmentEntity deptEntity = createDepartmentEntity();

		Mockito.when(em.createNamedQuery(EmployeeEntity.FIND_EMAIL_FOR_UPDATE, EmployeeEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("primaryEmail", employeeDTO.getPrimaryEmail())).thenReturn(query);
		Mockito.when(query.setParameter("employeeId", employeeDTO.getEmployeeId())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());

		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);
		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(departmentService.findDepartmentEntityById(1)).thenReturn(deptEntity);

		employeeService.updateEmployee(employeeDTO);
	}

	@Test
	public void testDeleteEmployee_ShouldDeleteEmployee_WhenEmployeeIdIsGiven() {
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);
		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(em.contains(employeeEntity)).thenReturn(true);

		employeeService.deleteEmployeeById(employeeEntity.getEmployeeId());

	}

	private BasicEmployeeDTO createEmployee() {
		DepartmentDTO department = createDepartment();
		return new BasicEmployeeDTO(1, "thinzar", "yoon", null, null, null, null, null, null, null, department);
	}

	private DepartmentDTO createDepartment() {
		return new DepartmentDTO(1, "ICT");
	}

	private DepartmentEntity createDepartmentEntity() {
		return new DepartmentEntity(1, "ICT");
	}

	private EmployeeEntity createEmployeeEntity() {
		DepartmentEntity department = createDepartmentEntity();
		return new EmployeeEntity(1, "thinzar", "yoon", null, null, null, null, null, null, null, null, null,
				department);
	}

}