package com.axonactive.employeecore.additionalcontact;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ContactType {

	EMAIL("Email"),
	MOBILEPHONE("MobilePhone"),
	HOMEPHONE("HomePhone"),
	LINK("Link"),
	PHONE("Phone"),
	SKYPE("Skype");

	private @Getter String value;
}