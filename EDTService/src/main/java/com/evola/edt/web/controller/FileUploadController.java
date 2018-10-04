package com.evola.edt.web.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.evola.edt.manager.FileItem;
import com.evola.edt.manager.FileManager;

/**
 * @author Nikola 27.06.2013.
 * 
 */
@Controller
public class FileUploadController {

	@Inject
	private FileManager fileManager;

	@ModelAttribute(value = "fileItem")
	public FileItem getModel() {
		return new FileItem();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String get() {
		return "uploadPage";
	}

	/**
	 * @author Nikola 27.06.2013.
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(
			@ModelAttribute(value = "fileItem") FileItem fileItem) {
		fileManager.saveFile(fileItem);
		return "uploadPage";
	}
}
