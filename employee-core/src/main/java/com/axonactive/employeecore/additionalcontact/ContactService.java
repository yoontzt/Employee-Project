package com.axonactive.employeecore.additionalcontact;

import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.axonactive.employeecore.employee.EmployeeEntity;
import com.axonactive.employeecore.employee.EmployeeService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.presistence.GenericService;

@Stateless
public class ContactService extends GenericService<ContactEntity> {

	private static final String CONTACT_EXISTED = "Contact information is already existed ! Please use another one.";

	private static final String CONTACT_NOT_FOUND = "Contact is not found! Please check id again.";

	@EJB
	private EmployeeService employeeService;

	private ContactTypeConverter contactTypeConverter = new ContactTypeConverter();

	public ContactDTO findAContactOfEmployee(Integer employeeId, Integer contactId) {
		employeeService.findEmployeeEntityById(employeeId);
		ContactEntity contactEntity = em.createNamedQuery(ContactEntity.FIND_CONTACT_OF_EMPLOYEE, ContactEntity.class)
				.setParameter("employeeId", employeeId).setParameter("contactId", contactId).getResultList().stream()
				.findFirst().orElse(null);
		if (contactEntity == null) {
			throw new EntityNotFoundException(CONTACT_NOT_FOUND);
		}
		return ContactConverter.getInstance().toDTO(contactEntity);
	}

	public List<ContactDTO> findAllContactByEmployeeId(int employeeId) {
		employeeService.findEmployeeEntityById(employeeId);
		TypedQuery<ContactEntity> typedQuery = em
				.createNamedQuery(ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, ContactEntity.class)
				.setParameter("employeeId", employeeId);
		List<ContactEntity> contactEntities = typedQuery.getResultList();
		return ContactConverter.getInstance().toListDTO(contactEntities);
	}

	public Integer addContact(Integer employeeId, ContactDTO contactDTO) {
		validateDuplication(contactDTO, employeeId);
		ContactEntity contactEntity = ContactConverter.getInstance().toEntity(contactDTO);
		setAttribute(contactEntity, contactDTO, employeeId);
		this.save(contactEntity);
		return contactEntity.getContactId();
	}

	public void updateContact(ContactDTO contactDTO, Integer employeeId) {
		validateDuplicationUpdate(contactDTO, employeeId);
		ContactEntity contactEntity = findContactEntityById(contactDTO.getContactId());
		setAttribute(contactEntity, contactDTO, employeeId);
		this.update(contactEntity);
	}

	private void setAttribute(ContactEntity contactEntity, ContactDTO contactDTO, Integer employeeId) {
		contactEntity.setContactId(contactDTO.getContactId());
		contactEntity.setType(contactTypeConverter.convertToEntityAttribute(contactDTO.getType()));
		contactEntity.setValue(contactDTO.getValue());
		contactEntity.setEmployeeEntity(employeeService.findEmployeeEntityById(employeeId));
	}

	public void deleteContact(int contactId, int employeeId) {
		this.remove(ContactConverter.getInstance().toEntity(findAContactOfEmployee(employeeId, contactId)));
	}

	// not work have to check again
	private void validateDuplicationUpdate(ContactDTO contactDTO, Integer employeeId) {
		EmployeeEntity employeeEntity = employeeService.findEmployeeEntityById(employeeId);
		if (isContactDuplicate(contactDTO, employeeEntity)) {
			throw new ValidationException(CONTACT_EXISTED);
		}
	}

	// Just work in contact table not check with the employee table (have to check
	// again)
	private void validateDuplication(ContactDTO contactDTO, Integer employeeId) {
		EmployeeEntity employeeEntity = employeeService.findEmployeeEntityById(employeeId);
		if (isContactDuplicate(contactDTO, employeeEntity)) {
			throw new ValidationException(CONTACT_EXISTED);
		} else {
			List<ContactDTO> listContact = findAllContactByEmployeeId(employeeId);
			for (ContactDTO item : listContact) {
				if (item.getValue().equalsIgnoreCase(contactDTO.getValue())) {
					throw new ValidationException(CONTACT_EXISTED);
				}
			}
		}
	}

	private ContactEntity findContactEntityById(int contactID) {
		ContactEntity contactEntity = em.find(ContactEntity.class, contactID);
		if (Objects.isNull(contactEntity)) {
			throw new EntityNotFoundException("Contact list not found");
		}
		return contactEntity;
	}

	private boolean isContactDuplicate(ContactDTO contactDTO, EmployeeEntity employeeEntity) {
		return (contactDTO.getValue().equalsIgnoreCase(employeeEntity.getPrimaryEmail())
				|| contactDTO.getValue().equalsIgnoreCase(employeeEntity.getSkype())
				|| contactDTO.getValue().equalsIgnoreCase(employeeEntity.getMobilePhone()));
	}
}
