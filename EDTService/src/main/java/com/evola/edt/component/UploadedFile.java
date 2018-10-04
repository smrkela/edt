package com.evola.edt.component;

import java.io.InputStream;

import org.springframework.util.Assert;

/**
 * @author Nikola.Nikolic
 * 
 */
public class UploadedFile {
	private InputStream inputStream;
	private FileInfo fileInfo;

	public UploadedFile(InputStream inputStream, FileInfo fileInfo) {
		super();
		Assert.notNull(inputStream);
		Assert.notNull(fileInfo);
		this.inputStream = inputStream;
		this.fileInfo = fileInfo;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

}
