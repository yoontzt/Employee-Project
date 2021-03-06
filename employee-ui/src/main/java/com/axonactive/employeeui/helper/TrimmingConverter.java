package com.axonactive.employeeui.helper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "trimmingConverter")
public class TrimmingConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value.trim();
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return value.toString().trim();
	}
}
