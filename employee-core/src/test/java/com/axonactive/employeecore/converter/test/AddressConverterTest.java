package com.axonactive.employeecore.converter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.address.AddressConverter;
import com.axonactive.employeecore.address.AddressDTO;
import com.axonactive.employeecore.address.AddressEntity;

@RunWith(PowerMockRunner.class)
public class AddressConverterTest {

	@Mock
	AddressConverter addressConverter;
	AddressEntity addressEntity = createAddressEntity();
	AddressDTO addressDTO = createAddresDTO();
	List<AddressEntity> addressEntities = Arrays.asList(createAddressEntity());
	List<AddressDTO> addressDTOs = Arrays.asList(createAddresDTO());

	@Test
	public void testToEntity_ShouldReturnAddressEntity_WhenDTOIsGiven() {

		AddressEntity actual = AddressConverter.getInstance().toEntity(addressDTO);
		assertEquals(actual, addressEntity);
	}

	@Test
	public void testToEntity_ShouldReturnNull_WhenDTOIsGiven() {
		AddressEntity actual = AddressConverter.getInstance().toEntity(null);
		assertNull(actual);
	}

	@Test
	public void testToDTO_ShouldReturnAddressDTO_WhenAddressEntityIsGiven() {
		AddressDTO actual = AddressConverter.getInstance().toDTO(addressEntity);
		assertEquals(addressDTO, actual);
	}

	@Test
	public void testToDTO_ShouldReturnNull_WhenAddressEntityIsGiven() {
		AddressDTO actual = AddressConverter.getInstance().toDTO(null);
		assertNull(actual);
	}

	@Test
	public void testToListEntity_ShouldReturnList_WhenAddressDTOIsGiven() {
		List<AddressEntity> actual = AddressConverter.getInstance().toListEntity(addressDTOs);
		assertEquals(addressEntities, actual);
	}

	@Test
	public void testToListDTO_ShouldReturnEmpty_WhenAddressDTOIsGiven() {
		List<AddressEntity> actual = AddressConverter.getInstance().toListEntity(null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testToListDTO_ShouldReturnAddressDTOList_WhenAddressEntityIsGiven() {
		List<AddressDTO> actual = AddressConverter.getInstance().toListDTO(addressEntities);
		assertEquals(addressDTOs, actual);
	}

	@Test
	public void testToListDTO_ShouldReturnEmpty_WhenAddressEntityIsGiven() {
		List<AddressDTO> actual = AddressConverter.getInstance().toListDTO(null);
		assertTrue(actual.isEmpty());
	}

	private AddressDTO createAddresDTO() {
		return new AddressDTO(1, "hai au 10th floor", "truong son", "4");
	}

	private AddressEntity createAddressEntity() {
		return new AddressEntity(1, "hai au 10th floor", "truong son", "4");
	}
}
