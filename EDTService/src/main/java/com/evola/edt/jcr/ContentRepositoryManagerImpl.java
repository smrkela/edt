package com.evola.edt.jcr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Binary;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.evola.edt.component.TempUploadComponent;
import com.evola.edt.component.UploadedFile;
import com.evola.edt.web.security.SecurityUtils;

/**
 * @author Nikola
 * 
 */
public class ContentRepositoryManagerImpl implements ContentRepositoryManager {

	private static final String MODIFIED_BY = "modifiedBy";
	private static final String MODIFIED_ON = "modifiedOn";
	private static final String CREATED_BY = "createdBy";
	private static final String CREATED_ON = "createdOn";
	private static final String CONTENT = "content";
	private static final String NODE_TYPE = "nodeType";
	private static final String COMMENT = "comment";
	private static final String DESCRIPTION = "description";
	private static final String SIZE = "size";
	private static final String EXTENSION = "extension";
	private static final String CLIENT = "client";
	private static final String MIME_TYPE = "mimeType";
	private static final String PREVIEW = "preview";
	private static final String FILE_PREVIEW = "filePreview";

	private JcrTemplate jcrTemplate;

	@Inject
	private TempUploadComponent uploadComponent;

	private ContentRepositoryManagerImpl(JcrTemplate jcrTemplate) {
		super();
		this.jcrTemplate = jcrTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#get(java.lang.String
	 * )
	 */
	public Document get(final String documentId) {

		Document document = null;

		try {

			document = jcrTemplate.execute(new JcrCallback<Document>() {
				public Document doInJcr(Session session) throws RepositoryException {
					Node node = session.getNodeByIdentifier(documentId);
					Document doc = getDocumentFromNode(node);
					return doc;
				}

			});
		} catch (ContentRepositoryException e) {

			// nema objekta, to je ok
		}

		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see management.ContentRepositoryManager#getByPath (java.lang.String)
	 */
	public Document getByPath(final String path) {

		Document document = null;

		try {

			document = jcrTemplate.execute(new JcrCallback<Document>() {
				public Document doInJcr(Session session) throws RepositoryException {
					Node node = session.getNode(path);
					Document doc = getDocumentFromNode(node);
					return doc;
				}

			});
		} catch (ContentRepositoryException e) {

			// element nije pronadjen, to je ok
		}

		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see management.ContentRepositoryManager#existsOnPath (java.lang.String)
	 */
	public Boolean existsOnPath(final String path) {
		return jcrTemplate.execute(new JcrCallback<Boolean>() {
			public Boolean doInJcr(Session session) throws RepositoryException {
				return session.nodeExists(path);
			}

		});
	}

	/**
	 * @param node
	 * @return
	 * @throws PathNotFoundException
	 * @throws RepositoryException
	 * @throws ValueFormatException
	 */
	private Document getDocumentFromNode(Node node) throws PathNotFoundException, RepositoryException,
			ValueFormatException {
		Property nodeType = node.getProperty(NODE_TYPE);
		Metadata metadata = new Metadata();
		DocumentType documentType = DocumentType.valueOf(nodeType.getString());
		Document doc = new Document(documentType, node.getName(), metadata);
		doc.setPath(node.getPath());
		doc.setIdentifier(node.getIdentifier());
		doc.setCheckedOut(node.isCheckedOut());
		doc.setParentId(node.getParent().getIdentifier());

		if (documentType.equals(DocumentType.FILE)) {
			Property property = node.getProperty(CONTENT);
			Binary binary = property.getBinary();
			doc.setContent(binary.getStream());
		}

		Property created = node.getProperty(CREATED_BY);
		metadata.setCreatedBy(created.getString());
		Property createdOn = node.getProperty(CREATED_ON);
		metadata.setCreatedOn(createdOn.getDate());
		Property modified = node.getProperty(MODIFIED_BY);
		metadata.setUpdatedBy(modified.getString());
		Property modifiedOn = node.getProperty(MODIFIED_ON);
		metadata.setUpdatedOn(modifiedOn.getDate());
		Property description = node.getProperty(DESCRIPTION);
		metadata.setDescription(description.getString());

		try {

			// posto smo size ubacili kasnije neke node nemaju ovaj atribut
			Property size = node.getProperty(SIZE);
			metadata.setSize(size.getDouble());

		} catch (PathNotFoundException pnfe) {

			metadata.setSize(0d);
		}

		try {

			// posto smo extension ubacili kasnije neke node nemaju ovaj atribut
			Property extension = node.getProperty(EXTENSION);
			metadata.setExtension(extension.getString());

		} catch (PathNotFoundException pnfe) {

			metadata.setExtension("");
		}

		try {

			// posto smo extension ubacili kasnije neke node nemaju ovaj atribut
			Property client = node.getProperty(CLIENT);
			metadata.setClient(client.getString());

		} catch (PathNotFoundException pnfe) {

			metadata.setClient("");
		}

		try {

			// posto smo mime ubacili kasnije neke node nemaju ovaj atribut
			Property client = node.getProperty(MIME_TYPE);
			metadata.setMimeType(client.getString());

		} catch (PathNotFoundException pnfe) {

			metadata.setMimeType("");
		}

		try {

			Property property = node.getProperty(PREVIEW);
			Binary binary = property.getBinary();
			metadata.setPreview(binary.getStream());

		} catch (PathNotFoundException pnfe) {

			metadata.setPreview(null);
		}

		try {

			Property property = node.getProperty(FILE_PREVIEW);
			Binary binary = property.getBinary();
			metadata.setFilePreview(binary.getStream());

		} catch (PathNotFoundException pnfe) {

			metadata.setFilePreview(null);
		}

		return doc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#add(com.alokinsoft
	 * .cr.Document, java.lang.String)
	 */
	public String add(final Document document, final String parentDocumentId) throws ContentRepositoryException {
		Assert.notNull(document);

		String identifier = jcrTemplate.execute(new JcrCallback<String>() {
			public String doInJcr(Session session) throws javax.jcr.RepositoryException {
				return addDocument(document, parentDocumentId, session);
			}

		});

		return identifier;
	}

	/**
	 * @param document
	 * @param parentDocumentId
	 * @param session
	 * @return
	 * @throws ItemNotFoundException
	 * @throws RepositoryException
	 */
	private String addDocument(final Document document, final String parentDocumentId, Session session)
			throws ItemNotFoundException, RepositoryException {
		Node rootNode = null;
		if (StringUtils.hasText(parentDocumentId)) {
			rootNode = session.getNodeByIdentifier(parentDocumentId);
		} else {
			rootNode = session.getRootNode();
		}
		Assert.notNull(rootNode);
		Node addNode = addDocumentToNode(document, rootNode, session);
		return addNode.getIdentifier();
	}

	/**
	 * @param document
	 * @param rootNode
	 * @param session
	 * @return
	 * @throws javax.jcr.RepositoryException
	 */
	private Node addDocumentToNode(final Document document, Node rootNode, Session session)
			throws javax.jcr.RepositoryException {
		String name = document.getName();
		DocumentType documentType = document.getDocumentType();

		Property rootNodeType = null;
		try {
			rootNodeType = rootNode.getProperty(NODE_TYPE);
		} catch (Exception e) {
			// handle exception when property not exists
		}
		verifyNodeAppend(documentType, rootNodeType);

		Node addNode = null;
		addNode = rootNode.addNode(name);
		addNode.setProperty(NODE_TYPE, documentType.name());
		if (documentType.equals(DocumentType.FILE)) {
			if (document.getContent() == null) {
				throw new IllegalStateException("Document of type FILE must have content");
			}
			ValueFactory vf = session.getValueFactory();
			Binary binary = vf.createBinary(document.getContent());
			addNode.setProperty(CONTENT, binary);
			addNode.addMixin("mix:versionable");
		}
		addMetadata(document, addNode, session);
		return addNode;
	}

	/**
	 * @param nodeType
	 * @param rootNodeType
	 * @throws ValueFormatException
	 * @throws RepositoryException
	 */
	private void verifyNodeAppend(final DocumentType nodeType, Property rootNodeType) throws ValueFormatException,
			RepositoryException {
		if (rootNodeType != null) {
			DocumentType rootDocumentType = DocumentType.valueOf(rootNodeType.getString());
			if (rootDocumentType.equals(DocumentType.FILE) && nodeType.equals(DocumentType.FILE)) {
				throw new IllegalStateException(
						"Document of type FILE can't be child of a another document of type FILE");
			}
			if (rootDocumentType.equals(DocumentType.FILE) && nodeType.equals(DocumentType.FOLDER)) {
				throw new IllegalStateException("Document of type FOLDER can't be child of a document of type FILE");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#add(com.alokinsoft
	 * .cr.Document)
	 */
	public String add(final Document document) throws ContentRepositoryException {
		return add(document, null);
	}

	/**
	 * @param document
	 * @param addNode
	 * @throws ValueFormatException
	 * @throws VersionException
	 * @throws LockException
	 * @throws ConstraintViolationException
	 * @throws RepositoryException
	 */
	private void addMetadata(final Document document, Node addNode, Session session) throws ValueFormatException,
			VersionException, LockException, ConstraintViolationException, RepositoryException {
		Metadata metadata = document.getMetadata();
		Calendar createdOn = metadata.getCreatedOn();
		addNode.setProperty(CREATED_ON, createdOn == null ? Calendar.getInstance() : createdOn);
		String createdBy = metadata.getCreatedBy();
		addNode.setProperty(CREATED_BY, createdBy == null ? "" : createdBy);
		Calendar updatedOn = metadata.getUpdatedOn();
		addNode.setProperty(MODIFIED_ON, updatedOn == null ? Calendar.getInstance() : updatedOn);
		String updatedBy = metadata.getUpdatedBy();
		addNode.setProperty(MODIFIED_BY, updatedBy == null ? "" : updatedBy);

		String description = metadata.getDescription();
		addNode.setProperty(DESCRIPTION, description == null ? "" : description);

		Double size = metadata.getSize();
		addNode.setProperty(SIZE, size == null ? 0 : size);

		String extension = metadata.getExtension();
		addNode.setProperty(EXTENSION, extension == null ? "" : extension);

		String client = metadata.getClient();
		addNode.setProperty(CLIENT, client == null ? "" : client);

		String mimeType = metadata.getMimeType();
		addNode.setProperty(MIME_TYPE, mimeType == null ? "" : mimeType);

		InputStream previewStream = metadata.getPreview();
		if (previewStream != null) {
			ValueFactory vf = session.getValueFactory();
			Binary binary = vf.createBinary(previewStream);
			addNode.setProperty(PREVIEW, binary);
		}

		InputStream filePreview = metadata.getFilePreview();
		if (filePreview != null) {
			ValueFactory vf = session.getValueFactory();
			Binary binary = vf.createBinary(filePreview);
			addNode.setProperty(FILE_PREVIEW, binary);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#edit(com.alokinsoft
	 * .cr.Document)
	 */
	public void edit(final Document document) {
		Assert.notNull(document);
		Assert.notNull(document.getIdentifier());
		jcrTemplate.execute(new JcrCallback<Void>() {
			public Void doInJcr(Session session) throws javax.jcr.RepositoryException {
				editDocument(document, session);
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#edit(com.alokinsoft
	 * .cr.Document)
	 */
	public void editDocumentFilePreview(final Document document, final InputStream filePreviewInputStream)
			throws ContentRepositoryException {
		Assert.notNull(document);
		Assert.notNull(document.getIdentifier());
		jcrTemplate.execute(new JcrCallback<Void>() {
			public Void doInJcr(Session session) throws javax.jcr.RepositoryException {
				setDocumentFilePreview(document, filePreviewInputStream, session);
				return null;
			}

		});
	}

	/**
	 * @param document
	 * @param session
	 * @throws ItemNotFoundException
	 * @throws RepositoryException
	 * @throws UnsupportedRepositoryOperationException
	 * @throws ValueFormatException
	 * @throws VersionException
	 * @throws LockException
	 * @throws ConstraintViolationException
	 */
	private void editDocument(final Document document, Session session) throws ItemNotFoundException,
			RepositoryException, UnsupportedRepositoryOperationException, ValueFormatException, VersionException,
			LockException, ConstraintViolationException {
		Node nodeByIdentifier = session.getNodeByIdentifier(document.getIdentifier());

		changeDocumentName(document, session, nodeByIdentifier);

		DocumentType documentType = document.getDocumentType();

		if (documentType.equals(DocumentType.FILE)) {
			if (document.getContent() == null) {
				throw new IllegalStateException("Document of type FILE must have content");
			}
			ValueFactory vf = session.getValueFactory();
			Binary binary = vf.createBinary(document.getContent());
			nodeByIdentifier.setProperty(CONTENT, binary);
		}

		nodeByIdentifier.setProperty(MODIFIED_ON, Calendar.getInstance());

		Metadata metadata = document.getMetadata();

		String updatedBy = metadata.getUpdatedBy();

		nodeByIdentifier.setProperty(MODIFIED_BY, updatedBy == null ? "" : updatedBy);

		String description = metadata.getDescription();

		nodeByIdentifier.setProperty(DESCRIPTION, description == null ? "" : description);

		Double size = metadata.getSize();

		nodeByIdentifier.setProperty(SIZE, size == null ? 0 : size);

		String extension = metadata.getExtension();

		nodeByIdentifier.setProperty(EXTENSION, extension == null ? "" : extension);

		String mimeType = metadata.getMimeType();

		nodeByIdentifier.setProperty(MIME_TYPE, mimeType == null ? "" : mimeType);

		if (metadata.getPreview() != null) {
			ValueFactory vf = session.getValueFactory();
			Binary previewBinary = vf.createBinary(metadata.getPreview());
			nodeByIdentifier.setProperty(PREVIEW, previewBinary);
		}

		if (metadata.getFilePreview() != null) {
			ValueFactory vf = session.getValueFactory();
			Binary filePreviewBinary = vf.createBinary(metadata.getFilePreview());
			nodeByIdentifier.setProperty(FILE_PREVIEW, filePreviewBinary);
		}
	}

	/**
	 * @param document
	 * @param session
	 * @throws ItemNotFoundException
	 * @throws RepositoryException
	 * @throws UnsupportedRepositoryOperationException
	 * @throws ValueFormatException
	 * @throws VersionException
	 * @throws LockException
	 * @throws ConstraintViolationException
	 */
	private void setDocumentFilePreview(final Document document, InputStream filePreviewInputStream, Session session)
			throws ItemNotFoundException, RepositoryException, UnsupportedRepositoryOperationException,
			ValueFormatException, VersionException, LockException, ConstraintViolationException {

		Node nodeByIdentifier = session.getNodeByIdentifier(document.getIdentifier());

		ValueFactory vf = session.getValueFactory();
		Binary filePreviewBinary = vf.createBinary(filePreviewInputStream);
		nodeByIdentifier.setProperty(FILE_PREVIEW, filePreviewBinary);
	}

	/**
	 * @param document
	 * @param session
	 * @param nodeByIdentifier
	 * @throws RepositoryException
	 */
	private void changeDocumentName(final Document document, Session session, Node nodeByIdentifier)
			throws RepositoryException {
		if (!nodeByIdentifier.getName().equals(document.getName())) {
			String path = nodeByIdentifier.getParent().getPath();
			session.move(nodeByIdentifier.getPath(), (path.endsWith("/") ? path : path + "/") + document.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#delete(java.lang
	 * .String)
	 */
	public void delete(final String documentId) {
		Assert.notNull(documentId);
		jcrTemplate.execute(new JcrCallback<Void>() {
			public Void doInJcr(Session session) throws javax.jcr.RepositoryException {

				Node nodeByIdentifier = session.getNodeByIdentifier(documentId);
				nodeByIdentifier.remove();
				return null;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#move(java.lang.
	 * String, java.lang.String)
	 */
	public void move(final String documentToMoveId, final String parentDocumentToMoveToId) {
		Assert.notNull(documentToMoveId);
		Assert.notNull(parentDocumentToMoveToId);
		jcrTemplate.execute(new JcrCallback<Void>() {
			public Void doInJcr(Session session) throws javax.jcr.RepositoryException {
				Node nodeToMove = session.getNodeByIdentifier(documentToMoveId);
				Node parentNodeToMoveTo = session.getNodeByIdentifier(parentDocumentToMoveToId);
				DocumentType documentType = DocumentType.valueOf(nodeToMove.getProperty(NODE_TYPE).getString());
				verifyNodeAppend(documentType, parentNodeToMoveTo.getProperty(NODE_TYPE));

				String newPath = parentNodeToMoveTo.getPath();

				newPath += "/" + nodeToMove.getName();

				session.move(nodeToMove.getPath(), newPath);
				return null;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#getChildren(java
	 * .lang.String)
	 */
	public List<Document> getChildren(final String documentId) {
		Assert.notNull(documentId);
		List<Document> documents = jcrTemplate.execute(new JcrCallback<List<Document>>() {
			public List<Document> doInJcr(Session session) throws javax.jcr.RepositoryException {

				Node nodeByIdentifier = session.getNodeByIdentifier(documentId);
				NodeIterator nodes = nodeByIdentifier.getNodes();
				List<Document> returnList = new ArrayList<Document>();
				while (nodes.hasNext()) {
					Object next = nodes.next();
					Node node = (Node) next;
					Document documentFromNode = getDocumentFromNode(node);
					returnList.add(documentFromNode);
				}
				return returnList;
			}
		});
		return documents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#getParent(java.
	 * lang.String)
	 */
	public Document getParent(final String documentId) throws ContentRepositoryException {
		Assert.notNull(documentId);
		Document document = jcrTemplate.execute(new JcrCallback<Document>() {
			public Document doInJcr(Session session) throws javax.jcr.RepositoryException {
				Node nodeByIdentifier = session.getNodeByIdentifier(documentId);
				Node node = (Node) nodeByIdentifier.getParent();
				Document documentFromNode = getDocumentFromNode(node);
				return documentFromNode;
			}
		});
		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#checkIn(com.alokinsoft
	 * .cr.Document, java.lang.String)
	 */
	public void checkIn(final Document document, final String comment) throws ContentRepositoryException {
		Assert.notNull(document);
		Assert.notNull(document.getIdentifier());
		Assert.notNull(document.getMetadata());
		Assert.notNull(document.getMetadata().getUpdatedBy());
		Assert.notNull(comment);
		jcrTemplate.execute(new JcrCallback<Void>() {
			public Void doInJcr(Session session) throws javax.jcr.RepositoryException {
				editDocument(document, session);
				Node nodeByIdentifier = session.getNodeByIdentifier(document.getIdentifier());
				nodeByIdentifier.setProperty(COMMENT, comment);
				session.save();
				VersionManager vm = session.getWorkspace().getVersionManager();
				VersionHistory versionHistory = vm.getVersionHistory(nodeByIdentifier.getPath());
				Integer count = getNextRevision(versionHistory);
				Version version = vm.checkin(nodeByIdentifier.getPath());
				versionHistory.addVersionLabel(version.getName(), count.toString(), false);
				return null;
			}

		});

	}

	/**
	 * @param versionHistory
	 * @return
	 * @throws RepositoryException
	 */
	private Integer getNextRevision(VersionHistory versionHistory) throws RepositoryException {
		VersionIterator allVersions = versionHistory.getAllVersions();
		Integer count = 0;
		while (allVersions.hasNext()) {
			allVersions.next();
			count++;
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#checkOut(java.lang
	 * .String)
	 */
	public Document checkOut(final String documentId) throws ContentRepositoryException {
		Assert.notNull(documentId);
		Document document = jcrTemplate.execute(new JcrCallback<Document>() {
			public Document doInJcr(Session session) throws javax.jcr.RepositoryException {
				VersionManager vm = session.getWorkspace().getVersionManager();
				Node nodeByIdentifier = session.getNodeByIdentifier(documentId);
				vm.checkout(nodeByIdentifier.getPath());
				Document document = getDocumentFromNode(session.getNodeByIdentifier(documentId));
				return document;
			}
		});
		return document;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#getVersionHistory
	 * (java.lang.String)
	 */
	public List<com.evola.edt.jcr.Version> getVersionHistory(final String documentId) {
		Assert.notNull(documentId);
		return jcrTemplate.execute(new JcrCallback<List<com.evola.edt.jcr.Version>>() {
			public List<com.evola.edt.jcr.Version> doInJcr(Session session) throws javax.jcr.RepositoryException {
				Document document = getDocumentFromNode(session.getNodeByIdentifier(documentId));
				DocumentType documentType = document.getDocumentType();
				if (!documentType.equals(DocumentType.FILE)) {
					throw new IllegalArgumentException("Can't get version history for node type different than FILE.");
				}
				VersionManager vm = session.getWorkspace().getVersionManager();
				Node nodeByIdentifier = session.getNodeByIdentifier(document.getIdentifier());
				VersionHistory versionHistory = vm.getVersionHistory(nodeByIdentifier.getPath());
				VersionIterator allVersions = versionHistory.getAllVersions();
				List<com.evola.edt.jcr.Version> versions = new ArrayList<com.evola.edt.jcr.Version>();
				while (allVersions.hasNext()) {
					Version version = (Version) allVersions.next();
					if (!version.getName().equals("jcr:rootVersion")) {
						String[] versionLabels = versionHistory.getVersionLabels(version);
						if (versionLabels.length != 1) {
							throw new IllegalStateException("Version must contain at least one label");
						}
						Node frozenNode = version.getFrozenNode();
						String comment = frozenNode.getProperty(COMMENT).getString();
						String author = frozenNode.getProperty(MODIFIED_BY).getString();
						Integer revision = Integer.valueOf(versionLabels[0]);
						com.evola.edt.jcr.Version v = new com.evola.edt.jcr.Version(revision, version.getCreated(),
								comment, author);
						versions.add(v);
					}
				}
				return versions;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#getByRevision(java
	 * .lang.Integer, java.lang.String)
	 */
	public Document getByRevision(final Integer revisionNumber, final String documentId) {
		Assert.notNull(revisionNumber);
		Assert.notNull(documentId);
		return jcrTemplate.execute(new JcrCallback<Document>() {
			public Document doInJcr(Session session) throws javax.jcr.RepositoryException {
				Document document = getDocumentFromNode(session.getNodeByIdentifier(documentId));
				DocumentType documentType = document.getDocumentType();
				if (!documentType.equals(DocumentType.FILE)) {
					throw new IllegalArgumentException("Can't get version for node type different than FILE.");
				}
				VersionManager vm = session.getWorkspace().getVersionManager();
				Node nodeByIdentifier = session.getNodeByIdentifier(document.getIdentifier());
				VersionHistory versionHistory = vm.getVersionHistory(nodeByIdentifier.getPath());
				Version versionByLabel = versionHistory.getVersionByLabel(revisionNumber.toString());
				Document documentFromNode = getDocumentFromNode(versionByLabel.getFrozenNode());
				return documentFromNode;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alokinsoft.cr.management.ContentRepositoryManager#restoreToRevision
	 * (java.lang.Integer, java.lang.String)
	 */
	public void restoreToRevision(final Integer revisionToRestoreTo, final String documentId) {
		Assert.notNull(revisionToRestoreTo);
		Assert.notNull(documentId);
		jcrTemplate.execute(new JcrCallback<Void>() {
			public Void doInJcr(Session session) throws javax.jcr.RepositoryException {
				Document document = getDocumentFromNode(session.getNodeByIdentifier(documentId));
				DocumentType documentType = document.getDocumentType();
				if (!documentType.equals(DocumentType.FILE)) {
					throw new IllegalArgumentException("Can't get version for node type different than FILE.");
				}
				VersionManager vm = session.getWorkspace().getVersionManager();
				Node nodeByIdentifier = session.getNodeByIdentifier(document.getIdentifier());
				vm.restoreByLabel(nodeByIdentifier.getPath(), revisionToRestoreTo.toString(), false);
				return null;
			}
		});
	}

	@Override
	public void saveFileToRepository(String folderRepositoryPath, String fileRepositoryName, String imageId, int width,
			int height) {

		UploadedFile tempFile = uploadComponent.getTempFile(imageId);
		InputStream inputStream = tempFile.getInputStream();

		String repositoryPath = folderRepositoryPath + "/" + fileRepositoryName;

		boolean imageExists = existsOnPath(repositoryPath);

		InputStream in = null;

		int fileSize = 0;

		if (width > 0 && height > 0) {

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {

				if (width != -1 && height != -1)
					Thumbnails.of(inputStream).outputFormat("jpg").size(width, height).toOutputStream(os);
				else if (width != -1)
					Thumbnails.of(inputStream).outputFormat("jpg").width(width).toOutputStream(os);
				else if (height != -1)
					Thumbnails.of(inputStream).outputFormat("jpg").height(height).toOutputStream(os);
				else
					Thumbnails.of(inputStream).outputFormat("jpg").toOutputStream(os);

			} catch (IOException e) {

				throw new ContentRepositoryException(e);
			}

			fileSize = os.size();

			in = new ByteArrayInputStream(os.toByteArray());
		}

		Long currentUserId = SecurityUtils.getUserId();

		Document image = null;

		if (imageExists) {

			image = getByPath(repositoryPath);

			Metadata meta = image.getMetadata();
			meta.setUpdatedBy(currentUserId.toString());
			meta.setUpdatedOn(Calendar.getInstance());
			meta.setSize((double) fileSize);
			meta.setExtension("jpg");
			meta.setMimeType("image/jpg");

			image.setContent(in);

			edit(image);

		} else {

			Metadata metaData = getMetadata(currentUserId, Calendar.getInstance(), fileSize);
			image = new Document(DocumentType.FILE, fileRepositoryName, metaData);
			image.setContent(in);

			add(image, getParentNodeId(folderRepositoryPath));
		}
	}

	public Metadata getMetadata(Long userId, Calendar calendar, double size) {

		Metadata meta = new Metadata();

		meta.setCreatedBy(userId.toString());
		meta.setCreatedOn(calendar);
		meta.setDescription("");
		meta.setUpdatedBy(userId.toString());
		meta.setUpdatedOn(calendar);
		meta.setSize(size);
		meta.setExtension("jpg");
		meta.setMimeType("image/jpg");

		return meta;
	}

	public String getParentNodeId(String parentFolderPath) {

		String parentId = null;

		if (existsOnPath(parentFolderPath)) {

			parentId = getByPath(parentFolderPath).getIdentifier();

		} else {

			// ako nismo nasli odgovarajci folder onda ga moramo napraviti
			// idemo redom i pravimo node

			String[] pathParts = parentFolderPath.split("/");

			String currentPath = "";

			for (int i = 0; i < pathParts.length; i++) {

				String part = pathParts[i];

				if (part.equals(""))
					continue;

				if (i > 0)
					currentPath += "/" + part;
				else
					currentPath = part;

				if (existsOnPath(currentPath)) {

					parentId = getByPath(currentPath).getIdentifier();
				} else {

					Document folderDocument = new Document(DocumentType.FOLDER, part);

					if (parentId != null)
						parentId = add(folderDocument, parentId);
					else
						parentId = add(folderDocument);
				}
			}
		}

		return parentId;
	}

}
