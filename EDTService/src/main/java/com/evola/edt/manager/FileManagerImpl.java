package com.evola.edt.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * @author Nikola 27.06.2013.
 * 
 */
@Named
public class FileManagerImpl implements FileManager {

	Logger log = Logger.getLogger(getClass());

	@Value(value = "${fileRepositoryBasePath}")
	private String basePath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.evola.edt.manager.FileManager#saveFile(com.evola.edt.manager.FileItem
	 * )
	 */
	@Override
	public void saveFile(FileItem fileItem) {
		Assert.notNull(fileItem);
		Assert.notNull(fileItem.getFile());
		if (fileItem.getFile().isEmpty()) {
			return;
		}
		String parentFolderPath = basePath
				+ File.separator
				+ (StringUtils.isNotBlank(fileItem.getFolderName()) ? fileItem
						.getFolderName() + File.separator : "");
		String filePath = parentFolderPath
				+ fileItem.getFile().getOriginalFilename();
		File parentFolder = new File(parentFolderPath);
		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		File file = new File(filePath);
		try {
			FileOutputStream output = new FileOutputStream(file);
			InputStream inputStream = fileItem.getFile().getInputStream();
			IOUtils.copy(inputStream, output);
			inputStream.close();
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			log.error(e, e);
		} catch (IOException e) {
			log.error(e, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.evola.edt.manager.FileManager#removeFile(java.lang.String)
	 */
	@Override
	public void removeFile(String name) {
		Assert.notNull(name);
		String fileName = basePath + File.separator + name;
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

}
