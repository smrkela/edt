package com.evola.edt.jcr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.component.TempUploadComponent;
import com.evola.edt.component.UploadedFile;
import com.evola.edt.service.dto.AttachmentDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.dto.transformer.DocumentDTOTransformer;
import com.evola.edt.web.security.SecurityUtils;

@Named
public class GalleryManager {

	private static final int FILE_SIZE_LIMIT = 2 * 1024 * 1024;

	@Inject
	ContentRepositoryManager repositoryManager;

	@Inject
	DocumentDTOTransformer documentDTOTransformer;

	@Inject
	TempUploadComponent uploadComponent;

	@Value("${applicationUrl}")
	private String applicationUrl;

	/**
	 * Metoda vraca sve albume autoskole.
	 * 
	 * @param schoolId
	 * @return
	 */
	public List<DocumentDTO> getDrivingSchoolAlbums(long schoolId) {

		// ovde dohvatamo sve albume autoskole
		// slike koje nisu svrstane ni u jedan album nalaze se u albumu 'misc'

		String folderPath = "/drivingSchools/" + schoolId + "/gallery";

		return getAlbums(folderPath);
	}

	/**
	 * Metoda vraca album autoskole.
	 * 
	 * @param albumId
	 * @return
	 */
	public DocumentDTO getDrivingSchoolAlbum(String albumId) {

		Document albumDocument = repositoryManager.get(albumId);

		DocumentDTO dto = documentDTOTransformer.transformToDTO(albumDocument, "author");

		if ("misc_album".equals(dto.getName())) {
			dto.setMisc(true);
			dto.setName("Razno");
		}

		return dto;
	}

	/**
	 * Metoda vraca doredjenu sliku autoskole.
	 * 
	 * @param imageId
	 * @return
	 */
	public DocumentDTO getDrivingSchoolImage(String imageId) {

		Document imageDocument = repositoryManager.get(imageId);

		DocumentDTO dto = documentDTOTransformer.transformToDTO(imageDocument, "author");

		return dto;
	}

	/**
	 * Metoda proverava da li slika pripada odredjenoj autoskoli.
	 * 
	 * @param schoolId
	 * @param image
	 * @return
	 */
	public Boolean imageBelongsToSchool(Long schoolId, DocumentDTO image) {

		Document albumDocument = repositoryManager.get(image.getParentId());

		DocumentDTO dto = documentDTOTransformer.transformToDTO(albumDocument);

		return albumBelongsToSchool(schoolId, dto);
	}

	/**
	 * Metoda proverava da li album pripada odredjenoj autoskoli.
	 * 
	 * @param schoolId
	 * @param album
	 * @return
	 */
	public Boolean albumBelongsToSchool(Long schoolId, DocumentDTO album) {

		// moramo proveriti da li prosledjeni album stvarno pripada ovoj skoli
		Document parent = repositoryManager.get(album.getParentId());

		String folderPath = "/drivingSchools/" + schoolId + "/gallery";

		// parent nam je gallery folder a njegov parent je folder skole
		Document galleryFolder = repositoryManager.getByPath(folderPath);

		if (!galleryFolder.getIdentifier().equals(parent.getIdentifier()))
			return false;

		return true;
	}

	/**
	 * Metoda brise album autoskole.
	 * 
	 * @param drivingSchoolAlbum
	 */
	public void deleteDrivingSchoolAlbum(DocumentDTO drivingSchoolAlbum) {

		repositoryManager.delete(drivingSchoolAlbum.getId());
	}

	/**
	 * Metoda vraca slike nekog albuma.
	 * 
	 * @param albumId
	 * @return
	 */
	public List<DocumentDTO> getAlbumImages(String albumId) {

		List<DocumentDTO> imagesDto = new LinkedList<DocumentDTO>();

		List<Document> children = repositoryManager.getChildren(albumId);

		for (Document child : children) {

			DocumentDTO childDto = documentDTOTransformer.transformToDTO(child, "author");

			imagesDto.add(childDto);
		}

		return imagesDto;
	}

	public void deleteDrivingSchoolImage(String imageId) {

		repositoryManager.delete(imageId);
	}

	public List<DocumentDTO> getGlobalImageAlbums() {

		String folderPath = "/images";

		return getAlbums(folderPath);
	}

	public List<DocumentDTO> getGlobalFileAlbums() {

		String folderPath = "/files";

		return getAlbums(folderPath);
	}

	/**
	 * Metoda vraca listu albuma koji pripadaju nekoj putanju tj. folderu na
	 * pitanji.
	 * 
	 * Ukoliko nema albuma onda se odmah pravi misc album i on se vraca.
	 * 
	 * @param folderPath
	 * @return
	 */
	private List<DocumentDTO> getAlbums(String folderPath) {

		List<DocumentDTO> dtos = new LinkedList<DocumentDTO>();

		String parentNodeId = repositoryManager.getParentNodeId(folderPath);

		Document galleryRoot = repositoryManager.get(parentNodeId);

		List<Document> albums = repositoryManager.getChildren(galleryRoot.getIdentifier());

		// ako nemamo ni jedan album onda pravimo misc album
		if (albums.size() == 0) {

			Document folderDocument = new Document(DocumentType.FOLDER, "misc_album");

			String miscAlbumId = repositoryManager.add(folderDocument, galleryRoot.getIdentifier());

			// opet izvlacimo list foldera, sada treba da imamo samo ovaj sto
			// smo upravo ubacili
			albums = repositoryManager.getChildren(galleryRoot.getIdentifier());
		}

		for (Document doc : albums) {

			DocumentDTO dto = documentDTOTransformer.transformToDTO(doc, "author");

			if (dto.getName().equals("misc_album")) {
				dto.setMisc(true);
				dto.setName("Nesvrstane slike");
				dto.setDescription("Sve slike koje nisu u ni jednom odredjenom albumu");
			}

			List<Document> children = repositoryManager.getChildren(doc.getIdentifier());

			dto.setNumOfDocuments(children.size());

			dtos.add(dto);
		}

		return dtos;
	}

	/**
	 * Metoda vraca slike iz albuma misc autoskole.
	 * 
	 * @param schoolId
	 * @return
	 */
	public List<DocumentDTO> getDrivingSchoolMiscelanousImages(Long schoolId) {

		String folderPath = "/drivingSchools/" + schoolId + "/gallery/misc_album";

		Document miscAlbum = repositoryManager.getByPath(folderPath);

		List<DocumentDTO> albumImages = getAlbumImages(miscAlbum.getIdentifier());

		return albumImages;
	}

	/**
	 * Metoda uklanja misc album iz liste albuma.
	 * 
	 * @param drivingSchoolAlbums
	 */
	public void removeMiscAlbum(List<DocumentDTO> drivingSchoolAlbums) {

		int index = 0;

		for (DocumentDTO doc : drivingSchoolAlbums) {

			if (doc.isMisc()) {

				break;
			}

			index++;
		}

		if (index < drivingSchoolAlbums.size())
			drivingSchoolAlbums.remove(index);
	}

	public DocumentDTO getGlobalImageAlbum(String albumId) {

		Document albumDocument = repositoryManager.get(albumId);

		DocumentDTO dto = documentDTOTransformer.transformToDTO(albumDocument, "author");

		if ("misc_album".equals(dto.getName())) {
			dto.setMisc(true);
			dto.setName("Razno");
		}

		return dto;
	}

	public DocumentDTO getGlobalImage(String imageId) {

		Document imageDocument = repositoryManager.get(imageId);

		DocumentDTO dto = documentDTOTransformer.transformToDTO(imageDocument, "author");

		return dto;
	}

	public void deleteGlobalImageAlbum(String albumId) {

		repositoryManager.delete(albumId);
	}

	public void deleteGlobalImage(String imageId) {

		repositoryManager.delete(imageId);
	}

	/**
	 * Metoda cuva logo autoskole.
	 * 
	 * @param schoolId
	 * @param logo
	 */
	public void saveDrivingSchoolLogo(long schoolId, String logo) {

		if (StringUtils.isNotBlank(logo)) {

			String folderPath = "/drivingSchools/" + schoolId;

			repositoryManager.saveFileToRepository(folderPath, "logo", logo, 250, 250);
		}
	}

	/**
	 * Metoda cuva sliku zaposlenog.
	 * 
	 * @param schoolId
	 * @param employeeId
	 * @param imageId
	 */
	public void saveDrivingSchoolEmployeeImage(long schoolId, long employeeId, String imageId) {

		if (StringUtils.isNotBlank(imageId)) {

			String folderPath = "/drivingSchools/" + schoolId + "/employees";

			repositoryManager.saveFileToRepository(folderPath, employeeId + "", imageId, 300, 300);
		}
	}

	/**
	 * Metoda cuva sliku auta.
	 * 
	 * @param schoolId
	 * @param carId
	 * @param imageId
	 */
	public void saveDrivingSchoolCarImage(Long schoolId, Long carId, String imageId) {

		if (StringUtils.isNotBlank(imageId)) {

			String folderPath = "/drivingSchools/" + schoolId + "/cars";

			repositoryManager.saveFileToRepository(folderPath, carId + "", imageId, 300, 300);
		}
	}

	/**
	 * Metoda dodaje album autoskole.
	 * 
	 * @param schoolId
	 * @param name
	 * @param description
	 */
	public void addDrivingSchoolAlbum(long schoolId, String name, String description) {

		String folderPath = "/drivingSchools/" + schoolId + "/gallery";

		addAlbum(folderPath, name, description);
	}

	/**
	 * Metoda dodaje album.
	 * 
	 * @param folderPath
	 * @param name
	 * @param description
	 */
	private void addAlbum(String folderPath, String name, String description) {

		String parentNodeId = repositoryManager.getParentNodeId(folderPath);

		Document document = new Document(DocumentType.FOLDER, name);
		document.getMetadata().setDescription(description);

		document.getMetadata().setCreatedBy(SecurityUtils.getUserId().toString());
		document.getMetadata().setCreatedOn(Calendar.getInstance());

		repositoryManager.add(document, parentNodeId);
	}

	/**
	 * Metoda osvezava album autoskole.
	 * 
	 * @param schoolId
	 * @param id
	 * @param name
	 * @param description
	 */
	public void updateDrivingSchoolAlbum(Long schoolId, String id, String name, String description) {

		Document album = repositoryManager.get(id);
		DocumentDTO dto = documentDTOTransformer.transformToDTO(album);

		Assert.isTrue(albumBelongsToSchool(schoolId, dto));

		album.setName(name);
		album.getMetadata().setDescription(description);

		album.getMetadata().setUpdatedBy(SecurityUtils.getUserId().toString());
		album.getMetadata().setUpdatedOn(Calendar.getInstance());

		repositoryManager.edit(album);
	}

	/**
	 * Metoda osvezava sliku iz autoskole.
	 * 
	 * @param schoolId
	 * @param imageId
	 * @param name
	 * @param description
	 */
	public void updateDrivingSchoolImage(long schoolId, String imageId, String name, String description) {

		Document image = repositoryManager.get(imageId);
		DocumentDTO album = getDrivingSchoolAlbum(image.getParentId());

		Assert.isTrue(albumBelongsToSchool(schoolId, album));

		updateImage(image, name, description);
	}

	/**
	 * Metoda dodaje slike u album autoskole.
	 * 
	 * @param schoolId
	 * @param id
	 * @param attachments
	 */
	public void addDrivingSchoolImagesToAlbum(Long schoolId, String albumId, List<AttachmentDTO> attachments) {

		DocumentDTO album = getDrivingSchoolAlbum(albumId);

		Assert.isTrue(albumBelongsToSchool(schoolId, album));

		addImagesToAlbum(albumId, attachments);
	}

	private void addImagesToAlbum(String albumId, List<AttachmentDTO> attachments) {

		addAnyImagesToAlbum(albumId, attachments, true);
	}

	/**
	 * Metoda dodaje slike u album.
	 * 
	 * @param albumId
	 * @param attachments
	 */
	private void addAnyImagesToAlbum(String albumId, List<AttachmentDTO> attachments, boolean shouldTransform) {

		for (AttachmentDTO attachmentDTO : attachments) {

			String attachmentId = attachmentDTO.getId();
			String name = attachmentDTO.getName();
			String description = attachmentDTO.getDescription();

			name = StringUtils.trim(name);
			description = StringUtils.trim(description);

			UploadedFile tempFile = uploadComponent.getTempFile(attachmentId);
			InputStream inputStream = tempFile.getInputStream();

			String finalName = name;

			if (StringUtils.isBlank(finalName))
				finalName = tempFile.getFileInfo().getName();

			if (StringUtils.isBlank(finalName))
				finalName = tempFile.getFileInfo().getFileName();

			InputStream in = null;

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			
			String extension = null;

			try {

				if (shouldTransform){
					Thumbnails.of(inputStream).outputFormat("jpg").size(800, 800).toOutputStream(os);
					extension = "jpg";
				}
				else{
					IOUtils.copy(inputStream, os);
					extension = getExtension(finalName);
				}

			} catch (IOException e) {

				throw new ContentRepositoryException(e);
			}

			in = new ByteArrayInputStream(os.toByteArray());

			Long currentUserId = SecurityUtils.getUserId();

			Document image = null;

			Metadata metaData = repositoryManager.getMetadata(currentUserId, Calendar.getInstance(), os.size());
			
			metaData.setExtension(extension);
			metaData.setMimeType("image/"+extension);

			image = new Document(DocumentType.FILE, finalName, metaData);
			image.setContent(in);

			repositoryManager.add(image, albumId);
		}
	}

	private String getExtension(String finalName) {

		String extension = "jpg";
		
		if(finalName.indexOf(".") != -1)
			extension = finalName.substring(finalName.lastIndexOf(".") + 1);
		
		return extension;
	}

	/**
	 * Metoda dodaje globalni album.
	 * 
	 * @param name
	 * @param description
	 */
	public void addGlobalImageAlbum(String name, String description) {

		String folderPath = "/images";

		addAlbum(folderPath, name, description);

	}

	/**
	 * Metoda osvezava podatke globalnog albuma.
	 * 
	 * @param id
	 * @param name
	 * @param description
	 */
	public void updateGlobalImageAlbum(String id, String name, String description) {

		Document album = repositoryManager.get(id);

		album.setName(name);
		album.getMetadata().setDescription(description);

		album.getMetadata().setUpdatedBy(SecurityUtils.getUserId().toString());
		album.getMetadata().setUpdatedOn(Calendar.getInstance());

		repositoryManager.edit(album);
	}

	/**
	 * Metoda osvezava podatke globalne slike.
	 * 
	 * @param imageId
	 * @param name
	 * @param description
	 */
	public void updateGlobalImage(String imageId, String name, String description) {

		Document image = repositoryManager.get(imageId);

		updateImage(image, name, description);
	}

	/**
	 * Metoda osvezava podatke slike.
	 * 
	 * @param image
	 * @param name
	 * @param description
	 */
	private void updateImage(Document image, String name, String description) {

		// sada na naziv moramo da vratimo ekstenziju
		String extension = image.getMetadata().getExtension();
		name = name + "." + extension;

		image.setName(name);
		image.getMetadata().setDescription(description);

		image.getMetadata().setUpdatedBy(SecurityUtils.getUserId().toString());
		image.getMetadata().setUpdatedOn(Calendar.getInstance());

		repositoryManager.edit(image);
	}

	/**
	 * Metoda ubacuje slike u globalni album.
	 * 
	 * @param albumId
	 * @param attachments
	 */
	public void addGlobalImagesToAlbum(String albumId, List<AttachmentDTO> attachments) {

		addAnyImagesToAlbum(albumId, attachments, false);
	}

	public void saveUserProfileImage(Long userId, String profileImage) {

		if (StringUtils.isNotBlank(profileImage)) {

			String folderPath = "/users/" + userId;

			repositoryManager.saveFileToRepository(folderPath, "normalImage", profileImage, 250, 250);

			repositoryManager.saveFileToRepository(folderPath, "smallImage", profileImage, 50, 50);
		}
	}

	public void addImageUploaderData(ModelAndView mav, String imagePath) {

		String imageUrl = applicationUrl + "resource?path=" + imagePath;

		int doubleSlashIndex = imageUrl.lastIndexOf("//");

		// mozda sada imamo dva puta slash, moramo ukloniti jedan, > 10 jer
		// imamo // posle http://
		if (doubleSlashIndex > 10) {

			imageUrl = imageUrl.substring(0, doubleSlashIndex) + imageUrl.substring(doubleSlashIndex + 1);
		}
		
		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

		mav.addObject("token", sessionId);
		mav.addObject("imageUrl", imageUrl);
		mav.addObject("fileSizeLimit", FILE_SIZE_LIMIT);
		mav.addObject("fileCountLimit", 1);
	}

	public void addGalleryUploaderData(ModelAndView mav, int fileCountLimit) {

		String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();

		mav.addObject("token", sessionId);
		mav.addObject("fileSizeLimit", FILE_SIZE_LIMIT);
		mav.addObject("fileCountLimit", fileCountLimit);
	}

	public int getDrivingSchoolNumberOfImages(Long schoolId) {

		List<DocumentDTO> drivingSchoolAlbums = getDrivingSchoolAlbums(schoolId);

		int totalImages = 0;

		for (DocumentDTO dto : drivingSchoolAlbums) {

			List<DocumentDTO> albumImages = getAlbumImages(dto.getId());

			totalImages += albumImages.size();
		}

		return totalImages;
	}

}
