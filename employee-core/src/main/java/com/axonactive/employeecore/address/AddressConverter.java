
package com.axonactive.employeecore.address;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

public class AddressConverter {

	private static AddressConverter addressConverter;

	private AddressConverter() {
	}

	public static AddressConverter getInstance() {
		if (null == addressConverter) {
			addressConverter = new AddressConverter();
		}
		return addressConverter;
	}

	public AddressEntity toEntity(AddressDTO addressDTO) {
		if (addressDTO != null) {
			return new ModelMapper().map(addressDTO, AddressEntity.class);
		}
		return null;
	}

	public AddressDTO toDTO(AddressEntity addressEntity) {
		if (addressEntity != null) {
			return new ModelMapper().map(addressEntity, AddressDTO.class);
		}
		return null;
	}

	public List<AddressEntity> toListEntity(List<AddressDTO> addressDTOs) {
		if (addressDTOs != null) {
			Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
			return 	new ModelMapper().map(addressDTOs, listType);
		}
		return Collections.emptyList();
	}

	public List<AddressDTO> toListDTO(List<AddressEntity> addressEntities) {
		if (addressEntities != null) {
			Type listType = new TypeToken<List<AddressDTO>>() {}.getType();
			return 	new ModelMapper().map(addressEntities, listType);
		}
		return Collections.emptyList();
	}
}