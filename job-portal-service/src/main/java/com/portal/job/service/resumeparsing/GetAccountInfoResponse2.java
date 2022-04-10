
package com.portal.job.service.resumeparsing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAccountInfoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAccountInfoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SubCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreditsUsed" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CreditsRemaining" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAccountInfoResponse2", propOrder = {
    "code",
    "subCode",
    "message",
    "creditsUsed",
    "creditsRemaining",
    "expirationDate"
})
public class GetAccountInfoResponse2 {

    @XmlElement(name = "Code")
    protected String code;
    @XmlElement(name = "SubCode")
    protected String subCode;
    @XmlElement(name = "Message")
    protected String message;
    @XmlElement(name = "CreditsUsed")
    protected int creditsUsed;
    @XmlElement(name = "CreditsRemaining")
    protected int creditsRemaining;
    @XmlElement(name = "ExpirationDate")
    protected String expirationDate;

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
     * Gets the value of the creditsUsed property.
     * 
     */
    public int getCreditsUsed() {
        return creditsUsed;
    }

    /**
     * Sets the value of the creditsUsed property.
     * 
     */
    public void setCreditsUsed(int value) {
        this.creditsUsed = value;
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
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

}
