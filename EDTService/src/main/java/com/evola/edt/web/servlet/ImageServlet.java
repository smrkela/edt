package com.evola.edt.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jmimemagic.Magic;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Nikola 08.04.2013.
 * 
 */
@WebServlet(value = "/image", asyncSupported = true)
public class ImageServlet extends HttpServlet {
	Logger log = Logger.getLogger(getClass());

	private static final String PATH_PARAM_NAME = "path";
	private static final String ENVIRONMENT_PROPERTIES = "environment.properties";
	private static final String SERVLET_IMAGE_BASEPATH = "fileRepositoryBasePath";
	private Properties props = new Properties();

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		InputStream resourceAsStream = ImageServlet.class.getClassLoader()
				.getResourceAsStream(ENVIRONMENT_PROPERTIES);
		try {
			props.load(resourceAsStream);
		} catch (IOException e) {
			log.error(e, e);
		}
	}

	/**
	 * @author Nikola 08.04.2013.
	 */
	private static final long serialVersionUID = 3600776896853610808L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String filePath = request.getParameter(PATH_PARAM_NAME);
			if (StringUtils.isEmpty(filePath)) {
				throw new IllegalArgumentException("Query param "
						+ PATH_PARAM_NAME + " is missing");
			}
			String basePath = props.getProperty(SERVLET_IMAGE_BASEPATH);
			String fullFilepath = basePath + File.separator + filePath;
			File imageFile = new File(fullFilepath);
			String contentType = Magic.getMagicMatch(imageFile, false)
					.getMimeType();
			// You must tell the browser the file type you are going to send
			// for example application/pdf, text/plain, text/html, image/jpg
			response.setContentType(contentType);
			// This should send the file to browser
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(imageFile);
			IOUtils.copyLarge(in, out, new byte[ResourceServlet.BUFFER_SIZE]);
			in.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error(e, e);
		}
	}

}
