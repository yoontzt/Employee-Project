package com.axonactive.employeecore.employee;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;

import com.axonactive.employeecore.additionalcontact.ContactDTO;
import com.axonactive.employeecore.additionalcontact.ContactService;
import com.axonactive.employeecore.address.AddressConverter;
import com.axonactive.employeecore.certificate.CertificateDTO;
import com.axonactive.employeecore.certificate.CertificateService;
import com.axonactive.employeecore.department.DepartmentService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.presistence.GenericService;
import com.axonactive.employeecore.util.UtfStringConverter;

@Stateless
public class EmployeeService extends GenericService<EmployeeEntity> {
	private static final String DEPARMENT_ID_NOT_MATCH = "Deparment id does not match with the department name! Please check again";

	private static final String EMPLOYEE_NOT_FOUND = "Employee is not found! Please check id again.";

	@EJB
	private DepartmentService deptService;

	@EJB
	private ContactService contactService;

	@EJB
	private CertificateService certificateService;

	private UtfStringConverter utfStringConverter = new UtfStringConverter();

	@Inject
	Validator validator;

	EmployeeConverter employeeConverter = new EmployeeConverter();

	public List<BasicEmployeeDTO> getAllEmployeeList() {
		TypedQuery<EmployeeEntity> query = em.createNamedQuery(EmployeeEntity.FIND_ALL, EmployeeEntity.class);
		List<EmployeeEntity> listEmployeeEntitiy = query.getResultList();
		return employeeConverter.toDTOs(listEmployeeEntitiy);
	}

	public EmployeeEntity findEmployeeEntityById(int id) {
		EmployeeEntity employee = em.find(EmployeeEntity.class, id);
		if (employee == null) {
			throw new EntityNotFoundException(EMPLOYEE_NOT_FOUND);
		}
		return employee;
	}

	public BasicEmployeeDTO findEmployeeById(int id) {
		BasicEmployeeDTO employee = employeeConverter.toDTO(em.find(EmployeeEntity.class, id));
		Optional<BasicEmployeeDTO> employeeOptional = Optional.ofNullable(employee);
		if (!employeeOptional.isPresent()) {
			throw new EntityNotFoundException(EMPLOYEE_NOT_FOUND);
		}
		return employeeOptional.get();
	}

	public int addEmployee(BasicEmployeeDTO employee) {
		if (employee.getEmployeeId() != null) {
			throw new ValidationException("Dont put id when add employee");
		}
		validateEmployeeUniqueAttribute(employee);
		EmployeeEntity newEntity = employeeConverter.toEntity(employee);
		this.save(newEntity);
		return newEntity.getEmployeeId();
	}

	// new function
	@Transactional(rollbackOn = { Exception.class })
	public int newAddEmployee(EmployeeDTO addEmployee) {
		validateEmployeeUniqueAttribute(addEmployee.getEmployee());
		EmployeeEntity newEntity = employeeConverter.toEntity(addEmployee.getEmployee());
		this.save(newEntity);
		int createdEmployeeId = newEntity.getEmployeeId();
		addContactList(createdEmployeeId, addEmployee.getAdditionalContacts());
		addCertificateList(createdEmployeeId, addEmployee.getCertificates());
		return createdEmployeeId;
	}

	public void updateEmployee(BasicEmployeeDTO employee) {
		if (checkEmailDuplicationForUpdate(employee)) {
			EmployeeEntity currentEntity = findEmployeeEntityById(employee.getEmployeeId());
			setAttribute(currentEntity, employee);
			this.update(currentEntity);
		} else {
			throw new ValidationException("Duplicate email !!please check again");
		}
	}

	private void setAttribute(EmployeeEntity newEntity, BasicEmployeeDTO employee) {
		newEntity.setLastname(employee.getLastname());
		newEntity.setFirstname(employee.getFirstname());
		newEntity.setDateOfBirth(employee.getDateOfBirth());
		newEntity.setNationality(employee.getNationality());
		newEntity.setPrimaryEmail(employee.getPrimaryEmail());
		newEntity.setMobilePhone(employee.getMobilePhone());
		newEntity.setSkype(employee.getSkype());
		newEntity.setEmergencyContact(employee.getEmergencyContact());
		newEntity.setAddress(AddressConverter.getInstance().toEntity(employee.getAddress()));
		newEntity.setDepartment(deptService.findDepartmentEntityById(employee.getDepartment().getDepartmentId()));
		compareDepartmentName(employee.getDepartment().getName(), newEntity.getDepartment().getName());
	}

	public void deleteEmployeeById(Integer id) {
		this.remove(findEmployeeEntityById(id));
	}

	private void compareDepartmentName(String deptName, String existedDeptName) {
		if (!deptName.equalsIgnoreCase(existedDeptName)) {
			throw new EntityNotFoundException(DEPARMENT_ID_NOT_MATCH);
		}
	}

	private boolean checkEmailDuplication(BasicEmployeeDTO employee) {
		List<EmployeeEntity> existedEntity = em.createNamedQuery(EmployeeEntity.FIND_EMAIL, EmployeeEntity.class)
				.setParameter("primaryEmail", employee.getPrimaryEmail()).getResultList();
		return existedEntity.isEmpty();
	}

	private boolean checkEmailDuplicationForUpdate(BasicEmployeeDTO employee) {
		List<EmployeeEntity> existedEntity = em
				.createNamedQuery(EmployeeEntity.FIND_EMAIL_FOR_UPDATE, EmployeeEntity.class)
				.setParameter("primaryEmail", employee.getPrimaryEmail())
				.setParameter("employeeId", employee.getEmployeeId()).getResultList();
		return existedEntity.isEmpty();
	}

	private boolean checkPhoneDuplication(BasicEmployeeDTO employee) {
		List<EmployeeEntity> existedEntity = em.createNamedQuery(EmployeeEntity.FIND_PHONE, EmployeeEntity.class)
				.setParameter("mobilePhone", employee.getMobilePhone()).getResultList();
		return existedEntity.isEmpty();
	}

	public List<BasicEmployeeDTO> checkInputAndSearch(String value) {
		String[] stored = splitString(value);
		removeAccent(stored);
		addPunctuation(stored);
		lowerCase(stored);
		List<EmployeeEntity> employeeResultList = em.createNamedQuery(EmployeeEntity.SEARCH, EmployeeEntity.class)
				.setParameter("input", stored[0]).getResultList();
		for (int i = 1; i < stored.length; i++) {
			List<EmployeeEntity> employeeList = em.createNamedQuery(EmployeeEntity.SEARCH, EmployeeEntity.class)
					.setParameter("input", stored[i]).getResultList();
			/* Using union list to intersection */
			employeeResultList.retainAll(employeeList);

			/* using stream to intersection list */
			employeeResultList.stream().filter(employeeList::contains).collect(Collectors.toList());
		}
		return employeeConverter.toDTOs(employeeResultList);
	}

	private void removeAccent(String[] input) {
		for (int i = 0; i < input.length; i++) {
			input[i] = utfStringConverter.convertUtf8String(input[i]);
		}
	}

	private void lowerCase(String[] input) {
		for (int i = 0; i < input.length; i++) {
			input[i] = input[i].toLowerCase();
		}
	}

	private void addPunctuation(String[] input) {
		for (int i = 0; i < input.length; i++) {
			input[i] = "%" + input[i] + "%";
		}
	}

	private String[] splitString(String input) {
		return input.trim().split("\\s* \\s*");

	}

	private boolean checkSkypeDuplication(BasicEmployeeDTO employee) {
		List<EmployeeEntity> listExistedEntity = em.createNamedQuery(EmployeeEntity.FIND_SKYPE, EmployeeEntity.class)
				.setParameter("skype", employee.getSkype()).getResultList();
		return listExistedEntity.isEmpty();
	}

	private void validateEmployeeUniqueAttribute(BasicEmployeeDTO empDto) {
		List<String> errorList = new ArrayList<>();
		if (!checkEmailDuplication(empDto)) {
			errorList.add("Duplicate email.");
		}
		if (!checkPhoneDuplication(empDto)) {
			errorList.add("Duplicate phone.");
		}
		if (!checkSkypeDuplication(empDto)) {
			errorList.add("Duplicate skype.");
		}
		if (!errorList.isEmpty()) {
			throw new ValidationException(StringUtils.join(errorList, " ") + "Please check again.");
		}
	}

	private void addCertificateList(int employeeId, List<CertificateDTO> listCertificate) {
		Iterator<CertificateDTO> certificateIterator = listCertificate.iterator();
		while (certificateIterator.hasNext()) {
			certificateService.addCertificate(employeeId, certificateIterator.next());
		}
	}

	private void addContactList(int employeeId, List<ContactDTO> listContact) {
		Iterator<ContactDTO> contactIterator = listContact.iterator();
		while (contactIterator.hasNext()) {
			contactService.addContact(employeeId, contactIterator.next());
		}
	}
}
