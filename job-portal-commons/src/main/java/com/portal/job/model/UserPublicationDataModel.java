package com.portal.job.model;



public class UserPublicationDataModel {

	// Member fields.
	private String publicationId;
	private String publicationDate;
	private String publicationDescription;
	private String publicationOrganisation;
	private String publicationTitle;
	
	public UserPublicationDataModel(){
		
	}

	public UserPublicationDataModel(String publicationId, String publicationDate,
			String publicationDescription, String publicationOrganisation,
			String publicationTitle) {
		super();
		this.publicationId = publicationId;
		this.publicationDate = publicationDate;
		this.publicationDescription = publicationDescription;
		this.publicationOrganisation = publicationOrganisation;
		this.publicationTitle = publicationTitle;
	}

	@Override
	public String toString() {
		return "UserPublicationDataModel [publicationId=" + publicationId
				+ ", publicationDate=" + publicationDate
				+ ", publicationDescription=" + publicationDescription
				+ ", publicationOrganisation=" + publicationOrganisation
				+ ", publicationTitle=" + publicationTitle + "]";
	}

	public String getPublicationOrganisation() {
		return publicationOrganisation;
	}

	public void setPublicationOrganisation(String publicationOrganisation) {
		this.publicationOrganisation = publicationOrganisation;
	}

	public String getPublicationTitle() {
		return publicationTitle;
	}

	public void setPublicationTitle(String publicationTitle) {
		this.publicationTitle = publicationTitle;
	}

	// Getters And Setters
	public String getPublicationId() {
		return publicationId;
	}

	public void setPublicationId(String publicationId) {
		this.publicationId = publicationId;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getPublicationDescription() {
		return publicationDescription;
	}

	public void setPublicationDescription(String publicationDescription) {
		this.publicationDescription = publicationDescription;
	}
	
}
