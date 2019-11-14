package com.axonactive.employeecore.additionalcontact;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = ContactEntity.FIND_ALL_CONTACT_BY_EMPLOYEE_ID, query = "select c from ContactEntity c where c.employeeEntity.employeeId=:employeeId order by c.contactId ASC")
@NamedQuery(name = ContactEntity.FIND_CONTACT_OF_EMPLOYEE, query = "select c from ContactEntity c where c.employeeEntity.employeeId=:employeeId and c.contactId=:contactId order by c.contactId ASC")
@Entity
@Table(name = "contact")
public class ContactEntity {

	public static final String PREFIX = "com.axonactive.employeecore.entity.ContactEnity.";

	public static final String FIND_ALL = PREFIX + "find all";
	public static final String FIND_CONTACT_OF_EMPLOYEE = PREFIX + "find a contact belong to the employee";

	public static final String FIND_ALL_CONTACT_BY_EMPLOYEE_ID = PREFIX + "find all contacts belong to the employee";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer contactId;

	@Column(name = "type")
	@Convert(converter = ContactTypeConverter.class)
	private ContactType type;

	@Column(name = "value")
	private String value;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "employeeId", nullable = true)
	private EmployeeEntity employeeEntity;

	public ContactEntity(ContactType type, String value) {
		this.value = value;
		this.type = type;
	}
}
