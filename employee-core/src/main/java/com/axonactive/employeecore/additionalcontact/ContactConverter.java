package com.axonactive.employeecore.additionalcontact;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;

public class ContactConverter {

	private static ContactConverter contactConverter;

	private ContactConverter() {
	}

	public static ContactConverter getInstance() {
		if (null == contactConverter) {
			contactConverter = new ContactConverter();
		}
		return contactConverter;
	}

	private ContactTypeConverter typeConverter = new ContactTypeConverter();

	public ContactEntity toEntity(ContactDTO contactDTO) {
		if (contactDTO != null) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			mapper.createTypeMap(ContactDTO.class, ContactEntity.class)
					.addMappings(new PropertyMap<ContactDTO, ContactEntity>() {

						@Override
						protected void configure() {
							using(converter -> typeConverter
									.convertToEntityAttribute(((ContactDTO) converter.getSource()).getType()))
											.map(source, destination.getType());
							skip(destination.getEmployeeEntity());
						}
					});
			mapper.validate();
			return mapper.map(contactDTO, ContactEntity.class);

		}
		return null;
	}

	public ContactDTO toDTO(ContactEntity contactEntity) {
		if (contactEntity != null) {

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			mapper.createTypeMap(ContactEntity.class, ContactDTO.class)
					.addMappings(new PropertyMap<ContactEntity, ContactDTO>() {

						@Override
						protected void configure() {
							using(converter -> typeConverter
									.convertToDatabaseColumn(((ContactEntity) converter.getSource()).getType()))
											.map(source, destination.getType());
						}
					});
			mapper.validate();

			return mapper.map(contactEntity, ContactDTO.class);

		}
		return null;
	}

	public List<ContactEntity> toListEntity(List<ContactDTO> contactDTOs) {
		if (contactDTOs != null) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			mapper.createTypeMap(ContactDTO.class, ContactEntity.class)
					.addMappings(new PropertyMap<ContactDTO, ContactEntity>() {

						@Override
						protected void configure() {
							using(converter -> typeConverter
									.convertToEntityAttribute(((ContactDTO) converter.getSource()).getType()))
											.map(source, destination.getType());
							skip(destination.getEmployeeEntity());
						}
					});
			mapper.validate();
			Type listType = new TypeToken<List<ContactEntity>>() {
			}.getType();
			return mapper.map(contactDTOs, listType);
		}
		return Collections.emptyList();
	}

	public List<ContactDTO> toListDTO(List<ContactEntity> contactEntities) {
		if (contactEntities != null) {

			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			mapper.createTypeMap(ContactEntity.class, ContactDTO.class)
					.addMappings(new PropertyMap<ContactEntity, ContactDTO>() {

						@Override
						protected void configure() {
							using(converter -> typeConverter
									.convertToDatabaseColumn(((ContactEntity) converter.getSource()).getType()))
											.map(source, destination.getType());
						}
					});

			mapper.validate();

			Type listType = new TypeToken<List<ContactDTO>>() {
			}.getType();

			return mapper.map(contactEntities, listType);
		}
		return Collections.emptyList();
	}
}