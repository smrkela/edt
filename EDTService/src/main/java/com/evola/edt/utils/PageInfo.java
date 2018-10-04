package com.evola.edt.utils;

public class PageInfo {

	public String name;
	public String category;
	public String title;
	public String htmlTitle;
	public String htmlDescription;
	public String fbLink;
	public String fbDescription;
	public String fbImage;
	public String fbCategory;
	public String fbSection;
	

	public PageInfo(String name, String title, String category) {

		this(name, title, category, "Vozaci Srbije | " + title, "Vozaci Srbije | " + title);
	}

	public PageInfo(String name, String title, String category, String htmlTitle, String htmlDescription) {

		this.name = name;
		this.title = title;
		this.category = category;
		this.htmlTitle = htmlTitle;
		this.htmlDescription = htmlDescription;
	}
	
	/**
	 * Metoda se koristi za postavljanje svih tagova koje Facebook zahteva
	 * @param name
	 * @param title
	 * @param category
	 * @param htmlTitle
	 * @param htmlDescription
	 * @param fbLink
	 * @param fbDescription
	 * @param fbImage
	 * @param fbCategory
	 * @param fbSection
	 */
	public PageInfo(String name, String title, String category, String htmlTitle, String htmlDescription, String fbLink, String fbDescription, 
					String fbImage, String fbCategory, String fbSection) {

		this.name = name;
		this.title = title;
		this.category = category;
		this.htmlTitle = htmlTitle;
		this.htmlDescription = htmlDescription;
		this.fbLink = fbLink;
		this.fbDescription = fbDescription;
		this.fbImage = fbImage;
		this.fbCategory = fbCategory;
		this.fbSection = fbSection;
	}

}
