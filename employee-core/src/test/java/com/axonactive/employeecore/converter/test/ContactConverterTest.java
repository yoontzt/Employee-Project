package com.axonactive.employeecore.converter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.additionalcontact.ContactConverter;
import com.axonactive.employeecore.additionalcontact.ContactDTO;
import com.axonactive.employeecore.additionalcontact.ContactEntity;
import com.axonactive.employeecore.additionalcontact.ContactType;
import com.axonactive.employeecore.additionalcontact.ContactTypeConverter;
import com.axonactive.employeecore.address.AddressEntity;
import com.axonactive.employeecore.certificate.CertificateEntity;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.employee.EmployeeEntity;

@RunWith(PowerMockRunner.class)
public class ContactConverterTest {

	@InjectMocks
	ContactConverter contactConverter;
	ContactEntity contactEntity = createContactEntity();

	ContactDTO contactDTO = createContactDTO();

	EmployeeEntity employee;

	AddressEntity address;

	List<CertificateEntity> certificateEntities;

	List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());

	ContactTypeConverter contactTypeConverter;
	DepartmentEntity department;

	@Test
	public void testToEntity_ShouldReturnNull_WhenContactDTOIsGiven() {
		ContactEntity actual = ContactConverter.getInstance().toEntity(null);
		assertNull(actual);
	}

	@Test
	public void testToDto_ShouldReturnNull_WhenContactEntityIsGiven() {
		ContactDTO actual = ContactConverter.getInstance().toDTO(null);
		assertNull(actual);
	}

	@Test
	public void testToListEntity_ShouldReturnEmpty_WhenListContactDTOIsGiven() {
		List<ContactEntity> contactEntities = contactConverter.toListEntity(null);
		assertEquals(Arrays.asList(), contactEntities);
		contactConverter.toListEntity(Arrays.asList());
	}

	@Test
	public void testToListDTO_ShouldReturnListDTO_WhenListContactDTOIsGiven() {
		List<ContactDTO> contactDTOs = contactConverter.toListDTO(Arrays.asList(createContactEntity()));
		assertEquals(Arrays.asList(createContactDTO()), contactDTOs);
		contactConverter.toListDTO(Arrays.asList(createContactEntity()));
	}

	@Test
	public void testToListDTO_ShouldReturnEmpty_WhenListContactEntityIsGiven() {
		List<ContactDTO> contactDTOs = contactConverter.toListDTO(null);
		assertEquals(Arrays.asList(), contactDTOs);
		contactConverter.toListDTO(Arrays.asList());
	}

	private ContactDTO createContactDTO() {
		return new ContactDTO(1, "Email", "hung@gmail.com");
	}

	private ContactEntity createContactEntity() {
		return new ContactEntity(1, ContactType.EMAIL, "hung@gmail.com", createEmployeeEntity());
	}

	private EmployeeEntity createEmployeeEntity() {
		return new EmployeeEntity(1, "thinzar", "Yoon", createDate(), "viet nam", null, null, null, null, address,
				certificateEntities, contactEntities, department);
	}

	private Date createDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dateInString = "07/06/2013";
		try {
			return formatter.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
