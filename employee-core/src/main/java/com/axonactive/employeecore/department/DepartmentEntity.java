package com.axonactive.employeecore.department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = DepartmentEntity.FIND_BY_ID, query = "SELECT d FROM DepartmentEntity d where d.departmentId = :deptid")
@NamedQuery(name = DepartmentEntity.FIND_BY_NAME, query = "SELECT d FROM DepartmentEntity d where d.name = :name")
@NamedQuery(name = DepartmentEntity.FIND_ALL, query = "select d from DepartmentEntity d order by d.departmentId ASC")
@Entity
@Table(name = "department")
public class DepartmentEntity {
	private static final String PREFIX = "com.axonactive.employeecore.entity.DepartmentEntity";
	
	public static final String FIND_BY_ID= PREFIX + "find by Id";
	
	public static final String FIND_ALL= PREFIX + "find all";
	
	public static final String FIND_BY_NAME= PREFIX + "find by name";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer departmentId;

	@NonNull
	@Column(name = "name")
	private String name;
}