package com.axonactive.employeecore.employee;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.axonactive.employeecore.additionalcontact.ContactEntity;
import com.axonactive.employeecore.address.AddressEntity;
import com.axonactive.employeecore.certificate.CertificateEntity;
import com.axonactive.employeecore.department.DepartmentEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee", uniqueConstraints = { @UniqueConstraint(columnNames = { "skype" }) })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = EmployeeEntity.FIND_ALL, query = "select e from EmployeeEntity e order by e.firstname ASC, e.lastname DESC")
@NamedQuery(name = EmployeeEntity.FIND_EMAIL, query = "select e from EmployeeEntity e where e.primaryEmail = :primaryEmail")
@NamedQuery(name = EmployeeEntity.FIND_EMAIL_FOR_UPDATE, query = "select e from EmployeeEntity e where e.primaryEmail = :primaryEmail and e.id !=:employeeId")
@NamedQuery(name = EmployeeEntity.FIND_PHONE, query = "select e from EmployeeEntity e where e.mobilePhone = :mobilePhone")
@NamedQuery(name = EmployeeEntity.FIND_SKYPE, query = "select e from EmployeeEntity e where e.skype = :skype")
@NamedQuery(name = EmployeeEntity.SEARCH, query = "select e from EmployeeEntity e where unaccent(lower(e.firstname)) like lower(:input) or unaccent(lower(e.lastname)) like lower(:input) or unaccent(lower(e.primaryEmail)) like lower(:input) order by e.firstname ASC, e.lastname DESC")
public class EmployeeEntity {
	private static final String PREFIX = "com.axonactive.employeecore.entity.EmployeeEntity.";

	public static final String FIND_ALL = PREFIX + "findall";

	public static final String FIND_EMAIL = PREFIX + "findEmail";

	public static final String FIND_PHONE = PREFIX + "findPhone";

	public static final String FIND_SKYPE = PREFIX + "findSkype";

	public static final String FIND_EMAIL_FOR_UPDATE = PREFIX + "find";

	public static final String SEARCH = PREFIX + "search";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer employeeId;

	@Column(name = "lastName")
	private String lastname;

	@Column(name = "firstName")
	private String firstname;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateOfBirth")
	private Date dateOfBirth;

	@Column(name = "nationality")
	private String nationality;

	@Column(name = "primaryEmail")
	private String primaryEmail;

	@Column(name = "mobilePhone")
	private String mobilePhone;

	@Column(name = "skype")
	private String skype;

	@Column(name = "emergencyContact")
	private String emergencyContact;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
	@JoinColumn(name = "address")
	private AddressEntity address;

	@OneToMany(mappedBy = "employeeEntity", orphanRemoval = true)
	private List<CertificateEntity> certificateEntities;

	@OneToMany(mappedBy = "employeeEntity", orphanRemoval = true)
	private List<ContactEntity> contactEntities;

	@ManyToOne
	@JoinColumn(name = "department", nullable = true)
	private DepartmentEntity department;
}
