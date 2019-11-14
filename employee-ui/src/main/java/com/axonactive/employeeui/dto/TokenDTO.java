package com.axonactive.employeeui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
	private String tokenValue;
	private Integer minutesToLive;
	private String tokenType;
	private String refreshToken;
	private String displayname;
}
