
package com.axonactive.employeecore.employee;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;

public class EmployeeConverter {

	public EmployeeEntity toEntity(BasicEmployeeDTO employeeDTO) {
		if (employeeDTO != null) {

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			mapper.createTypeMap(BasicEmployeeDTO.class, EmployeeEntity.class)
					.addMappings(new PropertyMap<BasicEmployeeDTO, EmployeeEntity>() {

						@Override
						protected void configure() {
							skip(destination.getCertificateEntities());
							skip(destination.getContactEntities());
						}

					});
			mapper.validate();

			return mapper.map(employeeDTO, EmployeeEntity.class);
		}
		return null;
	}

	public BasicEmployeeDTO toDTO(EmployeeEntity employeeEntity) {
		if (employeeEntity != null) {
			return new ModelMapper().map(employeeEntity, BasicEmployeeDTO.class);
		}
		return null;
	}

	public List<BasicEmployeeDTO> toDTOs(List<EmployeeEntity> employeeEntities) {
		if (employeeEntities != null) {
			Type listType = new TypeToken<List<BasicEmployeeDTO>>() {
			}.getType();
			return new ModelMapper().map(employeeEntities, listType);
		}
		return Collections.emptyList();
	}
}