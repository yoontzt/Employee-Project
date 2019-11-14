package com.axonactive.employeecore.certificate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
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
@NamedQuery(name = CertificateEntity.FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID, query = "select c from CertificateEntity c where c.employeeEntity.employeeId=:employeeId order by c.certificateId ASC")
@NamedQuery(name = CertificateEntity.FIND_CERTIFICATE_OF_EMPLOYEE, query = "select c from CertificateEntity c where c.employeeEntity.employeeId=:employeeId and c.certificateId=:certificateId order by c.certificateId ASC")
@Entity
@Table(name = "certificate")
@ToString(exclude = "employeeEntity")
public class CertificateEntity {

	private static final String PREFIX = "com.axonactive.entities.Employee.";

	public static final String FIND_ALL = PREFIX + "findall";

	public static final String FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID = PREFIX
			+ "find all certificates belong to the employee";

	public static final String FIND_CERTIFICATE_OF_EMPLOYEE = PREFIX + "find a certificate belong to the employee";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer certificateId;

	@Column(name = "name")
	private String nameOfCertificate;

	@Column(name = "type")
	private String typeOfCertificate;

	@Column(name = "achievedTime")
	private String achievedTime;

	@ManyToOne
	@JoinColumn(name = "employeeId", nullable = true)
	private EmployeeEntity employeeEntity;
}
