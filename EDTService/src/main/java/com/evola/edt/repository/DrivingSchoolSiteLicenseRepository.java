package com.evola.edt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolSiteLicense;

public interface DrivingSchoolSiteLicenseRepository extends
		JpaRepository<DrivingSchoolSiteLicense, Long> {

	DrivingSchoolSiteLicense findByDrivingSchool(DrivingSchool school);

}
