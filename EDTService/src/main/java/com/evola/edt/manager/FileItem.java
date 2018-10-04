package com.evola.edt.manager;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Nikola 27.06.2013.
 * 
 */
public class FileItem {
	private MultipartFile file;
	private String folderName = "";

	public FileItem() {
	}

	public FileItem(MultipartFile file, String name) {
		super();
		this.file = file;
		this.folderName = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String name) {
		this.folderName = name;
	}

}
