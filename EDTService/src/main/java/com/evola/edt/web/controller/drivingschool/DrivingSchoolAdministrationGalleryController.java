package com.evola.edt.web.controller.drivingschool;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.jcr.GalleryManager;
import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.service.DrivingSchoolService;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;
import com.evola.edt.web.security.UserCredentialsManager;

@Controller
public class DrivingSchoolAdministrationGalleryController {

	Logger log = Logger.getLogger(getClass());

	@Inject
	DrivingSchoolService service;

	@Inject
	UserCredentialsManager credentialsManager;

	@Inject
	DrivingSchoolController standardController;

	@Inject
	GalleryManager galleryManager;

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija")
	public ModelAndView drivingSchoolAdministrationGallery(@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		List<DocumentDTO> dtos = galleryManager.getDrivingSchoolAlbums(schoolId);
		
		int numberOfImages = galleryManager.getDrivingSchoolNumberOfImages(schoolId);
		int imageLimit = credentialsManager.getDrivingSchoolImageLimit(schoolId);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationGallery");
		mav.addObject("school", school);
		mav.addObject("albums", dtos);
		mav.addObject("numberOfImages", numberOfImages);
		mav.addObject("numberOfImagesLimit", imageLimit);
		mav.addObject("pageInfo", new PageInfo("galerija-admin", "Galerija", PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/dodaj")
	public ModelAndView drivingSchoolAdministrationGalleryAddAlbum(
			@RequestParam(value = "id", required = true) Long schoolId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		String message = credentialsManager.getDrivingSchoolGalleryLimitMessage(schoolId);

		if (message != null) {

			ModelAndView mav = new ModelAndView();
			mav.setViewName("drivingSchoolCannotAddAlbum");
			mav.addObject("message", message);
			mav.addObject("school", school);
			mav.addObject("pageInfo", new PageInfo("galerija-admin", "Greška", PageCategories.AUTOSKOLE));

			return mav;
		}

		ModelAndView mav = new ModelAndView();

		DocumentDTO album = new DocumentDTO();

		String title = "Dodavanje albuma - " + school.getName();

		mav.setViewName("drivingSchoolAdministrationAlbumAdd");
		mav.addObject("school", school);
		mav.addObject("album", album);
		mav.addObject("pageInfo", new PageInfo("galerija-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/dodaj-slike")
	public ModelAndView drivingSchoolAdministrationAlbumAddImages(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "albumId", required = true) String albumId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		DocumentDTO album = galleryManager.getDrivingSchoolAlbum(albumId);

		if (!galleryManager.albumBelongsToSchool(schoolId, album))
			return credentialsManager.getForbiddenMAV();

		String title = "Dodavanje slika - " + album.getName() + " - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationAlbumAddImages");
		mav.addObject("school", school);
		mav.addObject("album", album);
		mav.addObject("pageInfo", new PageInfo("galerija-admin", title, PageCategories.AUTOSKOLE));

		int currentImageCount = galleryManager.getDrivingSchoolNumberOfImages(schoolId);
		int maxImageCount = credentialsManager.getDrivingSchoolImageLimit(schoolId);
		
		galleryManager.addGalleryUploaderData(mav, maxImageCount - currentImageCount);

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/izmeni")
	public ModelAndView drivingSchoolAdministrationAlbumUpdate(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "albumId", required = true) String albumId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		DocumentDTO album = galleryManager.getDrivingSchoolAlbum(albumId);

		if (!galleryManager.albumBelongsToSchool(schoolId, album))
			return credentialsManager.getForbiddenMAV();

		String title = "Izmena albuma - " + album.getName() + " - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationAlbumAdd");
		mav.addObject("school", school);
		mav.addObject("album", album);
		mav.addObject("pageInfo", new PageInfo("galerija-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/izmeni-sliku")
	public ModelAndView drivingSchoolAdministrationAlbumUpdateImage(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "imageId", required = true) String imageId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		DocumentDTO image = galleryManager.getDrivingSchoolImage(imageId);

		if (!galleryManager.imageBelongsToSchool(schoolId, image))
			return credentialsManager.getForbiddenMAV();

		String title = "Izmena slike - " + image.getName() + " - " + school.getName();

		// moramo skinuti ektenziju sa naziva fajla
		String string = FilenameUtils.removeExtension(image.getName());

		image.setName(string);

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationAlbumImageUpdate");
		mav.addObject("school", school);
		mav.addObject("image", image);
		mav.addObject("pageInfo", new PageInfo("galerija-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/obrisi")
	public ModelAndView drivingSchoolAdministrationAlbumDelete(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "albumId", required = true) String albumId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DocumentDTO drivingSchoolAlbum = galleryManager.getDrivingSchoolAlbum(albumId);

		if (!galleryManager.albumBelongsToSchool(schoolId, drivingSchoolAlbum))
			return credentialsManager.getForbiddenMAV();

		galleryManager.deleteDrivingSchoolAlbum(drivingSchoolAlbum);

		return drivingSchoolAdministrationGallery(schoolId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/uredi-slike")
	public ModelAndView drivingSchoolAdministrationAlbumManageImages(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "albumId", required = true) String albumId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DrivingSchoolDTO school = service.findDrivingSchoolById(schoolId);

		DocumentDTO album = galleryManager.getDrivingSchoolAlbum(albumId);

		if (!galleryManager.albumBelongsToSchool(schoolId, album))
			return credentialsManager.getForbiddenMAV();

		List<DocumentDTO> images = galleryManager.getAlbumImages(album.getId());

		String title = "Izmena albuma - " + album.getName() + " - " + school.getName();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("drivingSchoolAdministrationAlbumManageImages");
		mav.addObject("school", school);
		mav.addObject("album", album);
		mav.addObject("images", images);
		mav.addObject("pageInfo", new PageInfo("galerija-admin", title, PageCategories.AUTOSKOLE));

		credentialsManager.addLicenceInformation(mav, schoolId);

		boolean isAllowed = (boolean) mav.getModelMap().get("galleryAllowed");

		if (!isAllowed)
			return credentialsManager.getNotAvailableMAV();

		return mav;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/administracija-auto-skole/galerija/obrisi-sliku")
	public ModelAndView drivingSchoolAdministrationAlbumDeleteImage(
			@RequestParam(value = "id", required = true) Long schoolId,
			@RequestParam(value = "imageId", required = true) String imageId) {

		if (!credentialsManager.canAdministerSchool(schoolId))
			return credentialsManager.getForbiddenMAV();

		DocumentDTO image = galleryManager.getDrivingSchoolImage(imageId);

		if (!galleryManager.imageBelongsToSchool(schoolId, image))
			return credentialsManager.getForbiddenMAV();

		galleryManager.deleteDrivingSchoolImage(image.getId());

		return drivingSchoolAdministrationAlbumManageImages(schoolId, image.getParentId());
	}

}
