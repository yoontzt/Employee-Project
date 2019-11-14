/**
 * 
 */
package com.axonactive.common.security.authentication;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**

 * Contains the authentication information of a member
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Entity
@Table(name="userInfo")
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name=User.CHECK,query= "select e from User e where e.username =:username and e.password=:password")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String PREFIX = "com.axonactive.commin.security.authentication.User.";
	
	public static final String CHECK = PREFIX + "findall";
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/**
	 * It's mandatory and unique in the system
	 */
	@NotNull
	private String username;
	
	/**
	 * It's mandatory and has at least 8 characters
	 */
	@NotNull
	private String password;
	
	/**
	 * Indicate whether the user is blocked or not. if its value is true, the user wont be able to log into the system
	 */
	
	private String displayName;
	
	private boolean blocked;
	
	
	
	/**
	 * roles of the user
	 */
}
