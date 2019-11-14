package com.axonactive.common.security.authentication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
@ToString
@AllArgsConstructor
public class Token {
	private String tokenValue;
	private Integer minutesToLive;
	private String tokenType;
	private String refreshToken;
	private String displayname;
}