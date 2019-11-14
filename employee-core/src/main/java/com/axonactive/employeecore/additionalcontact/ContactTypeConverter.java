package com.axonactive.employeecore.additionalcontact;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

@Converter(autoApply = true)
public class ContactTypeConverter implements AttributeConverter<ContactType, String> {

	@Override
	public String convertToDatabaseColumn(ContactType attribute) {
		return attribute.getValue();
	}

	@Override
	public ContactType convertToEntityAttribute(String typeValue) {
		ContactType[] types = ContactType.values();
		return Arrays.stream(types).filter(each -> StringUtils.equalsIgnoreCase(each.getValue(), typeValue)).findFirst()
				.orElseThrow(IllegalStateException::new);
	}
}
