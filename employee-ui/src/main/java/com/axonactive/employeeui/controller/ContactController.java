package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;
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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.apache.logging.log4j.Logger;

import com.axonactive.employeeui.dto.ContactType;
import com.axonactive.employeeui.dto.FakeContactDTO;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.helper.JavaScriptHelper;
import com.axonactive.employeeui.restclient.ContactRestClient;

import lombok.Getter;
import lombok.Setter;

@Named(value = "contactController")
@ViewScoped
public class ContactController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Getter @Setter @Valid FakeContactDTO contact = new FakeContactDTO(ContactType.EMAIL, null);

	private Map<String, ContactType> availableContactTypeMap;

	private @Getter @Setter Integer employeeId;

	@Inject
	ContactRestClient contactRestClient;

	@Inject
	Validator validator;

	@Inject
	Logger logger;

	@PostConstruct
	public void init() {
		this.availableContactTypeMap = Arrays.stream(ContactType.values())
				.collect(Collectors.toMap(ContactType::getValue, e -> e));
		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = paramMap.get("eid");
		this.employeeId = Integer.valueOf(id);
	}

	public void addContact() throws IOException {
		try {
			contactRestClient.addNewAdditionalContact(this.employeeId, contact);
			closeAddContactDialog();
		} catch (SystemException exception) {
			showDuplicationErrorMessage(exception.getMessage());
		}
	}

	public List<Entry<String, ContactType>> getAvailableContactTypes() {
		return new ArrayList<>(this.availableContactTypeMap.entrySet());
	}

	public void closeAddContactDialog() {
		JavaScriptHelper.hideDialog("addContactDialog");
	}

	public void clearContactDialog() {
		contact = new FakeContactDTO(ContactType.EMAIL, null);
	}

	public void getTypeValue() {
		contact.getType();
	}

	public void onContactChanged() {
		getTypeValue();
		Set<ConstraintViolation<FakeContactDTO>> violations = validator.validate(contact);
		if (!violations.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("addContactForm:contactValue",
					new FacesMessage(violations.iterator().next().getMessage()));
		}
	}

	private void showDuplicationErrorMessage(String duplicateErrorMessage) {
		FacesContext.getCurrentInstance().addMessage("addContactForm:contactValue",
				new FacesMessage(duplicateErrorMessage));
	}
}
