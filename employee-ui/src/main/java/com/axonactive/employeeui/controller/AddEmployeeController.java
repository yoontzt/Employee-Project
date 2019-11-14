package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import com.axonactive.employeeui.dto.ContactType;
import com.axonactive.employeeui.dto.CountryList;
import com.axonactive.employeeui.dto.FakeAddEmployeeDTO;
import com.axonactive.employeeui.dto.FakeAddressDTO;
import com.axonactive.employeeui.dto.FakeCertificateDTO;
import com.axonactive.employeeui.dto.FakeContactDTO;
import com.axonactive.employeeui.dto.FakeDepartmentDTO;
import com.axonactive.employeeui.dto.FakeEmployeeDTO;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.helper.JavaScriptHelper;
import com.axonactive.employeeui.restclient.CertificateRestClient;
import com.axonactive.employeeui.restclient.ContactRestClient;
import com.axonactive.employeeui.restclient.DepartmentRestClient;
import com.axonactive.employeeui.restclient.EmployeeRestClient;
import com.google.common.base.Objects;

import lombok.Getter;
import lombok.Setter;

@Named(value = "addEmployeeController")
@ViewScoped
public class AddEmployeeController implements Serializable {

	private static final long serialVersionUID = 7889451587603628722L;

	private @Getter @Setter FakeAddEmployeeDTO addEmployee = new FakeAddEmployeeDTO();

	private @Getter @Setter FakeEmployeeDTO employee = new FakeEmployeeDTO();

	private transient @Getter @Setter FakeDepartmentDTO department = new FakeDepartmentDTO();

	private transient @Getter @Setter FakeAddressDTO address = new FakeAddressDTO();

	private transient @Getter @Setter List<FakeDepartmentDTO> departmentList = new ArrayList<>();

	private transient @Getter @Setter List<FakeCertificateDTO> certificateList;

	private transient @Getter @Setter List<FakeContactDTO> contactList;

	private transient @Getter @Setter List<CountryList> countryLists = Arrays.asList(CountryList.values());

	private transient @Getter @Setter List<FakeEmployeeDTO> employeeSearchList = new ArrayList<>();

	private transient @Getter @Setter List<FakeEmployeeDTO> employeeList = new ArrayList<>();

	private Map<String, ContactType> availableContactTypeMap;

	private Map<String, CountryList> availableCountryList;

	@Inject
	EmployeeRestClient empRestClient;

	@Inject
	Validator validator;

	@Inject
	DepartmentRestClient deptRestClient;

	@Inject
	ContactRestClient contactRestClient;

	@Inject
	CertificateRestClient certificateRestClient;

	@Inject
	EmployeeRestClient employeeRestClient;

	private EmployeeController employeeController;

	private boolean duplicate;

	@Inject
	Logger logger;

	private @Getter @Setter List<String> contactErrorMsgList;

	private @Getter @Setter List<String> certificateErrorMsgList;

	private @Getter @Setter List<String> certificateNameErrorMsgList;

	@PostConstruct
	public void init() {
		try {
			Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			employeeController = (EmployeeController) sessionMap.get("employeeController");
			setEmployeeSearchList(employeeController.getEmployeeList());
			this.availableContactTypeMap = Arrays.stream(ContactType.values())
					.collect(Collectors.toMap(ContactType::getValue, e -> e));
			this.availableCountryList = Arrays.stream(CountryList.values())
					.collect(Collectors.toMap(CountryList::getValue, e -> e));
			duplicate = false;
			departmentList = deptRestClient.getAllDepartmentList();
			this.contactList = new ArrayList<>();
			this.certificateList = new ArrayList<>();
			this.contactErrorMsgList = new ArrayList<>();
			this.certificateErrorMsgList = new ArrayList<>();
			this.certificateNameErrorMsgList = new ArrayList<>();
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public void addNewEmployee() {
		try {
			employee.setDepartment(department);
			employee.setAddress(address);
			addEmployee.setEmployee(employee);
			addEmployee.setAdditionalContacts(contactList);
			addEmployee.setCertificates(certificateList);
			empRestClient.addNewEmployee(addEmployee);
			JavaScriptHelper.showSuccessMessage("New Employee is successfully created");
			PrimeFaces.current().executeScript("goToMainPage();");
			clearWholeForm();
		} catch (IllegalArgumentException | IOException exception) {
			logger.error("Added new employee into the system failed on UI.", exception);
		} catch (SystemException exception) {
			logger.error("Added new employee into the system failed.");
		}
	}

	public void cancelOnAdd() {
		PrimeFaces.current().executeScript("reload();");
	}

	public void onDateSelect(SelectEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		facesContext.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
	}

	public void changeCountry(ValueChangeEvent country) {
		address.setCountry(country.toString());
	}

	public void changeDepartment(ValueChangeEvent dept) {
		try {
			department = deptRestClient.getDepartmentByID(Integer.parseInt(dept.getNewValue().toString()));
		} catch (IllegalArgumentException | IOException exception) {
			throw new SystemException("Change Deparment is failed");
		}
	}

	public void clearWholeForm() {
		department = new FakeDepartmentDTO();
		address = new FakeAddressDTO();
		employee = new FakeEmployeeDTO();
		contactList = new ArrayList<>();
		certificateList = new ArrayList<>();
	}

	public void checkDuplication() {
		try {
			employeeList = employeeRestClient.getAllEmployeeList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String email = employee.getPrimaryEmail();
		String skype = employee.getSkype();
		String phone = employee.getMobilePhone();
		for (FakeEmployeeDTO employeeDTO : employeeList) {
			if (employeeDTO.getPrimaryEmail().equalsIgnoreCase(email)) {
				showDuplicationErrorMesg("email");
			}
			if (employeeDTO.getSkype().equalsIgnoreCase(skype)) {
				showDuplicationErrorMesg("skype");
			}
			if (employeeDTO.getMobilePhone().equalsIgnoreCase(phone)) {
				showDuplicationErrorMesg("phone");
			}
		}

	}

	private void showDuplicationErrorMesg(String componentId) {
		FacesContext.getCurrentInstance().addMessage("addEmployeeForm:" + componentId,
				new FacesMessage("This " + componentId + " is already existed! Try with another."));
	}

	public void addNewContact() {
		contactErrorMsgList.add(0, "");
		contactList.add(0, new FakeContactDTO(ContactType.EMAIL, null));
	}

	public void addNewCertificate() {
		certificateErrorMsgList.add(0, "");
		certificateNameErrorMsgList.add(0, "");
		certificateList.add(0, new FakeCertificateDTO());
	}

	public void removeContact(int position) {
		if (!contactList.isEmpty()) {
			contactList.remove(position);
			contactErrorMsgList.remove(position);
		}
	}

	public void removeCertificate(int position) {
		if (!certificateList.isEmpty()) {
			certificateList.remove(position);
			certificateErrorMsgList.remove(position);
			certificateNameErrorMsgList.remove(position);
		}
	}

	public void onContactChanged(int position) {
		FakeContactDTO currentContact = contactList.get(position);
		Set<ConstraintViolation<FakeContactDTO>> violations = validator.validate(currentContact);
		String errorMessage = "";

		List<FakeContactDTO> copiedAddedContacts = new ArrayList<>(this.contactList);
		copiedAddedContacts.remove(position);

		List<FakeContactDTO> duplicateContact = copiedAddedContacts.stream()
				.filter(e -> Objects.equal(e, currentContact)).collect(Collectors.toList());

		if (!duplicateContact.isEmpty()) {
			errorMessage = "This contact is duplicated";
		}
		if (!violations.isEmpty()) {
			errorMessage = violations.iterator().next().getMessage();
		}
		this.contactErrorMsgList.set(position, errorMessage);
	}

	public void onCeritficateChanged(int position) {
		FakeCertificateDTO currentcertificate = certificateList.get(position);
		Set<ConstraintViolation<FakeCertificateDTO>> violations = validator.validate(currentcertificate);
		String errorMessage = " ";
		String nameMessage = " ";

		List<FakeCertificateDTO> copiedAddedCertificates = new ArrayList<>(this.certificateList);
		copiedAddedCertificates.remove(position);

		List<FakeCertificateDTO> duplicateCertificate = copiedAddedCertificates.stream()
				.filter(e -> e.getNameOfCertificate().equalsIgnoreCase(currentcertificate.getNameOfCertificate()))
				.filter(e -> e.getAchievedTime().equalsIgnoreCase(currentcertificate.getAchievedTime()))
				.collect(Collectors.toList());

		if (!duplicateCertificate.isEmpty()) {
			errorMessage = "This certificate is duplicated !! Please check Name and Achieved time of certificate";
		}

		if (!violations.isEmpty()) {
			nameMessage = violations.iterator().next().getMessage();
			if (nameMessage.contains("100")) {
				this.certificateErrorMsgList.set(position, nameMessage);
			} else if (nameMessage.contains("255")) {
				errorMessage = nameMessage;
				nameMessage = " ";
				this.certificateNameErrorMsgList.set(position, errorMessage);
			}
		}
		this.certificateNameErrorMsgList.set(position, errorMessage);
		this.certificateErrorMsgList.set(position, nameMessage);
	}

	public List<Entry<String, ContactType>> getAvailableContactTypes() {
		return new ArrayList<>(this.availableContactTypeMap.entrySet());
	}

	public List<Entry<String, CountryList>> getAvailableCountry() {
		return new ArrayList<>(this.availableCountryList.entrySet());
	}
}
