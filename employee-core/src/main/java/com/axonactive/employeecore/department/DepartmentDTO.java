package com.axonactive.employeecore.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class DepartmentDTO {
	private Integer departmentId;
	private String name;
}
