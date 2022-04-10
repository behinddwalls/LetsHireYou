package com.portal.job.resumeparsing.xmlparsing.parser;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import com.portal.job.resumeparsing.xmlparsing.model.Resume;
import com.portal.job.resumeparsing.xmlparsing.model.ResumeXSDObjectFactory;
import com.portal.job.resumeparsing.xmlparsing.model.UserAreaType;

@Component
public class JAXBParser implements IXMLToJava {

	private static Logger log = LoggerFactory.getLogger(JAXBParser.class);

	@SuppressWarnings("unchecked")
	public Resume parse(String xmlFileName) throws JAXBException {
		log.info("zzzzzzz xmlFileName:" + xmlFileName);
		JAXBContext jaxbContext = JAXBContext
				.newInstance(ResumeXSDObjectFactory.class);
		try {

			UnmarshallerHandler unmarshallerHandler = jaxbContext
					.createUnmarshaller().getUnmarshallerHandler();
			// unmarshaller.setProperty(
			// "com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper",
			// new NamespacePrefixMapper() {
			// final String FOO_PREFIX = ""; // DEFAULT NAMESPACE
			// final String FOO_URI = "http://ns.hr-xml.org/2006-02-28";
			// final String SOV_PREFIX = "sov";
			// final String SOV_URI = "http://sovren.com/hr-xml/2006-02-28";
			// @Override
			// public String getPreferredPrefix(String namespaceUri, String
			// suggestion,
			// boolean requestPrefix) {
			// if(FOO_URI.equals(namespaceUri)) {
			// return FOO_PREFIX;
			// } else if(SOV_URI.equals(namespaceUri)) {
			// return SOV_PREFIX;
			// }
			// return suggestion;
			// }
			//
			// @Override
			// public String[] getPreDeclaredNamespaceUris() {
			// return new String[] { FOO_URI, SOV_URI };
			// }
			// });

			SAXParserFactory spf = SAXParserFactory.newInstance();

			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XMLFilter xmlFilter = new MyXMLFilter(xr);
			xmlFilter.setContentHandler(unmarshallerHandler);

			ClassLoader classLoader = getClass().getClassLoader();
			InputStream inputStream = classLoader
					.getResourceAsStream(xmlFileName);

			InputSource inputSource = new InputSource(inputStream);
			log.info("zzzzzzzz input source " + inputSource);

			xmlFilter.parse(inputSource);
			inputStream.close();

			Object obj = unmarshallerHandler.getResult();
			Resume resume = null;
			// File inputFile = new File(classLoader.getResource(xmlFileName)
			// .getFile());
			// Object obj = unmarshaller.unmarshal(inputFile);
			log.info("zzzzzz object = " + obj);
			if (obj instanceof Resume) {
				log.info("yes it is");
				resume = (Resume) obj;
			} else {
				throw new IllegalArgumentException("resume cannot be parsed");
			}

			UserAreaType areaType = resume.getUserArea();
			log.info("zzzzzzz userArea = " + areaType);
			log.info("zzzzzz resumeArea= " + areaType.getAny());
			log.info("zzzzzz resume info = "
					+ resume.getStructuredXMLResume().getContactInfo()
							.getPersonName().getFormattedName());
			return resume;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public SovrenResumeWrapper parseResume(InputStream resumeInputStream)
			throws ResumeParsingException {

		try {
			JAXBContext jaxbContext = JAXBContext
					.newInstance(ResumeXSDObjectFactory.class);
			UnmarshallerHandler unmarshallerHandler = jaxbContext
					.createUnmarshaller().getUnmarshallerHandler();
			SAXParserFactory spf = SAXParserFactory.newInstance();

			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			XMLFilter xmlFilter = new MyXMLFilter(xr);
			xmlFilter.setContentHandler(unmarshallerHandler);

			InputSource inputSource = new InputSource(resumeInputStream);
			xmlFilter.parse(inputSource);
			Object obj = unmarshallerHandler.getResult();
			Resume resume = null;			
			if (obj instanceof Resume) {
				log.info("yes it is");
				resume = (Resume) obj;
			} else {
				throw new IllegalArgumentException("resume cannot be parsed");
			}				
			return new SovrenResumeWrapper(resume);

		} catch (Exception e) {
			throw new ResumeParsingException("unable to parse resume", e);
		}

	}

	/**
	 * solution from :
	 * http://stackoverflow.com/questions/11968399/how-to-make-jaxb
	 * -unmarshaller-to-ignore-prefixes
	 * 
	 * @author abbhasin
	 *
	 */
	private static class MyXMLFilter extends XMLFilterImpl {

		public MyXMLFilter(XMLReader xmlReader) {
			super(xmlReader);
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			final String SOV_PREFIX = "sov";
			log.info("zzzzzz uri=" + uri + "  localName = " + localName
					+ "  qName=" + qName + "  attri =" + attributes);
			int colonIndex = qName.indexOf(':');
			if (qName.contains(SOV_PREFIX) && colonIndex >= 0) {
				qName = qName.substring(colonIndex + 1);
				uri = "http://sovren.com/hr-xml/2006-02-28";
				super.startElement(uri, localName, qName, attributes);
				return;
			}
			uri = "http://ns.hr-xml.org/2006-02-28"; // default uri

			super.startElement(uri, localName, qName, attributes);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			int colonIndex = qName.indexOf(':');
			if (colonIndex >= 0) {
				qName = qName.substring(colonIndex + 1);
			}
			super.endElement(uri, localName, qName);
		}

	}

}
