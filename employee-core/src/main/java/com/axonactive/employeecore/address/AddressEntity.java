package com.axonactive.employeecore.address;

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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address")
@NamedQuery(name = AddressEntity.FIND_BY_ID, query = "SELECT c FROM AddressEntity c where c.addressId = :id")
@NamedQuery(name = AddressEntity.FIND_ALL, query = "select c from AddressEntity c order by c.id ASC")
@NamedQuery(name = AddressEntity.FIND_ALL_ID, query = "select c from AddressEntity c")
public class AddressEntity {

	private static final String PREFIX = "com.axonactive.employeecore.entity.AddressEntity.";

	public static final String FIND_BY_ID = PREFIX + "find by Id";

	public static final String FIND_ALL = PREFIX + "find all";
	public static final String FIND_ALL_ID = PREFIX + "find all id";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int addressId;

	@Column(name = "address")
	private String address;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;

}
