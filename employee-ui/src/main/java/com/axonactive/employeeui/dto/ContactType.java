package com.axonactive.employeeui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ContactType {
	EMAIL("Email"),
	PHONE("Phone"),
	LINK("Link");

	private @Getter String value;
}