package com.evola.edt.jcr;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Repository manager handles all content related actions
 * 
 * @author Nikola
 * 
 */
public interface ContentRepositoryManager {
	/**
	 * @param documentId
	 * @return
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public Document get(String documentId) throws ContentRepositoryException;

	/**
	 * @param path
	 * @return
	 * @author Nikola
	 */
	public Document getByPath(final String path);

	/**
	 * @param path
	 * @return
	 */
	public Boolean existsOnPath(final String path);

	/**
	 * Adds the document to the parent document with give id.<br>
	 * Document of type {@link DocumentType#FILE} can't be child of another
	 * document of type {@link DocumentType#FILE}<br>
	 * Document of type {@link DocumentType#FOLDER} can't be child of another
	 * document of type {@link DocumentType#FILE}<br>
	 * 
	 * @param document
	 * @param parentDocumentId
	 * @return
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public String add(Document document, String parentDocumentId)
			throws ContentRepositoryException;

	/**
	 * Adds document to the root of the repository.<br>
	 * Aldought file can be added it is usual to add a folder to the root of the
	 * repository.
	 * 
	 * @param document
	 * @return
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public String add(Document document) throws ContentRepositoryException;

	/**
	 * Edits {@link Document}
	 * 
	 * @param document
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public void edit(Document document) throws ContentRepositoryException;

	/**
	 * Sets {@link Document} file preview.
	 * 
	 * @param document
	 * @throws ContentRepositoryException
	 */
	public void editDocumentFilePreview(Document document,
			InputStream filePreviewInputStream)
			throws ContentRepositoryException;

	/**
	 * Deletes the {@link Document}
	 * 
	 * @param documentId
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public void delete(String documentId) throws ContentRepositoryException;;

	/**
	 * Moves the {@link Document} to be child of another {@link Document}.
	 * Folder can't be moved to a file and file can't be moved to a file.
	 * 
	 * @param documentToMoveId
	 * @param parentDocumentToMoveToId
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public void move(String documentToMoveId, String parentDocumentToMoveToId)
			throws ContentRepositoryException;;

	/**
	 * Check out the {@link Document}. Document has checkout flag to set to
	 * true.
	 * 
	 * @param documentId
	 * @return
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public Document checkOut(String documentId)
			throws ContentRepositoryException;;

	/**
	 * Checks in the document
	 * 
	 * @param document
	 * @param comment
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public void checkIn(final Document document, String comment)
			throws ContentRepositoryException;

	/**
	 * Get children of the document
	 * 
	 * @param documentId
	 * @return
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public List<Document> getChildren(String documentId)
			throws ContentRepositoryException;

	/**
	 * Get parent of the document
	 * 
	 * @param documentId
	 * @return
	 * @throws ContentRepositoryException
	 * @author Nikola
	 */
	public Document getParent(String documentId)
			throws ContentRepositoryException;

	/**
	 * Gets list of {@link Version}s for the given document
	 * 
	 * @param documentId
	 * @return
	 * @author Nikola
	 */
	public List<com.evola.edt.jcr.Version> getVersionHistory(String documentId);

	/**
	 * Gets {@link Document} by revision number
	 * 
	 * @param revisionNumber
	 * @param documentId
	 * @return
	 * @author Nikola
	 */
	public Document getByRevision(Integer revisionNumber, String documentId);

	/**
	 * Restore document to given revision
	 * 
	 * @param revisionToRestoreTo
	 * @param documentId
	 * @author Nikola
	 */
	public void restoreToRevision(Integer revisionToRestoreTo, String documentId);

	/**
	 * Metoda cuva novi fajl iz temp foldera i stavlja mu za putanju prosledjenu
	 * vrednosti.
	 * 
	 * @param path
	 * @param imageId
	 */
	public void saveFileToRepository(String folderRepositoryPath,
			String fileRepositoryId, String imageId, int width, int height);
	
	/**
	 * Metoda vraca id dokumenta na prosledjenoj putanji a ako dokument ne postoji
	 * pravi sva dokumenta na prosledjenoj putanji pre vracanja vrednosti.
	 * @param parentFolderPath
	 * @return
	 */
	public String getParentNodeId(String parentFolderPath);
	
	public Metadata getMetadata(Long userId, Calendar calendar, double size);

}
