package com.evola.edt.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.evola.edt.jcr.ContentRepositoryManager;
import com.evola.edt.jcr.Document;
import com.evola.edt.service.util.WebApplicationContextHolder;

/**
 * @author Nikola 08.04.2013.
 * 
 */
@WebServlet(value = "/resource", asyncSupported = true)
public class ResourceServlet extends HttpServlet {
	
	public static final int BUFFER_SIZE = 4 * 1024 * 4 * 4; //4 times the default size
	
	public static final String[] CACHEABLE_CONTENT_TYPES = new String[] {
        "text/css", "text/javascript", "image/png", "image/jpeg",
        "image/gif", "image/jpg" };
	
	static {
        Arrays.sort(CACHEABLE_CONTENT_TYPES);
    }
	
	Logger log = Logger.getLogger(getClass());

	private static final String ID_PARAM_NAME = "id";
	private static final String PATH_PARAM_NAME = "path";
	/**
	 * @author Nikola 08.04.2013.
	 */
	private static final long serialVersionUID = 3600776896853610808L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			String idParam = request.getParameter(ID_PARAM_NAME);
			String pathParam = request.getParameter(PATH_PARAM_NAME);

			if (StringUtils.isEmpty(idParam) && StringUtils.isEmpty(pathParam)) {
				throw new IllegalArgumentException("Query param " + ID_PARAM_NAME + " or " + PATH_PARAM_NAME
						+ " is missing");
			}

			ApplicationContext applicationContext = WebApplicationContextHolder.getApplicationContext();

			ContentRepositoryManager crManager = applicationContext.getBean(ContentRepositoryManager.class);

			Document document = null;

			if (idParam != null)
				document = crManager.get(idParam);
			else
				document = crManager.getByPath(pathParam);

			if (document != null) {

				long lastModified = 1000 * (document.getMetadata().getUpdatedOn().getTimeInMillis() / 1000);
				
				//prvo gledamo da li treba vratiti 304 odgovor ako nije bilo izmene fajla
				long cacheDate = request.getDateHeader("If-Modified-Since"); 
				
				if(cacheDate >= lastModified){
					
					//ako nije izmenjen dokument onda vracamo 304 status
					response.setStatus(response.SC_NOT_MODIFIED);
					
					return;
				}
				
				InputStream content = document.getContent();
				String mimeType = document.getMetadata().getMimeType();
				String fileName = document.getName() + "."+document.getMetadata().getExtension();
				// You must tell the browser the file type you are going to send
				// for example application/pdf, text/plain, text/html, image/jpg
				response.setContentType(mimeType);
				response.setContentLength(document.getMetadata().getSize().intValue());
				response.setHeader("Content-Disposition", "inline: filename=\""+fileName+"\"");
				response.setDateHeader("Last-Modified", lastModified);

		        if (mimeType != null && Arrays.binarySearch(CACHEABLE_CONTENT_TYPES, mimeType) > -1) {

		            Calendar inTwoMonths = Calendar.getInstance();
		            inTwoMonths.add(Calendar.DAY_OF_YEAR, 2);

		            response.setHeader("Cache-Control", "public");
		            response.setHeader("Expires", DateUtil.formatDate(inTwoMonths.getTime()));
		            
		        } else {
		        	
		            response.setHeader("Expires", "-1");
		        }
				
				// This should send the file to browser
				OutputStream out = response.getOutputStream();
				IOUtils.copyLarge(content, out, new byte[BUFFER_SIZE]);
				content.close();
				out.flush();
				out.close();
				
			} else {

				// ako ne nadjemo resurs ili sliku onda trazimo alternativnu

				if (pathParam != null) {

					if (pathParam.startsWith("/drivingSchools") && pathParam.endsWith("logo"))
						manageNullImage("driving_school_logo.jpg", response, request);
					
					else if (pathParam.startsWith("/drivingSchools") && pathParam.contains("/cars/"))
						manageNullImage("default_car_image.jpg", response, request);
					
					else if (pathParam.startsWith("/drivingSchools") && pathParam.contains("/employees/"))
						manageNullImage("default_employee_image.jpg", response, request);

					else if (pathParam.startsWith("/page") && pathParam.endsWith("smallPreview"))
						manageNullImage("page_small_preview.jpg", response, request);

					else if (pathParam.startsWith("/page") && pathParam.endsWith("normalPreview"))
						manageNullImage("page_normal_preview.jpg", response, request);

					else if (pathParam.startsWith("/user") && pathParam.endsWith("normalImage"))
						manageNullImage("default_user_profile_image_normal.png", response, request);

					else if (pathParam.startsWith("/user") && pathParam.endsWith("smallImage"))
						manageNullImage("default_user_profile_image_small.png", response, request);
				}

			}

		} catch (Exception e) {
			log.error(e, e);
		}
	}

	private void manageNullImage(String string, HttpServletResponse resp, HttpServletRequest request) throws IOException {

		ServletContext cntx = getServletContext();

		// Get the absolute path of the image
		String filename = cntx.getRealPath("images/" + string);

		String mime = cntx.getMimeType(filename);

		if (mime == null) {
			return;
		}
		
		File file = new File(filename);
		
		long lastModified = 1000 * (file.lastModified() / 1000);
		
		//prvo gledamo da li treba vratiti 304 odgovor ako nije bilo izmene fajla
		long cacheDate = request.getDateHeader("If-Modified-Since"); 
		
		if(cacheDate >= lastModified){
			
			//ako nije izmenjen dokument onda vracamo 304 status
			resp.setStatus(resp.SC_NOT_MODIFIED);
			
			return;
		}

		resp.setContentType(mime);
		
		if (resp != null && Arrays.binarySearch(CACHEABLE_CONTENT_TYPES, mime) > -1) {

            Calendar inTwoMonths = Calendar.getInstance();
            inTwoMonths.add(Calendar.DAY_OF_YEAR, 2);

            resp.setHeader("Cache-Control", "public");
            resp.setHeader("Expires", DateUtil.formatDate(inTwoMonths.getTime()));
            resp.setHeader("Content-Disposition", "inline: filename=\""+string+"\"");
            
        } else {
        	
        	resp.setHeader("Expires", "-1");
        }
		
		
		resp.setContentLength((int) file.length());
		resp.setDateHeader("Last-Modified", file.lastModified());

		FileInputStream in = new FileInputStream(file);
		
		OutputStream out = resp.getOutputStream();
		IOUtils.copyLarge(in, out, new byte[BUFFER_SIZE]);
		in.close();
		out.flush();
		out.close();
	}

}
