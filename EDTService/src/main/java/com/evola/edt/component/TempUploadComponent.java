package com.evola.edt.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * @author Nikola.Nikolic
 * 
 */
@Named
public class TempUploadComponent {
	Logger log = Logger.getLogger(getClass());
	@Value("${tempFolderPath}")
	private String tempFolderPath;

	/**
	 * Saves the temp file and returns id of the file
	 * 
	 * @param inputStream
	 * @return
	 */
	public String saveTempFile(UploadedFile uploadedFile) {
		String fileId = UUID.randomUUID().toString();
		String filePath = getFilePath(fileId);
		String infoFilePath = getInfoFilePath(fileId);
		File f = new File(tempFolderPath);
		if (!f.exists()) {
			f.mkdirs();
		}
		try {
			saveFileOnDisk(uploadedFile.getInputStream(), filePath);
			serializeInfoFileToDisk(uploadedFile, infoFilePath);
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("Failure saving temp file");
		}
		return fileId;
	}

	/**
	 * @param uploadedFile
	 * @param infoFilePath
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void serializeInfoFileToDisk(UploadedFile uploadedFile,
			String infoFilePath) throws IOException, FileNotFoundException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				new FileOutputStream(infoFilePath));
		objectOutputStream.writeObject(uploadedFile.getFileInfo());
		objectOutputStream.flush();
		objectOutputStream.close();
	}

	/**
	 * @param inputStream
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveFileOnDisk(InputStream inputStream, String filePath)
			throws FileNotFoundException, IOException {
		FileOutputStream output = new FileOutputStream(filePath);
		IOUtils.copy(inputStream, output);
		inputStream.close();
		output.flush();
		output.close();
	}

	/**
	 * @param fileId
	 * @return
	 */
	public UploadedFile getTempFile(String fileId) {
		Assert.notNull(fileId);
		String filePath = getFilePath(fileId);
		String infoFilePath = getInfoFilePath(fileId);
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			ObjectInputStream infoFileInputStream = new ObjectInputStream(
					new FileInputStream(infoFilePath));
			FileInfo fileInfo = (FileInfo) infoFileInputStream.readObject();
			infoFileInputStream.close();
			UploadedFile uploadedFile = new UploadedFile(fileInputStream,
					fileInfo);
			return uploadedFile;
		} catch (Exception e) {
			log.error(e, e);
			throw new RuntimeException("Failure getting temp file");
		}
	}

	private String getInfoFilePath(String fileId) {
		return getFilePath(fileId) + ".info";
	}

	private String getFilePath(String fileId) {
		return tempFolderPath + File.separator + fileId;
	}

}
