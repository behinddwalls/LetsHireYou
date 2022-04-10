package com.portal.job.resumeparsing.xmlparsing.parser;

import javax.xml.bind.JAXBException;

import com.portal.job.resumeparsing.xmlparsing.model.Resume;

public interface IXMLToJava {
	
	public Resume parse(String fileName) throws JAXBException;

}
