
package com.portal.job.service.resumeparsing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseJobOrderRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParseJobOrderRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FileBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="FileText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Configuration" type="{http://services.resumeparsing.com/}ParseJobOrderConfiguration" minOccurs="0"/>
 *         &lt;element name="OutputXmlDoc" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OutputHtml" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OutputRtf" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OutputWordXml" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RevisionDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParserVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParseJobOrderRequest", propOrder = {
    "accountId",
    "serviceKey",
    "fileBytes",
    "fileText",
    "configuration",
    "outputXmlDoc",
    "outputHtml",
    "outputRtf",
    "outputWordXml",
    "revisionDate",
    "parserVersion"
})
public class ParseJobOrderRequest {

    @XmlElement(name = "AccountId")
    protected String accountId;
    @XmlElement(name = "ServiceKey")
    protected String serviceKey;
    @XmlElement(name = "FileBytes")
    protected byte[] fileBytes;
    @XmlElement(name = "FileText")
    protected String fileText;
    @XmlElement(name = "Configuration")
    protected ParseJobOrderConfiguration configuration;
    @XmlElement(name = "OutputXmlDoc", required = true, type = Boolean.class, nillable = true)
    protected Boolean outputXmlDoc;
    @XmlElement(name = "OutputHtml", required = true, type = Boolean.class, nillable = true)
    protected Boolean outputHtml;
    @XmlElement(name = "OutputRtf", required = true, type = Boolean.class, nillable = true)
    protected Boolean outputRtf;
    @XmlElement(name = "OutputWordXml", required = true, type = Boolean.class, nillable = true)
    protected Boolean outputWordXml;
    @XmlElement(name = "RevisionDate")
    protected String revisionDate;
    @XmlElement(name = "ParserVersion")
    protected String parserVersion;

    /**
     * Gets the value of the accountId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the value of the accountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountId(String value) {
        this.accountId = value;
    }

    /**
     * Gets the value of the serviceKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceKey() {
        return serviceKey;
    }

    /**
     * Sets the value of the serviceKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceKey(String value) {
        this.serviceKey = value;
    }

    /**
     * Gets the value of the fileBytes property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFileBytes() {
        return fileBytes;
    }

    /**
     * Sets the value of the fileBytes property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFileBytes(byte[] value) {
        this.fileBytes = value;
    }

    /**
     * Gets the value of the fileText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileText() {
        return fileText;
    }

    /**
     * Sets the value of the fileText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileText(String value) {
        this.fileText = value;
    }

    /**
     * Gets the value of the configuration property.
     * 
     * @return
     *     possible object is
     *     {@link ParseJobOrderConfiguration }
     *     
     */
    public ParseJobOrderConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Sets the value of the configuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParseJobOrderConfiguration }
     *     
     */
    public void setConfiguration(ParseJobOrderConfiguration value) {
        this.configuration = value;
    }

    /**
     * Gets the value of the outputXmlDoc property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOutputXmlDoc() {
        return outputXmlDoc;
    }

    /**
     * Sets the value of the outputXmlDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutputXmlDoc(Boolean value) {
        this.outputXmlDoc = value;
    }

    /**
     * Gets the value of the outputHtml property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOutputHtml() {
        return outputHtml;
    }

    /**
     * Sets the value of the outputHtml property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutputHtml(Boolean value) {
        this.outputHtml = value;
    }

    /**
     * Gets the value of the outputRtf property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOutputRtf() {
        return outputRtf;
    }

    /**
     * Sets the value of the outputRtf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutputRtf(Boolean value) {
        this.outputRtf = value;
    }

    /**
     * Gets the value of the outputWordXml property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOutputWordXml() {
        return outputWordXml;
    }

    /**
     * Sets the value of the outputWordXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutputWordXml(Boolean value) {
        this.outputWordXml = value;
    }

    /**
     * Gets the value of the revisionDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRevisionDate() {
        return revisionDate;
    }

    /**
     * Sets the value of the revisionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRevisionDate(String value) {
        this.revisionDate = value;
    }

    /**
     * Gets the value of the parserVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParserVersion() {
        return parserVersion;
    }

    /**
     * Sets the value of the parserVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParserVersion(String value) {
        this.parserVersion = value;
    }

}
