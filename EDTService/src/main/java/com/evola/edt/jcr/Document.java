package com.evola.edt.jcr;

import java.io.InputStream;

import org.springframework.util.Assert;

/**
 * @author Nikola
 * 
 */
public class Document {
	private String identifier;
	private String name;
	private String path;
	private InputStream content;
	private DocumentType documentType;
	private Boolean checkedOut;
	private Metadata metadata;
	private String parentId;

	/**
	 * @param documentType
	 * @param name
	 * @author Nikola
	 */
	public Document(DocumentType documentType, String name) {
		Metadata metadata = new Metadata();
		Assert.notNull(name);
		Assert.notNull(documentType);
		this.name = name;
		this.documentType = documentType;
		this.metadata = metadata;
	}

	/**
	 * @param documentType
	 * @param name
	 * @param metadata
	 * @author Nikola
	 */
	public Document(DocumentType documentType, String name, Metadata metadata) {
		super();
		Assert.notNull(name);
		Assert.notNull(documentType);
		Assert.notNull(metadata);
		this.name = name;
		this.documentType = documentType;
		this.metadata = metadata;
	}

	/**
	 * Creates {@link Document} of type file
	 * 
	 * @param name
	 * @param content
	 * @param metadata
	 * @author Nikola
	 */
	public Document(String name, InputStream content, Metadata metadata) {
		super();
		Assert.notNull(name);
		Assert.notNull(metadata);
		Assert.notNull(content);
		this.name = name;
		this.documentType = DocumentType.FILE;
		this.content = content;
		this.metadata = metadata;
	}

	/**
	 * @return Name of the document
	 * @author Nikola
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the document
	 * 
	 * @param name
	 * @author Nikola
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Identifier of the document
	 * @author Nikola
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * This method should not be used
	 * 
	 * @param identifier
	 * @author Nikola
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return {@link DocumentType}
	 * @author Nikola
	 */
	public DocumentType getDocumentType() {
		return documentType;
	}

	/**
	 * @return content
	 * @author Nikola
	 */
	public InputStream getContent() {
		return content;
	}

	/**
	 * @param content
	 * @author Nikola
	 */
	public void setContent(InputStream content) {
		if (this.documentType != null && this.documentType.equals(DocumentType.FILE)) {
			this.content = content;
		} else {
			throw new IllegalArgumentException("Content can be set only on document of type FILE.");
		}
	}

	/**
	 * Indicates is this document checked out
	 * 
	 * @return
	 * @author Nikola
	 */
	public Boolean getCheckedOut() {
		return checkedOut;
	}

	/**
	 * This method should not be used
	 * 
	 * @param checkedOut
	 * @author Nikola
	 */
	public void setCheckedOut(Boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	/**
	 * @return metadata
	 * @author Nikola
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata
	 * @author Nikola
	 */
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return Path from root of the repository
	 * @author Nikola
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @param path
	 * @author Nikola
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
