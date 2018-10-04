package com.evola.edt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evola.edt.model.DrivingSchool;

/**
 * @author Nikola 24.05.2013.
 * 
 */
public interface DrivingSchoolRepository extends JpaRepository<DrivingSchool, Long> {

	@Query("SELECT e FROM DrivingSchool e WHERE UPPER(e.city) = :city AND (e.isHidden = false OR e.isHidden IS NULL) ORDER BY UPPER(e.name) ASC")
	public abstract List<DrivingSchool> findDrivingSchoolsByCity(@Param("city") String city);

	public DrivingSchool findByName(String name);

	@Query("select count(p) from DrivingSchool p where p.uniqueName=:uniqueName")
	public Long countByUniqueName(@Param("uniqueName") String uniqueName);

	@Query("select count(p) from DrivingSchool p where p.uniqueName=:uniqueName AND p.id <> :id")
	public Long countByUniqueNameExcept(@Param("uniqueName") String uniqueName, @Param("id") Long id);

	@Query("SELECT DISTINCT upper(p.city) FROM DrivingSchool p ORDER BY upper(p.city) ASC")
	public abstract List<String> findUniqueCityNames();
	
	@Query(value="SELECT p " + 
			   "FROM DrivingSchool p " +
			   "WHERE (upper(p.name) LIKE upper(:searchText) OR :searchText IS NULL) " +
			   "AND (upper(p.city) = upper(:city) OR :city IS NULL) " +
			   "AND (upper(p.categories) LIKE upper(:category) OR :category IS NULL) " +
			   "AND (p.averageMark >= :searchMarkFrom OR :searchMarkFrom IS NULL) " +
			   "AND (p.averageMark <= :searchMarkTo OR :searchMarkTo IS NULL) " +
			   "AND (p.categoryBPrice >= :searchPriceFrom OR :searchPriceFrom IS NULL) " +
			   "AND (p.categoryBPrice <= :searchPriceTo OR :searchPriceTo IS NULL) AND (p.isHidden = false OR p.isHidden IS NULL)")
	public abstract Page<DrivingSchool> findBySearchAndSortCriteria(@Param("searchText") String searchText, @Param("city") String city, @Param("category") String category,
																 @Param("searchMarkFrom") Double searchMarkFrom, @Param("searchMarkTo") Double searchMarkTo, 
																 @Param("searchPriceFrom") Double searchPriceFrom, @Param("searchPriceTo") Double searchPriceTo,
																 Pageable pageable);

	@Query("SELECT d FROM DrivingSchool d WHERE (d.isHidden = false OR d.isHidden IS NULL) AND (d.id NOT IN (:schoolIDs)) ORDER BY UPPER(d.name) ASC")
	public abstract List<DrivingSchool> findAllActiveSchools(@Param("schoolIDs") List<Long> schoolIDs);
		
	public abstract DrivingSchool findByUniqueName(String uniqueName);

	@Query("SELECT DISTINCT upper(p.city) FROM DrivingSchool p WHERE (p.isHidden = false OR p.isHidden IS NULL) ORDER BY upper(p.city) ASC")
	public abstract List<String> findUniqueCityNamesForActiveSchools();

	@Query("SELECT e FROM DrivingSchool e INNER JOIN e.members members WHERE :userId IN members.user.id ORDER BY UPPER(e.name) ASC")
	public abstract List<DrivingSchool> findByMember(@Param("userId") Long userId);

	@Query("SELECT e.drivingSchool FROM DrivingSchoolSiteLicense e ORDER BY UPPER(e.drivingSchool.name) ASC")
	public abstract List<DrivingSchool> findLicencedSchools();
	
	public abstract DrivingSchool findById(Long id);

}
