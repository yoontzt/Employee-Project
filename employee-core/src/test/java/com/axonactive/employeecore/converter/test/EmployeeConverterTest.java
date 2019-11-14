
package com.axonactive.employeecore.converter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;

import com.axonactive.employeecore.additionalcontact.ContactEntity;
import com.axonactive.employeecore.additionalcontact.ContactType;
import com.axonactive.employeecore.address.AddressDTO;
import com.axonactive.employeecore.address.AddressEntity;
import com.axonactive.employeecore.certificate.CertificateEntity;
import com.axonactive.employeecore.department.DepartmentDTO;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.employee.BasicEmployeeDTO;
import com.axonactive.employeecore.employee.EmployeeConverter;
import com.axonactive.employeecore.employee.EmployeeEntity;

@RunWith(PowerMockRunner.class)
public class EmployeeConverterTest {

	@InjectMocks
	EmployeeConverter employeeConverter;

	List<CertificateEntity> certificateEntities = Arrays.asList(createCertificateEntity());
	List<CertificateEntity> certificateEntitiesEmpty;
	List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());
	List<ContactEntity> contactEntitiesEmpty;
	BasicEmployeeDTO employeeDTO = createEmployeeDTO();
	EmployeeEntity employeeEntity = createEmployeeEntity();

	@Test
	public void testToEntity_shouldReturnEntity_WhenDTOIsGiven() {
		EmployeeEntity actual = employeeConverter.toEntity(employeeDTO);
		assertEquals(createEmployeeEntity(), actual);
	}

	@Test
	public void testToDTO_shouldReturnDTO_WhenEntityIsGiven() {

		BasicEmployeeDTO actual = employeeConverter.toDTO(employeeEntity);
		assertEquals(createEmployeeDTO(), actual);
	}

	@Test
	public void testToEntity_ShouldReturnNull_WhenDTOisNull() {
		EmployeeEntity actual = employeeConverter.toEntity(null);
		assertNull(actual);
	}

	@Test
	public void testToDTO_ShouldReturnNull_WhenEntityIsNull() {
		BasicEmployeeDTO actual = employeeConverter.toDTO(null);
		assertNull(actual);
	}

	@Test
	public void testToDTOs_ShouldReturnDTOEmptyList_WhenEntityListIsNull() {
		List<BasicEmployeeDTO> actual = employeeConverter.toDTOs(null);
		assertTrue(actual.isEmpty());
	}

	private BasicEmployeeDTO createEmployeeDTO() {
		Date date = createDate();
		return new BasicEmployeeDTO(1, "hung", "tran", date, "vn", "hung@gmail.com", "012345678", "hung.tran",
				"098765432", createAddressDTO(), createDepartmentDTO());
	}



	private EmployeeEntity createEmployeeEntity() {
		Date date = createDate();
		return new EmployeeEntity(1, "hung", "tran", date, "vn", "hung@gmail.com", "012345678", "hung.tran",
				"098765432", createAddressEntity(), certificateEntitiesEmpty, contactEntitiesEmpty,
				createDepartmentEntity());
	}

	private AddressDTO createAddressDTO() {
		return new AddressDTO(1, "hai au 10th floor", "truong son", "4");
	}

	private AddressEntity createAddressEntity() {
		return new AddressEntity(1, "hai au 10th floor", "truong son", "4");
	}

	private ContactEntity createContactEntity() {
		return new ContactEntity(1, ContactType.EMAIL, "hung@gmail.com", createEmployeeEntity());
	}

	private CertificateEntity createCertificateEntity() {
		return new CertificateEntity(1, "toeic", "language", "08/2014", createEmployeeEntity());
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

	private DepartmentDTO createDepartmentDTO() {
		return new DepartmentDTO(1, "ICT");
	}

	private DepartmentEntity createDepartmentEntity() {
		return new DepartmentEntity(1, "ICT");
	}

}
