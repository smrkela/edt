package com.evola.edt.manager;

public interface FileManager {

	/**
	 * @author Nikola 27.06.2013.
	 * @param fileItem
	 */
	public abstract void saveFile(FileItem fileItem);

	/**
	 * @author Nikola 27.06.2013.
	 * @param name
	 */
	public abstract void removeFile(String name);

}