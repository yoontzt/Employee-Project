package com.axonactive.common.security.authentication;
/**
 * 
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the name of the permission. it's mandatory and unique
	 */
	private String name;

	/**
	 * the  name that will be displayed on the GUI of the permission. it's optional.
	 * If it's null, the name will be used to display on the GUI
	 */
	private String displayName;
	
	public Permission() {
		// This is default constructor
	}
	

	/**
	 * @param name
	 * @param displayName
	 */
	public Permission(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Permission [name=" + name + ", displayName=" + displayName + "]";
	}

	

}

