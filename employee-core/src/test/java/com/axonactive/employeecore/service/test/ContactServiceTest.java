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

import com.axonactive.employeecore.additionalcontact.ContactConverter;
import com.axonactive.employeecore.additionalcontact.ContactDTO;
import com.axonactive.employeecore.additionalcontact.ContactEntity;
import com.axonactive.employeecore.additionalcontact.ContactService;
import com.axonactive.employeecore.additionalcontact.ContactType;
import com.axonactive.employeecore.department.DepartmentEntity;
import com.axonactive.employeecore.employee.EmployeeEntity;
import com.axonactive.employeecore.employee.EmployeeService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;

@RunWith(PowerMockRunner.class)
public class ContactServiceTest {

	@InjectMocks
	ContactService contactService;

	@Mock
	ContactConverter contactConverter;

	@Mock
	TypedQuery<ContactEntity> typeQuery;

	@Mock
	EmployeeService employeeService;

	@Mock
	EntityManager em;

	@Test
	public void testFindContactById_ShouldReturnContactDTO_WhenEmployeeIdAndContactIdIsGiven() {
		List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_CONTACT_OF_EMPLOYEE, ContactEntity.class))
				.thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("contactId", contactEntities.get(0).getContactId())).thenReturn(typeQuery);

		Mockito.when(typeQuery.getResultList()).thenReturn(contactEntities);
		contactService.findAContactOfEmployee(1, contactEntities.get(0).getContactId());
	}

	@Test(expected = EntityNotFoundException.class)
	public void testFindContactById_ShouldThrowException_WhenEmployeeIdAndContactIdIsGiven() {
		List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_CONTACT_OF_EMPLOYEE, ContactEntity.class))
				.thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("contactId", contactEntities.get(0).getContactId())).thenReturn(typeQuery);

		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList());
		contactService.findAContactOfEmployee(1, contactEntities.get(0).getContactId());
	}

	@Test
	public void testFindAllContactByEmployeeId_ShouldReturnListContactDTO_WhenEmployeeIDIsGiven() {
		List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());

		List<ContactDTO> expected = Arrays.asList(createContactDTO());

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class))
				.thenReturn(typeQuery);
		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);

		Mockito.when(typeQuery.getResultList()).thenReturn(contactEntities);

		List<ContactDTO> actual = contactService.findAllContactByEmployeeId(1);

		assertEquals(expected, actual);
	}

	@Test
	public void testFindAllContactByEmployeeId_ShouldReturnContactDtoList_WhenEmployeeIdIsGiven() {
		Mockito.when(em.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class))
				.thenReturn(typeQuery);
		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList(createContactEntity()));
		contactService.findAllContactByEmployeeId(1);
	}

	@Test
	public void testAddContact_ShouldAddContact_WhenContcatDtoIsGiven() {
		ContactDTO contactDto = createContactDTO();
		ContactDTO contactDTO2 = new ContactDTO(2, "Email", "aa@gmail.com");
		EmployeeEntity employeeEntity = createEmployeeEntity();

		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class))
				.thenReturn(typeQuery);
		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList(createContactEntity()));
		Mockito.when(contactConverter.toEntity(contactDto)).thenReturn(createContactEntity());

		contactService.addContact(1,contactDTO2);
	}

	@Test(expected = ValidationException.class)
	public void testAddContact_ShouldThrowException_WhenContatcInformationIsSame() {
		ContactDTO contactDto = createContactDTO();
		EmployeeEntity employeeEntity = createEmployeeEntity();
		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class))
				.thenReturn(typeQuery);
		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList(createContactEntity()));
		Mockito.when(contactConverter.toEntity(contactDto)).thenReturn(createContactEntity());
		contactService.addContact(1,contactDto);
	}


	@Test
	public void testUpdateContact_ShouldUpdateContactEntity_WhenContactDTOIsGiven() {
		ContactDTO contactDto = new ContactDTO(1, "Email", "aa@gmail.com");
		ContactEntity entity = createContactEntity();
		EmployeeEntity employeeEntity = createEmployeeEntity();

		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class))
				.thenReturn(typeQuery);
		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList(createContactEntity()));
		Mockito.when(em.find(ContactEntity.class, 1)).thenReturn(entity);
		contactService.updateContact(contactDto, 1);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testUpdateContact_ShouldThrowException_WhenContactIdIsNotValid() {
		ContactDTO contactDto = new ContactDTO(1, "Email", "aa@gmail.com");
		EmployeeEntity employeeEntity = createEmployeeEntity();

		Mockito.when(employeeService.findEmployeeEntityById(1)).thenReturn(employeeEntity);
		Mockito.when(em.find(EmployeeEntity.class, 1)).thenReturn(employeeEntity);

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class))
				.thenReturn(typeQuery);
		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);
		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList(createContactEntity()));
		Mockito.when(em.find(ContactEntity.class, 1)).thenReturn(null);
		contactService.updateContact(contactDto, 1);
	}

	@Test
	public void testDeleteContact_ShouldDeleteContactEntity_WhenContactDTOIsGiven() {
		List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_CONTACT_OF_EMPLOYEE, ContactEntity.class))
				.thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("contactId", contactEntities.get(0).getContactId())).thenReturn(typeQuery);

		Mockito.when(typeQuery.getResultList()).thenReturn(contactEntities);
		contactService.deleteContact(contactEntities.get(0).getContactId(), 1);
	}

	@Test(expected = EntityNotFoundException.class)
	public void testDeleteContact_ShouldThrowValidationException_WhenNonExistingContactIDIsGiven() {
		List<ContactEntity> contactEntities = Arrays.asList(createContactEntity());

		Mockito.when(em.createNamedQuery(ContactEntity.FIND_CONTACT_OF_EMPLOYEE, ContactEntity.class))
				.thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("employeeId", 1)).thenReturn(typeQuery);

		Mockito.when(typeQuery.setParameter("contactId", contactEntities.get(0).getContactId())).thenReturn(typeQuery);

		Mockito.when(typeQuery.getResultList()).thenReturn(Arrays.asList());
		contactService.deleteContact(contactEntities.get(0).getContactId(), 1);
	}

	private ContactDTO createContactDTO() {
		return ContactDTO.builder().contactId(1).type("Email").value("gg@gmail.com").build();
	}

	private ContactEntity createContactEntity() {
		DepartmentEntity departmentEntity = createDepartmentEntity();
		EmployeeEntity employeeEntity = EmployeeEntity.builder().employeeId(1).lastname("Yoon").firstname("Thin")
				.nationality("Myanamar").department(departmentEntity).build();

		ContactEntity contactEntity = ContactEntity.builder().contactId(1).type(ContactType.EMAIL).value("gg@gmail.com")
				.employeeEntity(employeeEntity).build();
		List<ContactEntity> contactEntities = Arrays.asList(contactEntity);
		employeeEntity.setContactEntities(contactEntities);

		return employeeEntity.getContactEntities().get(0);
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
