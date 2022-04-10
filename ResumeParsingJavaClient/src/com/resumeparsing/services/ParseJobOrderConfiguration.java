
package com.resumeparsing.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParseJobOrderConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParseJobOrderConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CountryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="KnownType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncludeRecruitingTerms" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IncludeSupplementalText" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="PreferShorterJobTitles" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParseJobOrderConfiguration", propOrder = {
    "countryCode",
    "language",
    "knownType",
    "includeRecruitingTerms",
    "includeSupplementalText",
    "preferShorterJobTitles"
})
public class ParseJobOrderConfiguration {

    @XmlElement(name = "CountryCode")
    protected String countryCode;
    @XmlElement(name = "Language")
    protected String language;
    @XmlElement(name = "KnownType")
    protected String knownType;
    @XmlElement(name = "IncludeRecruitingTerms")
    protected boolean includeRecruitingTerms;
    @XmlElement(name = "IncludeSupplementalText")
    protected boolean includeSupplementalText;
    @XmlElement(name = "PreferShorterJobTitles")
    protected boolean preferShorterJobTitles;

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the knownType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKnownType() {
        return knownType;
    }

    /**
     * Sets the value of the knownType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKnownType(String value) {
        this.knownType = value;
    }

    /**
     * Gets the value of the includeRecruitingTerms property.
     * 
     */
    public boolean isIncludeRecruitingTerms() {
        return includeRecruitingTerms;
    }

    /**
     * Sets the value of the includeRecruitingTerms property.
     * 
     */
    public void setIncludeRecruitingTerms(boolean value) {
        this.includeRecruitingTerms = value;
    }

    /**
     * Gets the value of the includeSupplementalText property.
     * 
     */
    public boolean isIncludeSupplementalText() {
        return includeSupplementalText;
    }

    /**
     * Sets the value of the includeSupplementalText property.
     * 
     */
    public void setIncludeSupplementalText(boolean value) {
        this.includeSupplementalText = value;
    }

    /**
     * Gets the value of the preferShorterJobTitles property.
     * 
     */
    public boolean isPreferShorterJobTitles() {
        return preferShorterJobTitles;
    }

    /**
     * Sets the value of the preferShorterJobTitles property.
     * 
     */
    public void setPreferShorterJobTitles(boolean value) {
        this.preferShorterJobTitles = value;
    }

}
