package com.evola.edt.service.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.evola.edt.component.FileInfo;
import com.evola.edt.component.TempUploadComponent;
import com.evola.edt.component.UploadedFile;

@Path("/")
@Named
public class UploadFileRestService extends AbstractRestService {
	Logger log = Logger.getLogger(getClass());
	@Inject
	private TempUploadComponent tempUploadComponent;

	@POST
	@Path("/upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public String getPage(@Context HttpServletRequest req) {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<String> ids = new ArrayList<String>();
			List<FileItem> items = upload.parseRequest(req);
			for (FileItem fileItem : items) {

				if (!"file".equals(fileItem.getFieldName()))
					continue;

				FileInfo fileInfo = new FileInfo();
				fileInfo.setFileName(fileItem.getName());
				fileInfo.setName(fileItem.getFieldName());
				fileInfo.setSize(fileItem.getSize());
				fileInfo.setType(fileItem.getContentType());
				UploadedFile uploadedFile = new UploadedFile(
						fileItem.getInputStream(), fileInfo);
				ids.add(tempUploadComponent.saveTempFile(uploadedFile));
			}
			return getCommaSeparatedIds(ids);
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
	}

	private String getCommaSeparatedIds(List<String> ids) {
		StringBuffer buf = new StringBuffer();
		for (String id : ids) {
			if (buf.length() == 0) {
				buf.append(id);
				continue;
			}
			buf.append(",");
			buf.append(id);
		}
		return buf.toString();
	}

}
