package com.axonactive.employeeui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {

	public static String countFile(String path) {
		File file = new File(path);
		File[] listFile = file.listFiles();
		List<String> fileNames = new ArrayList<>();
		for (int i = 0; i < listFile.length; i++) {
			fileNames.add(listFile[i].getAbsolutePath());
		}
		return fileNames.toString();
	}

	public static void main(String[] args) {
		System.out.println(countFile("C:\\opt\\jboss\\wildfly\\employee-tool\\12\\"));
	}

}
