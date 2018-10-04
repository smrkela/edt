package com.evola.edt.service;

import com.evola.edt.model.DrivingLicenceCategory;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolLicenseType;
import com.evola.edt.model.PriceList;
import com.evola.edt.model.PriceListCategory;
import com.evola.edt.model.PriceListSubcategory;
import com.evola.edt.model.dto.PriceListCategoryDTO;
import com.evola.edt.model.dto.PriceListDTO;
import com.evola.edt.model.dto.PriceListSubCategoryDTO;
import com.evola.edt.repository.DrivingLicenceCategoryRepository;
import com.evola.edt.repository.DrivingSchoolRepository;
import com.evola.edt.repository.DrivingSchoolStudentRepository;
import com.evola.edt.repository.PriceListCategoryRepository;
import com.evola.edt.repository.PriceListRepository;
import com.evola.edt.repository.PriceListSubcategoryRepository;
import com.evola.edt.service.dto.transformer.PriceListCategoryDTOTransformer;
import com.evola.edt.service.dto.transformer.PriceListDTOTransformer;
import com.evola.edt.service.dto.transformer.PriceListSubcategoryDTOTransformer;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nikola
 * Date: 23.9.13.
 * Time: 21.47
 */
public abstract class AbstractDrivingSchoolServiceImpl {
    @Inject
    protected DrivingSchoolRepository drivingSchoolRepository;
    @Inject
    protected DrivingSchoolStudentRepository rDrivingSchoolStudent;
    @Inject
    protected DrivingLicenceCategoryRepository rDrivingLicenceCategory;
    @Inject
    protected PriceListRepository priceListRepository;
    @Inject
    protected PriceListCategoryRepository priceListCategoryRepository;
    @Inject
    protected PriceListSubcategoryRepository priceListSubcategoryRepository;

    @Inject
    protected PriceListDTOTransformer priceListDTOTransformer;

    @Inject
    protected PriceListCategoryDTOTransformer priceListCategoryDTOTransformer;

    @Inject
    protected PriceListSubcategoryDTOTransformer priceListSubcategoryDTOTransformer;

    /*
 * (non-Javadoc)
 *
 * @see com.evola.edt.service.DrivingSchoolAdministrationService#
 * getPriceListForDrivingSchool(java.lang.Long)
 */
    @Transactional(readOnly = true)
    public PriceListDTO findPriceListForDrivingSchool(Long drivingSchoolId) {
    	
        Assert.notNull(drivingSchoolId);
        
        DrivingSchool drivingSchool = drivingSchoolRepository
                .findOne(drivingSchoolId);
        
        PriceList priceList = priceListRepository
                .findByDrivingSchool(drivingSchool);
        
        if (priceList == null) {
            return null;
        }
        
        PriceListDTO priceListDTO = priceListDTOTransformer
                .transformToDTO(priceList);
        
        return priceListDTO;
    }


    @Transactional(readOnly = true)
    public List<PriceListCategoryDTO> findPriceListCategories() {
        List<PriceListCategory> priceListCategories = priceListCategoryRepository
                .findAll();
        List<PriceListCategoryDTO> priceListCategoryDTOs = new ArrayList<PriceListCategoryDTO>();
        for (PriceListCategory priceListCategory : priceListCategories) {
            priceListCategoryDTOs.add(priceListCategoryDTOTransformer
                    .transformToDTO(priceListCategory));
        }
        return priceListCategoryDTOs;
    }

    @Transactional(readOnly = true)
    public List<PriceListSubCategoryDTO> findPriceListSubcategories() {
        List<PriceListSubcategory> priceListSubcategories = priceListSubcategoryRepository
                .findAll();
        List<PriceListSubCategoryDTO> priceListSubcategoryDTOs = new ArrayList<PriceListSubCategoryDTO>();
        for (PriceListSubcategory priceListSubcategory : priceListSubcategories) {
            priceListSubcategoryDTOs.add(priceListSubcategoryDTOTransformer
                    .transformToDTO(priceListSubcategory));
        }
        return priceListSubcategoryDTOs;
    }
}
