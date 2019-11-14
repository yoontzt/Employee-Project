package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.Validator;

import com.axonactive.employeeui.dto.FakeCertificateDTO;
import com.axonactive.employeeui.exception.SystemException;
import com.axonactive.employeeui.helper.JavaScriptHelper;
import com.axonactive.employeeui.restclient.CertificateRestClient;

import lombok.Getter;
import lombok.Setter;

@Named(value = "certificateController")
@ViewScoped
public class CertificateController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Getter @Setter @Valid FakeCertificateDTO certificate = new FakeCertificateDTO();

	private @Getter @Setter Integer employeeId;

	@Inject
	CertificateRestClient certificateRestClient;

	@Inject
	Validator validator;

	@PostConstruct
	public void init() {
		Map<String, String> paramMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = paramMap.get("eid");
		this.employeeId = Integer.valueOf(id);
	}

	public void addCertificate() throws IOException {
		try {
			certificateRestClient.addNewCertificate(this.employeeId, certificate);
			closeAddCertificateDialog();
		} catch (SystemException exception) {
			showDuplicationErrorMessage(exception.getMessage());
		}
	}

	public void closeAddCertificateDialog() {
		JavaScriptHelper.hideDialog("addCertificateDialog");
	}

	public void clearCertificateDialog() {
		certificate = new FakeCertificateDTO();
	}

	private void showDuplicationErrorMessage(String duplicateErrorMessage) {
		FacesContext.getCurrentInstance().addMessage("addCertificateForm:certificateName",
				new FacesMessage(duplicateErrorMessage));
	}

}
