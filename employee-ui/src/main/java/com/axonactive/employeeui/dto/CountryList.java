package com.axonactive.employeeui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CountryList {
	MYANMAR("Myanmar"),
	VIETNAM("Vietnam"),
	SWITZERLAND("Switzerland");

	private @Getter String value;
}