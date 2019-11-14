package com.axonactive.employeecore.article;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import com.axonactive.employeecore.presistence.GenericService;

@Stateless
public class ArticleService extends GenericService<ArticleEntity> {

	private static final String ROOT_DIRECTORY = "/opt/jboss/wildfly/employee-tool/";
	private static final String BACK_SLASH = "/";
	private String employeeDirectory;

	public String saveUploadedFileInServer(Map<String, List<InputPart>> uploadForm) throws IOException {
		createRootFolder();
		createEmployeeIDFolderForCandidate(uploadForm);
		String fileName = "";
		String finalLocation = "";
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		for (InputPart inputPart : inputParts) {
			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = employeeDirectory+getFileNameFromHeader(header);
			InputStream inputStream = inputPart.getBody(InputStream.class, null);
			finalLocation=writeFile(inputStream, fileName);
		}
		return finalLocation;

	}

	private String getFileNameFromHeader(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				return name[1].trim().replaceAll("\"", "");
			}
		}
		return "unknown";
	}

	private String writeFile(InputStream inputStream, String filename) throws IOException {

		FileOutputStream outputStream = null;
		File file = new File(formatPath(filename));
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
			IOUtils.write(IOUtils.toByteArray(inputStream), outputStream);
		} finally {
			close(inputStream);
			close(outputStream);
		}
		return file.getAbsolutePath();
	}

	private void createEmployeeIDFolderForCandidate(Map<String, List<InputPart>> uploadForm) throws IOException {
		List<InputPart> eids = uploadForm.get("employeeId");
		for (InputPart eid : eids) {
			this.employeeDirectory = ROOT_DIRECTORY + eid.getBodyAsString() + BACK_SLASH;
			File directoryByEmployeeID = new File(employeeDirectory);
			makeDirectoryIfNotExist(directoryByEmployeeID);
		}
	}

	public void createRootFolder() {
		File rootDirectory = new File(ROOT_DIRECTORY);
		makeDirectoryIfNotExist(rootDirectory);
	}

	private void makeDirectoryIfNotExist(File directory) {
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	private void close(Closeable c) {
		if (c == null)
			return;
		try {
			c.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String formatPath(String filePath) {
		String name = filePath.split("\\.")[0];
		StringBuilder extension = cutExtensionString(new StringBuilder(filePath));
		SimpleDateFormat fDate = new SimpleDateFormat("dd MM yy HH mm ss ");
		String sDate = fDate.format(new Date());
		return name+"-"+sDate.replaceAll("\\s+", "")+extension;
	}
	public StringBuilder cutExtensionString(StringBuilder sb) {
		sb.reverse();
		sb.replace(0, sb.length(), (sb.substring(0, sb.indexOf(".") + 1)));
		sb.reverse();
		return sb;
	}

}
