package com.axonactive.employeecore.certificate;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.axonactive.employeecore.employee.EmployeeService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.presistence.GenericService;

@Stateless
public class CertificateService extends GenericService<CertificateEntity> {

	private static final String CERTIFICATE_NOT_FOUND = "Certificate is not found ! Please check the id";

	private static final String CERTIFICATE_DUPLICATE_MESSAGE = "This certificate is duplicated !! Please check Name and Achieved time of certificate";

	@EJB
	private EmployeeService employeeService;

	private CertificateEntity findCertificateEntityById(Integer certificateID) {
		CertificateEntity certificateEntity = em.find(CertificateEntity.class, certificateID);
		checkEntityNotFound(certificateEntity);
		return certificateEntity;
	}

	public CertificateDTO findACertificateOfEmployee(Integer employeeId, Integer certificateId) {
		employeeService.findEmployeeEntityById(employeeId);
		CertificateEntity certificateEntity = em
				.createNamedQuery(CertificateEntity.FIND_CERTIFICATE_OF_EMPLOYEE, CertificateEntity.class)
				.setParameter("employeeId", employeeId).setParameter("certificateId", certificateId).getResultList()
				.stream().findFirst().orElse(null);
		checkEntityNotFound(certificateEntity);
		return CertificateConverter.getInstance().toDTO(certificateEntity);
	}

	public int addCertificate(Integer employeeId, CertificateDTO certificateDTO) {
		checkCertificateDuplication(certificateDTO, employeeId);
		CertificateEntity newCertificateEntity = CertificateConverter.getInstance().toEntity(certificateDTO);
		setCertificateAttribute(newCertificateEntity, certificateDTO, employeeId);
		this.save(newCertificateEntity);
		return newCertificateEntity.getCertificateId();
	}

	public void updateCertificate(CertificateDTO updateCertificate, Integer employeeId) {
		CertificateEntity currentCertificateEntity = findCertificateEntityById(updateCertificate.getCertificateId());
		checkCertificateDuplication(updateCertificate, employeeId);
		setCertificateAttribute(currentCertificateEntity, updateCertificate, employeeId);
		this.update(currentCertificateEntity);
	}

	private void checkCertificateDuplication(CertificateDTO certificateDTO, Integer employeeId) {
		List<CertificateDTO> listCertificate = findAllCertificateByEmployeeId(employeeId);
		for (CertificateDTO certificates : listCertificate) {
			if (certificateDTO.getNameOfCertificate().equalsIgnoreCase(certificates.getNameOfCertificate())
					&& certificateDTO.getAchievedTime().equalsIgnoreCase(certificates.getAchievedTime())) {
				throw new ValidationException(CERTIFICATE_DUPLICATE_MESSAGE);
			}
		}
	}

	public List<CertificateDTO> findAllCertificateByEmployeeId(Integer employeeId) {
		employeeService.findEmployeeEntityById(employeeId);
		TypedQuery<CertificateEntity> typedQuery = em
				.createNamedQuery(CertificateEntity.FIND_ALL_CERITFICATE_WITH_EMPLOYEE_ID, CertificateEntity.class)
				.setParameter("employeeId", employeeId);
		List<CertificateEntity> listCertificateEntity = typedQuery.getResultList();
		return CertificateConverter.getInstance().toListDTO(listCertificateEntity);
	}

	private void setCertificateAttribute(CertificateEntity certificateEntity, CertificateDTO certificateDTO,
			Integer employeeId) {
		certificateEntity.setCertificateId(certificateDTO.getCertificateId());
		certificateEntity.setNameOfCertificate(certificateDTO.getNameOfCertificate());
		certificateEntity.setTypeOfCertificate(certificateDTO.getTypeOfCertificate());
		certificateEntity.setAchievedTime(certificateDTO.getAchievedTime());
		certificateEntity.setEmployeeEntity(employeeService.findEmployeeEntityById(employeeId));
	}

	public void deleteCertificate(Integer employeeId, Integer certificateID) {
		this.remove(CertificateConverter.getInstance().toEntity(findACertificateOfEmployee(employeeId, certificateID)));
	}

	private void checkEntityNotFound(CertificateEntity certificateEntity) {
		if (certificateEntity == null) {
			throw new EntityNotFoundException(CERTIFICATE_NOT_FOUND);
		}
	}
}
