package com.evola.edt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.PriceList;

/**
 * @author Nikola 21.09.2013.
 * 
 */
public interface PriceListRepository extends JpaRepository<PriceList, Long> {
	/**
	 * @param drivingSchool
	 * @return Nikola 21.09.2013.
	 */
	public PriceList findByDrivingSchool(DrivingSchool drivingSchool);
}
