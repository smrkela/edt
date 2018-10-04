package com.evola.edt.web.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nikola 22.04.2013.
 * 
 */
public class LoginTest {

	/**
	 * @author Nikola 22.04.2013.
	 */

	@Test
	public void testLogin() {
		HttpClient client = new HttpClient();
		HttpMethod httpMethod = new GetMethod(
				"http://localhost:8080/EDTService/login?username=gost&password=gost");
		try {
			client.executeMethod(httpMethod);
			byte[] responseBody = httpMethod.getResponseBody();
			Properties props = new Properties();
			props.load(new ByteArrayInputStream(responseBody));
			String token = props.getProperty("authenticationToken");
			HttpMethod httpMethod2 = new GetMethod(
					"http://localhost:8080/EDTService/rest/user/getUser?userId=1");
			httpMethod2.setRequestHeader("AuthenticationToken", token);
			client.executeMethod(httpMethod2);
			System.out.println(httpMethod2.getResponseBodyAsString());

			HttpMethod httpMethod3 = new GetMethod(
					"http://localhost:8080/EDTService/logout");
			httpMethod3.setRequestHeader("AuthenticationToken", token);
			client.executeMethod(httpMethod3);
			client.executeMethod(httpMethod2);
			Assert.assertEquals(httpMethod2.getStatusCode(),
					HttpServletResponse.SC_UNAUTHORIZED);

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
