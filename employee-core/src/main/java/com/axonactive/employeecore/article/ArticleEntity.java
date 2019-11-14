package com.axonactive.employeecore.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.axonactive.employeecore.employee.EmployeeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="article")
@ToString(exclude = "employeeEntity")
public class ArticleEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer articleId;
	
	@Column(name="fileName",nullable = false)
	private String fileName;
	
	@ManyToOne
	@JoinColumn(name = "employeeId")
	private EmployeeEntity employeeEntity;

}
