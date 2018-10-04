package com.evola.edt.service.rest;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.evola.edt.component.DailyTestManager;
import com.evola.edt.component.MarathonManager;
import com.evola.edt.component.RealTestManager;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.helpers.DailyTestDTO;
import com.evola.edt.web.security.UserCredentialsManager;

@Path("/learning")
@Named
public class LearningRestService extends AbstractRestService {

	@Inject
	private DailyTestManager dailyTestManager;
	
	@Inject
	private RealTestManager mRealTest;
	
	@Inject
	private UserRepository userRepository;
	
	@Inject
	private UserCredentialsManager credentialsManager;
	
	@Inject
	private MarathonManager mMarathon;

	@POST
	@Path("/submitDailyTest")
	@Consumes("application/json")
	public Response registerUserDTO(DailyTestDTO dto) {

		try {

			Map<String, Object> map = dailyTestManager.submitTest(dto);
			
			return createOkResponse(map);

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}
	
	@POST
	@Path("/submitRealTest")
	@Consumes("application/json")
	public Response submitRealTest(DailyTestDTO dto) {

		try {

			Map<String, Object> map = mRealTest.submitTest(dto);
			
			return createOkResponse(map);

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}
	
	@GET
	@Path("/maraton/polaganje/pocetak")
	@Produces("application/json")
	public Response loadMarathon(@QueryParam("marathonId") String marathonId){
		
		try {

			Map<String, Object> map = mMarathon.loadInitialData(marathonId);
			
			return createOkResponse(map);

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}
	
	@GET
	@Path("/maraton/polaganje/preskoci")
	@Produces("application/json")
	public Response skipMarathonQuestion(@QueryParam("marathonId") String marathonId){
		
		try {

			Map<String, Object> map = mMarathon.skipQuestion(marathonId);
			
			return createOkResponse(map);

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}
	
	@POST
	@Path("/maraton/polaganje/podnesi")
	@Produces("application/json")
	public Response submitMarathonQuestion(Map<String, Object> data){
		
		try {

			Map<String, Object> map = mMarathon.submitQuestion(data);
			
			return createOkResponse(map);

		} catch (Exception e) {

			ExecutionStatus status = new ExecutionStatus(e.getMessage(), ExecutionStatus.Status.NOTOK);

			return Response.serverError().entity(status).build();
		}
	}
	
}
