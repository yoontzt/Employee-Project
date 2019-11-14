package com.axonactive.employeecore.converter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.department.DepartmentConverter;
import com.axonactive.employeecore.department.DepartmentDTO;
import com.axonactive.employeecore.department.DepartmentEntity;

@RunWith(PowerMockRunner.class)
public class DepartmentConverterTest {

	@Test
	public void testToEntity_ShouldReturnEntity_WhenDTOisGiven() {
		DepartmentEntity expected = createDepartmentEntity();
		DepartmentDTO departmentDTO = createDepartmentDTO();

		DepartmentEntity actual = DepartmentConverter.getInstance().toEntity(departmentDTO);
		assertEquals(expected, actual);

	}

	@Test
	public void testToEntity_ShouldReturnNull_WhenDTOisNull() {

		DepartmentEntity actual = DepartmentConverter.getInstance().toEntity(null);
		assertNull(actual);
	}

	@Test
	public void testToDTO_ShouldReturnDTO_WhenEntityisGiven() {
		DepartmentEntity departmentEntity = createDepartmentEntity();
		DepartmentDTO expected = createDepartmentDTO();

		DepartmentDTO actual = DepartmentConverter.getInstance().toDTO(departmentEntity);
		assertEquals(expected, actual);

	}

	@Test
	public void testToDTO_ShouldReturnNull_WhenEntityisNull() {

		DepartmentDTO actual = DepartmentConverter.getInstance().toDTO(null);
		assertNull(actual);
	}

	@Test
	public void testToEntities_ShouldReturnListEntity_WhenDepartmentDTOsIsGiven() {

		List<DepartmentEntity> actual = DepartmentConverter.getInstance()
				.toEntities(Arrays.asList(createDepartmentDTO()));
		List<DepartmentEntity> expected = Arrays.asList(createDepartmentEntity());
		assertEquals(expected, actual);
	}

	@Test
	public void testToEntities_ShouldReturnEmpty_WhenEmptyDepartmentDTOsIsGiven() {

		List<DepartmentEntity> actual = DepartmentConverter.getInstance().toEntities(Arrays.asList());
		List<DepartmentEntity> expected = Arrays.asList();
		assertEquals(expected, actual);
	}

	@Test
	public void testToDTOs_ShouldReturnDTOList_WhenEntityListIsGiven() {

		List<DepartmentDTO> expected = Arrays.asList(createDepartmentDTO(), createDepartmentDTO());
		List<DepartmentEntity> departmentEntities = Arrays.asList(createDepartmentEntity(), createDepartmentEntity());

		List<DepartmentDTO> actual = DepartmentConverter.getInstance().toDTOs(departmentEntities);
		assertEquals(expected, actual);
	}

	@Test
	public void testToDTOs_ShouldReturnDTOEmptyList_WhenEntityListIsNull() {
		List<DepartmentDTO> actual = DepartmentConverter.getInstance().toDTOs(null);
		assertTrue(actual.isEmpty());
	}

	private DepartmentDTO createDepartmentDTO() {

		return new DepartmentDTO(1, "ICT");
	}

	private DepartmentEntity createDepartmentEntity() {

		return new DepartmentEntity(1, "ICT");
	}
}
