package com.axonactive.employeeui.helper;

import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.PrimeFaces;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JavaScriptHelper {

	public static void showErrorMessage(String message) {
		message = StringEscapeUtils.escapeJava(message);
		PrimeFaces.current().executeScript("top.ui.showErrorMessage(\"" + message + "\");");
	}

	public static void showDialog(String dialogName) {
		PrimeFaces.current().executeScript("PF('" + dialogName + "').show()");
	}

	public static void hideDialog(String dialogName) {
		PrimeFaces.current().executeScript("PF('" + dialogName + "').hide()");
	}

	public static void showSuccessMessage(String message) {
		message = StringEscapeUtils.escapeJava(message);
		PrimeFaces.current().executeScript("top.ui.showSuccessMessage(\"" + message + "\");");
	}

}
