package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Validator;

import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import com.axonactive.employeeui.dto.CountryList;
import com.axonactive.employeeui.dto.FakeAddressDTO;
import com.axonactive.employeeui.dto.FakeCertificateDTO;
import com.axonactive.employeeui.dto.FakeDepartmentDTO;
import com.axonactive.employeeui.dto.FakeEmployeeDTO;
import com.axonactive.employeeui.dto.TestContactDTO;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.helper.JavaScriptHelper;
import com.axonactive.employeeui.restclient.ArticleRestClient;
import com.axonactive.employeeui.restclient.CertificateRestClient;
import com.axonactive.employeeui.restclient.ContactRestClient;
import com.axonactive.employeeui.restclient.DepartmentRestClient;
import com.axonactive.employeeui.restclient.EmployeeRestClient;

import lombok.Getter;
import lombok.Setter;

@Named(value = "updateEmployeeController")
@ViewScoped
public class UpdateEmployeeController implements Serializable {

	public static final String MAIN_PAGE = "employee-list.xhtml";

	private static final long serialVersionUID = 6496868065695262188L;

	private transient @Getter @Setter FakeEmployeeDTO employee = new FakeEmployeeDTO();

	private transient @Getter @Setter FakeDepartmentDTO department = new FakeDepartmentDTO();

	private @Getter @Setter List<TestContactDTO> contactList = new ArrayList<>();

	private transient @Getter @Setter List<FakeCertificateDTO> certificateList = new ArrayList<>();

	private Map<String, CountryList> availableCountryList;

	private transient @Getter @Setter List<FakeDepartmentDTO> departmentList = new ArrayList<>();

	private transient @Getter @Setter FakeAddressDTO address = new FakeAddressDTO();

	private @Getter @Setter boolean showValue = true;

	private @Getter @Setter int employeeId;

	private @Getter @Setter String stringEmpId;
	
	private @Getter @Setter List<String> locations;
	
	private @Getter @Setter List<String> fileNames;

	@Inject
	EmployeeRestClient empRestClient;

	@Inject
	Validator validator;

	@EJB
	DepartmentRestClient deptRestClient;

	@EJB
	ContactRestClient contactRestClient;

	@EJB
	CertificateRestClient certificateRestClient;

	@Inject
	ArticleRestClient articleRestClient;

	@Inject
	Logger logger;

	@PostConstruct
	public void init() {
		hideIcon();
		try {
			Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext()
					.getRequestParameterMap();
			stringEmpId = paramMap.get("eid");
			this.employeeId = Integer.valueOf(stringEmpId);
			employee = empRestClient.getEmployeeByID(employeeId);
			address = employee.getAddress();
			department = deptRestClient.getDepartmentByID(employee.getDepartment().getDepartmentId());
			contactList = contactRestClient.getAllContactListOfEmployee(employeeId);
			certificateList = certificateRestClient.getCertificateListOfOneEmployee(employeeId);
			hideHeaderWhenCertiListIsEmpty(certificateList);
			this.availableCountryList = Arrays.stream(CountryList.values())
					.collect(Collectors.toMap(CountryList::getValue, e -> e));
			departmentList = deptRestClient.getAllDepartmentList();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void openAddContactDialog() {
		JavaScriptHelper.showDialog("addContactDialog");
	}

	public void openAddCertificateDialog() {
		hideHeaderWhenCertiListIsEmpty(certificateList);
		JavaScriptHelper.showDialog("addCertificateDialog");
	}

	public void update() {
		try {
			employee.setDepartment(department);
			employee.setAddress(address);
			empRestClient.updateEmployee(employee);
			this.showValue = true;
			hideIcon();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeShowValue() {
		this.showValue = !this.showValue;
		hideIcon();
		hideHeaderWhenCertiListIsEmpty(this.certificateList);
	}

	public String goToMainPage() {
		return MAIN_PAGE;
	}

	public void cancel() throws IOException {
		PrimeFaces.current().resetInputs("updateEmployeeForm");
		setEmployee(empRestClient.getEmployeeByID(employeeId));
		this.showValue = true;
		hideIcon();
	}

	public void onDateSelect(SelectEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		facesContext.addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
	}

	public void changeCountry(ValueChangeEvent country) {
		employee.getAddress().setCountry(country.toString());
	}

	public void changeDepartment(ValueChangeEvent dept) {
		try {
			department = deptRestClient.getDepartmentByID(Integer.parseInt(dept.getNewValue().toString()));
		} catch (IllegalArgumentException | IOException exception) {
			throw new SystemException("Change Deparment is failed");
		}
	}

	public void deleteContact(Integer contactId) {
		try {
			contactRestClient.deleteContact(this.employeeId, contactId);
			contactList = contactRestClient.getAllContactListOfEmployee(employeeId);
		} catch (IOException exception) {
			logger.error("Deleted contact failed.", exception);
		}
	}

	public void deleteCertificate(Integer certificateId) {
		try {
			certificateRestClient.deleteCertificate(this.employeeId, certificateId);
			certificateList = certificateRestClient.getCertificateListOfOneEmployee(this.employeeId);
			hideHeaderWhenCertiListIsEmpty(certificateList);
		} catch (IOException exception) {
			logger.error("Deleted contact failed.", exception);
		}
	}

	public List<Entry<String, CountryList>> getAvailableCountry() {
		return new ArrayList<>(this.availableCountryList.entrySet());
	}

	public void hideIcon() {
		if (this.showValue) {
			PrimeFaces.current().executeScript("hideIcons();");
		}
	}

	public void hideHeaderWhenCertiListIsEmpty(List<FakeCertificateDTO> list) {
		if (list.isEmpty()) {
			PrimeFaces.current().executeScript("hideCertificateHeader();");
		}
	}

	public void reloadContactInformation() {
		try {
			contactList = contactRestClient.getAllContactListOfEmployee(this.employeeId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void reloadCertificateInformation() {
		try {
			certificateList = certificateRestClient.getCertificateListOfOneEmployee(this.employeeId);
			hideHeaderWhenCertiListIsEmpty(certificateList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getEmployeeData() throws IOException {
		this.employee = empRestClient.getEmployeeByID(this.employeeId);
	}

	public void saveFileToBackEnd(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		String fileLocation= articleRestClient.saveFileToBackEnd(uploadedFile, this.stringEmpId);
		System.out.println(fileLocation);
	}
}
