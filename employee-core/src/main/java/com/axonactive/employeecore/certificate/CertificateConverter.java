package com.axonactive.employeecore.certificate;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

public class CertificateConverter {

	private static CertificateConverter certificateConverter;
	private CertificateConverter() {}
	
	public static CertificateConverter getInstance() {
		if(null == certificateConverter) {
			certificateConverter = new CertificateConverter();
		}
		return certificateConverter;
	}
	public CertificateEntity toEntity(CertificateDTO certificateDTO) {
		if (certificateDTO != null) {
			return new ModelMapper().map(certificateDTO, CertificateEntity.class);
		}
		return null;
	}

	public CertificateDTO toDTO(CertificateEntity certificateEntity) {
		if (certificateEntity != null) {
			return new ModelMapper().map(certificateEntity, CertificateDTO.class);
		}
		return null;
	}

	public  List<CertificateEntity> toListEntity(List<CertificateDTO> certificateDTOs) {
		if (certificateDTOs != null) {
			Type listType = new TypeToken<List<CertificateEntity>>() {}.getType();
			return  new ModelMapper().map(certificateDTOs, listType);
		}
		return Collections.emptyList();
	}

	public  List<CertificateDTO> toListDTO(List<CertificateEntity> certificateEntities) {
		if (certificateEntities != null) {
			Type listType = new TypeToken<List<CertificateDTO>>() {}.getType();
			return  new ModelMapper().map(certificateEntities, listType);
		}
		return Collections.emptyList();
	}
}