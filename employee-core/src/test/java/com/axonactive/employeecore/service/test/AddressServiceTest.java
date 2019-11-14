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

import com.axonactive.employeecore.address.AddressConverter;
import com.axonactive.employeecore.address.AddressDTO;
import com.axonactive.employeecore.address.AddressEntity;
import com.axonactive.employeecore.address.AddressService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.presistence.GenericService;

@RunWith(PowerMockRunner.class)
public class AddressServiceTest {

	@InjectMocks
	AddressService addressService;

	@Mock
	EntityManager em;

	@Mock
	AddressConverter addressConverter;

	@Mock
	GenericService<AddressEntity> genericService;

	@Mock
	TypedQuery<AddressEntity> typeQuery;

	@Test
	public void testFindAddressByID_ShouldReturnAddressDTO_WhenBuildingNumberIsGiven() {

		AddressEntity addressEntity = createAddressEntity();
		Mockito.when(em.find(AddressEntity.class, 1)).thenReturn(addressEntity);
		Mockito.when(addressConverter.toDTO(addressEntity)).thenReturn(createAddressDTO());
		AddressDTO actual = addressService.findAddressByID(1);
		assertEquals(createAddressDTO(), actual);
		addressService.findAddressByID(1);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindAddressByID_ShouldThrowException_WhenBuildingNumberIsGiven() {
		AddressEntity addressEntity = createAddressEntity();
		Mockito.when(em.find(AddressEntity.class, 1)).thenReturn(null);
		Mockito.when(addressConverter.toDTO(addressEntity)).thenReturn(null);
		addressService.findAddressByID(1);
	}

	@Test
	public void testGetAllAddress_ShouldReturnListAddressDTO_WhenNothingIsGiven() {
		Mockito.when(em.createNamedQuery(AddressEntity.FIND_ALL, AddressEntity.class)).thenReturn(typeQuery);
		List<AddressDTO> expected = Arrays.asList(createAddressDTO());
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList(createAddressEntity()));

		List<AddressDTO> addressDTO = addressService.getAllAddress();
		assertEquals(expected, addressDTO);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testGetAllAddress_ShouldThrowException_WhenNothingIsGiven() {

		Mockito.when(em.createNamedQuery(AddressEntity.FIND_ALL, AddressEntity.class)).thenReturn(typeQuery);
		List<AddressDTO> expected = Arrays.asList(createAddressDTO());
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList());

		List<AddressDTO> addressDTO = addressService.getAllAddress();
		assertEquals(expected, addressDTO);
	}

	@Test
	public void testAddAddress_ShouldAddAddress_WhenAddressDtoIsGiven() {
		AddressDTO addressDTO = createAddressDTO();
		Mockito.when(em.createNamedQuery(AddressEntity.FIND_ALL_ID, AddressEntity.class)).thenReturn(typeQuery);
		Mockito.when(em.find(AddressEntity.class, addressDTO.getAddressId())).thenReturn(createAddressEntity());
		addressService.addAddress(addressDTO);
		Mockito.verify(em).persist(createAddressEntity());
	}

	@Test
	public void testUpdateAddress_ShouldUpdateAddress_WhenAddressDTOIsGiven() {
		AddressDTO addressDTO = createAddressDTO();
		Mockito.when(em.createNamedQuery(AddressEntity.FIND_ALL_ID, AddressEntity.class)).thenReturn(typeQuery);
		Mockito.when(em.find(AddressEntity.class, addressDTO.getAddressId())).thenReturn(createAddressEntity());
		addressService.update(addressDTO);
		Mockito.verify(em).merge(createAddressEntity());
	}

	@Test
	public void testDeleteAddress_ShouldDeleteAddress_WhenBuildingNumberIsGiven() {

		AddressEntity addressEntity = createAddressEntity();
		Mockito.when(em.find(AddressEntity.class, addressEntity.getAddressId())).thenReturn(addressEntity);
		Mockito.when(em.contains(addressEntity)).thenReturn(true);

		addressService.deleteAddress(addressEntity.getAddressId());

		Mockito.verify(em).remove(addressEntity);
	}

	private AddressDTO createAddressDTO() {
		return new AddressDTO(1, "hai au building", "Dien Bien Phu", "Vietnam");
	}

	private AddressEntity createAddressEntity() {
		return new AddressEntity(1, "hai au building", "Dien Bien Phu", "Vietnam");
	}

}
