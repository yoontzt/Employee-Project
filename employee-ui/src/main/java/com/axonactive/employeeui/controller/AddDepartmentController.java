package com.axonactive.employeeui.controller;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.client.ClientProtocolException;

import com.axonactive.employeeui.dto.FakeDepartmentDTO;
import com.axonactive.employeeui.restclient.DepartmentRestClient;
import com.axonactive.employeeui.restclient.SecurityRestClient;

import lombok.Getter;
import lombok.Setter;

@Named(value = "addDepartmentController")
@ViewScoped
public class AddDepartmentController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private transient @Getter @Setter FakeDepartmentDTO department = new FakeDepartmentDTO();
	
	@Inject
	DepartmentRestClient deptRestClient;
	
	@Inject 
	SecurityRestClient securityRestClient;


	@PostConstruct
	public void init() {
		
		
	}
	
	public void addNewDepartment() throws ClientProtocolException, IOException, NoSuchAlgorithmException {
		deptRestClient.addNewDepartment(department);
		
	}

}
