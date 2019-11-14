package com.axonactive.employeeui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "phoneNumberConverter")
public class PhoneNumberConverter implements Converter {

	@Override
	public String getAsObject(FacesContext context, UIComponent component, String value) {
		return value.replace(" ", "").replace("-", "");
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString();
	}

}
