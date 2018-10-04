package com.evola.edt.service.rest;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

/**
 * @author Nikola 21.09.2013.
 * 
 */
public class AbstractRestService {
	protected Logger log = Logger.getLogger(getClass());

	protected Response createNotOkResponse(Exception e) {
		
		ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.NOTOK);
		status.setMessage(e.getMessage());
		log.error(e, e);
		
		return Response.ok().entity(status).build();
	}

	protected Response createNotOkResponse(String message) {
		
		ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.NOTOK);
		status.setMessage(message);
		log.error(message);
		
		return Response.ok().entity(status).build();
	}

	protected Response createOkResponse() {
	
		ExecutionStatus status = new ExecutionStatus(ExecutionStatus.Status.OK);
		
		return Response.ok().entity(status).build();
	}
	
	protected Response createOkResponse(String message) {
		
		ExecutionStatus status = new ExecutionStatus(message, ExecutionStatus.Status.OK);
		
		return Response.ok().entity(status).build();
	}
	
	protected Response createOkResponse(Object data) {
		
		return Response.ok().entity(data).build();
	}
}
