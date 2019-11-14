package com.axonactive.employeecore.department;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

public class DepartmentConverter {
	
	private static DepartmentConverter departmentConverter;
    private DepartmentConverter() {}
    
    public static DepartmentConverter getInstance() {
        if(null == departmentConverter) {
            departmentConverter = new DepartmentConverter();
        }
        return departmentConverter;
    }

	public DepartmentEntity toEntity(DepartmentDTO departmentDTO) {
		if (departmentDTO != null) {
			return new ModelMapper().map(departmentDTO, DepartmentEntity.class);
		}
		return null;
	}

	public DepartmentDTO toDTO(DepartmentEntity deparmentEntity) {
		if (deparmentEntity != null) {
			return new ModelMapper().map(deparmentEntity, DepartmentDTO.class);
		}
		return null;
	}

	public List<DepartmentEntity> toEntities(List<DepartmentDTO> departmentDTOs) {
		if (departmentDTOs != null) {
			Type listType = new TypeToken<List<DepartmentEntity>>() {}.getType();
			return new ModelMapper().map(departmentDTOs, listType);
		}
		return Collections.emptyList();
	}

	public List<DepartmentDTO> toDTOs(List<DepartmentEntity> deparmentEntities) {
		if (deparmentEntities != null) {
			Type listType = new TypeToken<List<DepartmentDTO>>() {}.getType();
			return new ModelMapper().map(deparmentEntities, listType);
		}
		return Collections.emptyList();
	}

}