package com.axonactive.employeecore.util;

import java.text.Normalizer;

public class UtfStringConverter {

	public String convertUtf8String(String origin) {
		String normalizeString = Normalizer.normalize(origin, Normalizer.Form.NFD);
		normalizeString = normalizeString.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").replace("\u0111", "d")
				.replace("\u00d0", "D").replace("\u0110", "D").replace("\u0189", "D");
		return normalizeString;
	}
}
