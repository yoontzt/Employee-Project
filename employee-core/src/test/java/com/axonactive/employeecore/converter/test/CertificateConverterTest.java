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
import com.axonactive.employeecore.address.AddressEntity;
import com.axonactive.employeecore.certificate.CertificateConverter;
import com.axonactive.employeecore.certificate.CertificateDTO;
import com.axonactive.employeecore.certificate.CertificateEntity;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.employee.EmployeeEntity;

@RunWith(PowerMockRunner.class)
public class CertificateConverterTest {

	@InjectMocks
	CertificateConverter certificateConverter;

	CertificateEntity certificateEntity = createCertificateEntity();

	CertificateDTO certificateDTO = createCertificateDTO();

	EmployeeEntity employee;

	AddressEntity address;

	List<CertificateEntity> certificateEntities;

	List<ContactEntity> contactEntities;

	DepartmentEntity department;

	@Test
	public void testToEntity_ShouldReturnCertificateEntity_WhenCertificateDTOIsGiven() {
		CertificateEntity actual = CertificateConverter.getInstance().toEntity(certificateDTO);
		assertEquals(createCertificateEntityWithNoEmployeeInfo(), actual);
	}

	@Test
	public void testToEntity_ShouldReturnNull_WhenCertificateDTOIsGiven() {
		CertificateEntity actual = CertificateConverter.getInstance().toEntity(null);
		assertNull(actual);
	}

	@Test
	public void testToDTO_ShouldReturnCertificateDTO_WhenCertificateEntityIsGiven() {
		CertificateDTO actual = CertificateConverter.getInstance().toDTO(certificateEntity);
		assertEquals(certificateDTO, actual);
	}

	@Test
	public void testToDTO_ShouldReturnNull_WhenCertificateEntityIsGiven() {
		CertificateDTO actual = CertificateConverter.getInstance().toDTO(null);
		assertNull(actual);
	}

	@Test
	public void testToListEntity_ShouldReturnListCertificateEntity_WhenListCertificateDTOIsGiven() {
		List<CertificateEntity> actual = CertificateConverter.getInstance().toListEntity(Arrays.asList(certificateDTO));
		assertEquals(Arrays.asList(createCertificateEntityWithNoEmployeeInfo()), actual);
	}

	@Test
	public void testToListEntity_ShouldReturnEmpty_WhenListCertificateDTOIsGiven() {
		List<CertificateEntity> actual = CertificateConverter.getInstance().toListEntity(null);
		assertTrue(actual.isEmpty());
	}

	@Test
	public void testToListDTO_ShouldReturnListCertificateDTO_WhenListCertificateEntityIsGiven() {
		List<CertificateDTO> actual = CertificateConverter.getInstance().toListDTO(Arrays.asList(certificateEntity));
		assertEquals(Arrays.asList(certificateDTO), actual);
	}

	@Test
	public void testToListDTO_ShouldReturnEmpty_WhenListCertificateEntityIsGiven() {
		List<CertificateDTO> actual = CertificateConverter.getInstance().toListDTO(null);
		assertTrue(actual.isEmpty());
	}

	private CertificateDTO createCertificateDTO() {
		return new CertificateDTO(1, "toeic", "language", "08/2014");
	}

	private CertificateEntity createCertificateEntity() {
		return new CertificateEntity(1, "toeic", "language", "08/2014", createEmployeeEntity());
	}

	private CertificateEntity createCertificateEntityWithNoEmployeeInfo() {
		return new CertificateEntity(1, "toeic", "language", "08/2014", employee);
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
