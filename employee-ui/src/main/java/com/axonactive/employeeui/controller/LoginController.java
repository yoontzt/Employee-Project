package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.axonactive.employeeui.dto.TokenDTO;
import com.axonactive.employeeui.exception.UnauthorizedAccessException;
import com.axonactive.employeeui.restclient.SecurityRestClient;

import lombok.Getter;
import lombok.Setter;

@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	SecurityRestClient securityRestClient;

	private @Getter @Setter TokenDTO tokenDto = new TokenDTO();

	private @Getter @Setter String username;

	private @Getter @Setter String pwd;

	public void logIn() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			tokenDto = securityRestClient.getToken(username, pwd);
			context.getExternalContext().getSessionMap().put("user", username);
			context.getExternalContext().redirect("employee-list.xhtml");
		} catch (IOException | UnauthorizedAccessException e) {
			showErrorMessage();
		}
	}

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();
		try {
			context.getExternalContext().redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showErrorMessage() {
		FacesContext.getCurrentInstance().addMessage("loginForm:messages",
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid User and Password", ""));
	}
}
