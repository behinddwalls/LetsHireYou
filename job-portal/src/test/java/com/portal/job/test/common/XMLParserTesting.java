package com.portal.job.test.common;

import java.io.File;

import org.junit.Test;

import com.portal.job.resumeparsing.xmlparsing.parser.JAXBParser;


public class XMLParserTesting {
	

	private static final String xmlRecords = "<data><employee><name>A</name>"
			+ "<title>Manager</title></employee></data>";

	//@Before
	public void setup() {
//		SAXReader reader = new SAXReader();
//		reader.setIncludeExternalDTDDeclarations(false);
//		reader.setIncludeInternalDTDDeclarations(false);
//		reader.setValidation(false);
//		document = reader.read(new StringReader(xmlRecords));

	}

//	@Test
	public void testReadContact() throws Exception {
//		Dom4jXMLParser xmlParser = new Dom4jXMLParser();
//
//		xmlParser.parseXMLFile("xmlFiles/Sample.xml");

		JAXBParser jaxbParser = new JAXBParser();
		jaxbParser.parse("xmlFiles/Original.xml");
	}
	
	@Test
	public void testGetResourcePath(){
		ClassLoader classLoader = getClass().getClassLoader();
		File dir = new File("/Users/abbhasin/HelloHiringWS/letshireyou/job-portal/src/main/resources/xmlFiles");
		int i=1;
		String nameSuffix = ".xml"; 
		for(File f : dir.listFiles()){
			if(f.isFile() && f.getName()!="Original1.xml"){
				System.out.println("file is "+f.getName());
				File newFile = new File(i+nameSuffix);
				f.renameTo(newFile);
				i++;
			}
		}
		System.out.println("isDirectory: "+dir.isDirectory()+" files: "+dir.listFiles());
		
	}

}
