package com.axonactive.employeecore.service.test;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.department.DepartmentConverter;
import com.axonactive.employeecore.department.DepartmentDTO;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.department.DepartmentService;
import com.axonactive.employeecore.exception.EntityNotFoundException;

@RunWith(PowerMockRunner.class)
public class DepartmentServiceTest {

	@InjectMocks
	DepartmentService departmentService;

	@Mock
	EntityManager em;

	@Mock
	TypedQuery<DepartmentEntity> query;

	@Mock
	DepartmentConverter departmentConverter;

	@Test
	public void testGetAllDepartmentList_ShouldReturnList() {
		Mockito.when(em.createNamedQuery(DepartmentEntity.FIND_ALL, DepartmentEntity.class)).thenReturn(query);

		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(createDepartmentEntity()));
		departmentService.getAllDepartmentList();
	}

	@Test
	public void testFindDepartmentDTOById_ShouldReturnDepartmentDTO_WhenIDIsGiven() {
		DepartmentDTO departmentDTO = createDepartmentDTO();
		
		Mockito.when(em.find(DepartmentEntity.class, 1)).thenReturn(createDepartmentEntity());
		Mockito.when(departmentConverter.toDTO(createDepartmentEntity())).thenReturn(departmentDTO);
		
		departmentService.findDepartmentDTOById(1);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindDepartmentDTOById_ShouldThrowException_WhenIDIsGiven() {
		DepartmentDTO departmentDTO = createDepartmentDTO();
		departmentDTO.setDepartmentId(1);
		departmentDTO.setName("ICT");
		Mockito.when(em.createNamedQuery(DepartmentEntity.FIND_BY_ID, DepartmentEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("deptid", departmentDTO.getDepartmentId())).thenReturn(query);
		Mockito.when(query.getSingleResult()).thenReturn(null);
		departmentService.findDepartmentDTOById(1);
	}

	@Test
	public void testFindDepartmentEntityById_ShouldReturnDepartmentEntity_WhenIDIsGiven() {
		Mockito.when(em.find(DepartmentEntity.class, 1)).thenReturn(createDepartmentEntity());
		departmentService.findDepartmentEntityById(1);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindDepartmentEntityById_ShouldThrowException_WhenIDIsGiven() {
		Mockito.when(em.find(DepartmentEntity.class, 1)).thenReturn(null);
		departmentService.findDepartmentEntityById(1);
	}

	@Test
	public void testAddDepartment_ShouldAddDepartment_WhenDepartmentDTOIsGiven() {
		DepartmentDTO departmentDTO = createDepartmentDTO();
		DepartmentEntity departmentEntity = createDepartmentEntity();
		
		Mockito.when(em.createNamedQuery(DepartmentEntity.FIND_BY_NAME, DepartmentEntity.class)).thenReturn(query);
		Mockito.when(query.setParameter("name", departmentDTO.getName())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());
		
		Mockito.when(departmentConverter.toEntity(departmentDTO)).thenReturn(departmentEntity);
		departmentService.addDepartment(departmentDTO);
	}

	@Test
	public void testUpdateDepartment_ShouldUpdateDepartment_WhenDepartmentDTOIsGiven() {
		DepartmentDTO departmentDTO = createDepartmentDTO();
		departmentDTO.setDepartmentId(1);
		departmentDTO.setName("ICT");
		DepartmentEntity departmentEntity = createDepartmentEntity();
		Mockito.when(departmentConverter.toEntity(departmentDTO)).thenReturn(departmentEntity);
		departmentService.updateDepartment(departmentDTO);
	}
	
	@Test
	public void testDeleteDepartment_ShouldDeleteDepartment_WhenDepartmentIDIsGiven() {
		DepartmentEntity departmentEntity = createDepartmentEntity();
		Mockito.when(em.find(DepartmentEntity.class, 1)).thenReturn(departmentEntity);
		departmentService.deleteDepartment(1);
	}

	private DepartmentDTO createDepartmentDTO() {
		return new DepartmentDTO(1,"ICT");
	}

	private DepartmentEntity createDepartmentEntity() {
		return new DepartmentEntity(1, "ICT");
	}

}
