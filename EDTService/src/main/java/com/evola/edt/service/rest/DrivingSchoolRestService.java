package com.evola.edt.service.rest;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.service.DrivingSchoolService;

@Path("/drivingSchool")
@Named
public class DrivingSchoolRestService {

	@Inject
	private DrivingSchoolService service;

	@GET
	@Path("/getDrivingSchool")
	@Produces("application/json")
	public DrivingSchoolDTO getDrivingSchool(@QueryParam("id") Long id) {
		return service.findDrivingSchoolById(id);
	}

	@GET
	@Path("/getAllDrivingSchools")
	@Produces("application/json")
	public List<DrivingSchoolDTO> getAllDrivingSchools() {
		return service.findAll();
	}

}
