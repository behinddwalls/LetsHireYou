
package com.resumeparsing.services;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseResumeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParseResumeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SubCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FileType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TextCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Xml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XmlDoc" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Html" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HtmlCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Rtf" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RtfCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WordXml" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WordXmlCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreditsRemaining" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ParserVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FileExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParseResumeResponse", propOrder = {
    "code",
    "subCode",
    "message",
    "fileType",
    "text",
    "textCode",
    "xml",
    "xmlDoc",
    "html",
    "htmlCode",
    "rtf",
    "rtfCode",
    "wordXml",
    "wordXmlCode",
    "creditsRemaining",
    "parserVersion",
    "fileExtension"
})
public class ParseResumeResponse2 {

    @XmlElement(name = "Code")
    protected String code;
    @XmlElement(name = "SubCode")
    protected String subCode;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "FileType")
    protected String fileType;
    @XmlElement(name = "Text")
    protected String text;
    @XmlElement(name = "TextCode")
    protected String textCode;
    @XmlElement(name = "Xml")
    protected String xml;
    @XmlElement(name = "XmlDoc")
    protected ParseResumeResponse2 .XmlDoc xmlDoc;
    @XmlElement(name = "Html")
    protected String html;
    @XmlElement(name = "HtmlCode")
    protected String htmlCode;
    @XmlElement(name = "Rtf")
    protected String rtf;
    @XmlElement(name = "RtfCode")
    protected String rtfCode;
    @XmlElement(name = "WordXml")
    protected String wordXml;
    @XmlElement(name = "WordXmlCode")
    protected String wordXmlCode;
    @XmlElement(name = "CreditsRemaining")
    protected int creditsRemaining;
    @XmlElement(name = "ParserVersion")
    protected String parserVersion;
    @XmlElement(name = "FileExtension")
    protected String fileExtension;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the subCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubCode() {
        return subCode;
    }

    /**
     * Sets the value of the subCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubCode(String value) {
        this.subCode = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the fileType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Sets the value of the fileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileType(String value) {
        this.fileType = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the textCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextCode() {
        return textCode;
    }

    /**
     * Sets the value of the textCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextCode(String value) {
        this.textCode = value;
    }

    /**
     * Gets the value of the xml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXml() {
        return xml;
    }

    /**
     * Sets the value of the xml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXml(String value) {
        this.xml = value;
    }

    /**
     * Gets the value of the xmlDoc property.
     * 
     * @return
     *     possible object is
     *     {@link ParseResumeResponse2 .XmlDoc }
     *     
     */
    public ParseResumeResponse2 .XmlDoc getXmlDoc() {
        return xmlDoc;
    }

    /**
     * Sets the value of the xmlDoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParseResumeResponse2 .XmlDoc }
     *     
     */
    public void setXmlDoc(ParseResumeResponse2 .XmlDoc value) {
        this.xmlDoc = value;
    }

    /**
     * Gets the value of the html property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHtml() {
        return html;
    }

    /**
     * Sets the value of the html property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHtml(String value) {
        this.html = value;
    }

    /**
     * Gets the value of the htmlCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHtmlCode() {
        return htmlCode;
    }

    /**
     * Sets the value of the htmlCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHtmlCode(String value) {
        this.htmlCode = value;
    }

    /**
     * Gets the value of the rtf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRtf() {
        return rtf;
    }

    /**
     * Sets the value of the rtf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRtf(String value) {
        this.rtf = value;
    }

    /**
     * Gets the value of the rtfCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRtfCode() {
        return rtfCode;
    }

    /**
     * Sets the value of the rtfCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRtfCode(String value) {
        this.rtfCode = value;
    }

    /**
     * Gets the value of the wordXml property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWordXml() {
        return wordXml;
    }

    /**
     * Sets the value of the wordXml property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWordXml(String value) {
        this.wordXml = value;
    }

    /**
     * Gets the value of the wordXmlCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWordXmlCode() {
        return wordXmlCode;
    }

    /**
     * Sets the value of the wordXmlCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWordXmlCode(String value) {
        this.wordXmlCode = value;
    }

    /**
     * Gets the value of the creditsRemaining property.
     * 
     */
    public int getCreditsRemaining() {
        return creditsRemaining;
    }

    /**
     * Sets the value of the creditsRemaining property.
     * 
     */
    public void setCreditsRemaining(int value) {
        this.creditsRemaining = value;
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

    /**
     * Gets the value of the fileExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * Sets the value of the fileExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileExtension(String value) {
        this.fileExtension = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;any/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "content"
    })
    public static class XmlDoc {

        @XmlMixed
        @XmlAnyElement(lax = true)
        protected List<Object> content;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * {@link Object }
         * 
         * 
         */
        public List<Object> getContent() {
            if (content == null) {
                content = new ArrayList<Object>();
            }
            return this.content;
        }

    }

}
