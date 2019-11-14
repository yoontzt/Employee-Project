package com.axonactive.employeeui.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FakeDepartmentDTO implements Serializable{

	private static final long serialVersionUID = -5170555586092768300L;

	private Integer departmentId;
	
	private String name;
	
	
}