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

import com.axonactive.employeecore.certificate.CertificateConverter;
import com.axonactive.employeecore.certificate.CertificateDTO;
import com.axonactive.employeecore.certificate.CertificateEntity;
import com.axonactive.employeecore.certificate.CertificateService;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.employee.EmployeeEntity;
import com.axonactive.employeecore.employee.EmployeeService;
import com.axonactive.employeecore.exception.EntityNotFoundException;

@RunWith(PowerMockRunner.class)
public class CertificateServiceTest {

	@InjectMocks
	CertificateService certificateService;

	@Mock
	EntityManager em;

	@Mock
	CertificateConverter certificateConverter;

	@Mock
	EmployeeService employeeService;

	@Mock
	TypedQuery<CertificateEntity> query;

	@Test
	public void testFindAllCertificateByEmployeeId_ShouldReturnList_WhenEmployeeIdIsGiven() {
		List<CertificateEntity> certificateEntities = Arrays.asList(createCertificateEntity());
		List<CertificateDTO> certificateDTOs = Arrays.asList(createCertificateDTO());
		Mockito.when(
				em.createNamedQuery(CertificateEntity.FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID, CertificateEntity.class))
				.thenReturn(query);

		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);

		Mockito.when(query.getResultList()).thenReturn(certificateEntities);
		List<CertificateDTO> actual = certificateService.findAllCertificateByEmployeeId(1);

		assertEquals(certificateDTOs, actual);
	}

	@Test
	public void testFindACertificateOfEmployee_ShouldReturnCertificate_WhenTwoIdsAreGiven() {
		List<CertificateEntity> certificateEntities = Arrays.asList(createCertificateEntity());
		Mockito.when(em.createNamedQuery(CertificateEntity.FIND_CERTIFICATE_OF_EMPLOYEE, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.setParameter("certificateId", certificateEntities.get(0).getCertificateId())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(certificateEntities);

		certificateService.findACertificateOfEmployee(1, certificateEntities.get(0).getCertificateId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindACertificateOfEmployee_ShouldthrowException_WhenCertificateIdIsNotFound() {
		List<CertificateEntity> certificateEntities = Arrays.asList(createCertificateEntity());
		Mockito.when(em.createNamedQuery(CertificateEntity.FIND_CERTIFICATE_OF_EMPLOYEE, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.setParameter("certificateId", certificateEntities.get(0).getCertificateId())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());

		certificateService.findACertificateOfEmployee(1, certificateEntities.get(0).getCertificateId());
	}

	@Test
	public void testAddCertificate_ShouldAddCertificate_WhenCertificateDTOIsGiven() {
		CertificateDTO certificateDTO = new CertificateDTO(1, "java", "language", "1999");
		CertificateEntity certificateEntity = createCertificateEntity();
		Mockito.when(
				em.createNamedQuery(CertificateEntity.FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(certificateEntity));
		Mockito.when(certificateConverter.toEntity(certificateDTO)).thenReturn(certificateEntity);
		certificateService.addCertificate(1,certificateDTO);
	}
	//TODO-Check this again
	@Test(expected = Exception.class)
	public void testAddCertificate_ShouldThrowException_WhenElementIsEmpty() {
		CertificateDTO certificateDto=createCertificateDTO();
		List<CertificateDTO> certificateDTOs=Arrays.asList(createCertificateDTO());
		Mockito.when(certificateService.findAllCertificateByEmployeeId(1)).thenReturn(certificateDTOs);
		Mockito.when(certificateConverter.toEntity(certificateDto)).thenReturn(createCertificateEntity());
		certificateService.addCertificate(1, certificateDto );
		CertificateDTO certificateDTO = createCertificateDTO();
		CertificateEntity certificateEntity = createCertificateEntity();
		Mockito.when(certificateConverter.toEntity(certificateDTO)).thenReturn(certificateEntity);
		certificateService.addCertificate(1, certificateDTO);
	}

	

	@Test
	public void testUpdateCertificate_ShouldUpdateCertificate_WhenCertificateDTOIsGiven() {
		CertificateDTO certificateDTO = new CertificateDTO(1, "java", "language", "1999");
		CertificateEntity certificateEntity = createCertificateEntity();
		Mockito.when(
				em.createNamedQuery(CertificateEntity.FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(certificateEntity));
		Mockito.when(em.find(CertificateEntity.class, 1)).thenReturn(certificateEntity);
		certificateService.updateCertificate(certificateDTO, 1);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testUpdateCertificate_ShouldThrowException_WhenIdIsNotValid() {
		CertificateDTO certificateDTO = createCertificateDTO();
		CertificateEntity certificateEntity = createCertificateEntity();
		Mockito.when(
				em.createNamedQuery(CertificateEntity.FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(certificateEntity));
		Mockito.when(em.find(CertificateEntity.class, 1)).thenReturn(null);
		certificateService.updateCertificate(certificateDTO, 1);
	}

	@Test
	public void testDeleteCertificate_ShouldDeleteCertificate_WhenCertificateIdIsGiven() {
		List<CertificateEntity> certificateEntities = Arrays.asList(createCertificateEntity());
		Mockito.when(em.createNamedQuery(CertificateEntity.FIND_CERTIFICATE_OF_EMPLOYEE, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.setParameter("certificateId", certificateEntities.get(0).getCertificateId())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(certificateEntities);
		certificateService.deleteCertificate(1, certificateEntities.get(0).getCertificateId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testDeleteCertificate_ShouldThrowException_WhenIdIsNotValid() {
		List<CertificateEntity> certificateEntities = Arrays.asList(createCertificateEntity());
		Mockito.when(em.createNamedQuery(CertificateEntity.FIND_CERTIFICATE_OF_EMPLOYEE, CertificateEntity.class))
				.thenReturn(query);
		Mockito.when(query.setParameter("employeeId", 1)).thenReturn(query);
		Mockito.when(query.setParameter("certificateId", certificateEntities.get(0).getCertificateId())).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList());
		certificateService.deleteCertificate(1, certificateEntities.get(0).getCertificateId());
	}

	private DepartmentEntity createDepartmentEntity() {
		return new DepartmentEntity(1, "ICT");
	}

	private CertificateEntity createCertificateEntity() {
		DepartmentEntity departmentEntity = createDepartmentEntity();
		EmployeeEntity employeeEntity = EmployeeEntity.builder().employeeId(1).lastname("Yoon").firstname("Thin")
				.nationality("Myanamar").department(departmentEntity).build();
		CertificateEntity certificateEntity = CertificateEntity.builder().certificateId(1).nameOfCertificate("java")
				.typeOfCertificate("language").achievedTime("08/2014").employeeEntity(employeeEntity).build();
		List<CertificateEntity> certificateEntities = Arrays.asList(certificateEntity);
		employeeEntity.setCertificateEntities(certificateEntities);
		return employeeEntity.getCertificateEntities().get(0);
	}

	private CertificateDTO createCertificateDTO() {
		return CertificateDTO.builder().certificateId(1).nameOfCertificate("java").typeOfCertificate("language")
				.achievedTime("08/2014").build();
	}
}
