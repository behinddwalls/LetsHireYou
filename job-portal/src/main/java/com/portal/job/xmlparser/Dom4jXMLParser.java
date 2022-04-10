package com.portal.job.xmlparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dom4jXMLParser {
	private static Logger log = LoggerFactory.getLogger(Dom4jXMLParser.class);

/*	public void parseXMLFile(final String xmlFile) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File inputFile = new File(classLoader.getResource(xmlFile)
					.getFile());

			// StringBuilder result = new StringBuilder("");
			// try (Scanner scanner = new Scanner(inputFile)) {
			//
			//
			// String line = scanner.nextLine();
			// result.append(line).append("\n");
			//
			//
			// scanner.close();
			//
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			//

			SAXReader reader = new SAXReader();
			Document document = reader.read(inputFile);
			BasicAccountDetail account = new BasicAccountDetail();
			if(readAccountDetails(document,account)){
				//set the account and get account id for userDetails
				account.setCreateDate(new Date());
			}
			
			LocationDetail location = new LocationDetail();
			if (readLocationDetail(document, location)) {
				// call location dao here and getLocationId for userDetail
			}
			UserDetail userDetail = new UserDetail();

			readBasicInformation(document, userDetail);
			log.info("location = "+location.getCityName());
			log.info("account = "+account.getEmailId());
			log.info("user = "+userDetail.getOtherContactNumbers());

		} catch (DocumentException e) {
			e.printStackTrace();

		}
	}

	@SuppressWarnings("unchecked")
	public boolean readAccountDetails(Document document,
			BasicAccountDetail account) {

		List<Node> contactMethodsList = document
				.selectNodes(SovrenResumeNodePaths.ContactMethod.getPath());
		for (Node node : contactMethodsList) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Node use = node.selectSingleNode("Use");
				Node emailAdd = node.selectSingleNode("InternetEmailAddress");
				if (use != null && emailAdd != null) {
					if (use.getText().equals("personal")) {
						account.setEmailId(emailAdd.getText());
						return true;
					}

				}
			}

		}
		return false;
	}

	
	@SuppressWarnings("unchecked")
	public boolean readLocationDetail(Document document, LocationDetail location) {
		List<Node> contactMethodsList = document
				.selectNodes(SovrenResumeNodePaths.ContactMethod.getPath());
		for (Node node : contactMethodsList) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Node address = node.selectSingleNode("PostalAddress");
				if (address != null) {
					String countryCode = address
							.selectSingleNode("CountryCode").getText();
					String city = address.selectSingleNode("Region").getText();
					location.setCityName(city);
					location.setCountryCodeEnum(countryCode); // change it to
																// CountryCodeEnum.valueOf
					return true;
				}

			}

		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void readBasicInformation(Document document, UserDetail userDetail) {
		log.info("trying to read document  "
				+ document.getRootElement().getName());
		List<Node> contactMethodsList = document
				.selectNodes(SovrenResumeNodePaths.ContactMethod.getPath());
		StringBuilder contactNumberBuilder = new StringBuilder();

		for (Node node : contactMethodsList) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String telephone = getTelephoneFromNode(node);
				String mobileNumber = getMobileNumber(node);
				if (telephone != null) {
					contactNumberBuilder.append(telephone).append(",");
				}
				if (mobileNumber != null)
					userDetail.setMobileNumber(mobileNumber);
				if (userDetail.getFirstName() == null) {
					Node personName = node.selectSingleNode("PersonName");
					if (personName != null) {
						Node firstName = personName
								.selectSingleNode("GivenName");
						Node lastName = personName
								.selectSingleNode("FamilyName");
						if (firstName != null)
							userDetail.setFirstName(firstName.getText());
						if (lastName != null)
							userDetail.setLastName(lastName.getText());
					}
				}

				log.info("zzz contactInfo = " + telephone + mobileNumber);
			} else {
				log.info("wrong node type");
			}
			userDetail.setOtherContactNumbers(contactNumberBuilder.toString());

		}

	}

	private String getTelephoneFromNode(Node node) {
		Node telNode = node.selectSingleNode("Telephone");
		if (telNode != null) {
			return telNode.selectSingleNode("FormattedNumber").getText();
		}
		return null;
	}

	private String getMobileNumber(Node node) {
		Node telNode = node.selectSingleNode("Mobile");
		if (telNode != null) {
			return telNode.selectSingleNode("FormattedNumber").getText();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserExperinceDetail> readExperienceDetails(Document document){
		List<UserExperinceDetail> experienceList = new ArrayList<UserExperinceDetail>();
		
		List<Node> contactMethodsList = document
				.selectNodes(SovrenResumeNodePaths.EmploymentOrg.getPath());
		for (Node node : contactMethodsList) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Node address = node.selectSingleNode("PostalAddress");
			}
		}
		return experienceList;
	}*/

}
