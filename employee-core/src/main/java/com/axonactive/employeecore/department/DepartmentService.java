package com.axonactive.employeecore.department;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;

import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;
import com.axonactive.employeecore.presistence.GenericService;

@Stateless
public class DepartmentService extends GenericService<DepartmentEntity> {

	private static final String DEPARTMENT_NOT_FOUND = "Department is not found! Please check id again.";

	public List<DepartmentDTO> getAllDepartmentList() {
		TypedQuery<DepartmentEntity> query = em.createNamedQuery(DepartmentEntity.FIND_ALL, DepartmentEntity.class);
		List<DepartmentEntity> listDepartmentEntity = query.getResultList();
		return DepartmentConverter.getInstance().toDTOs(listDepartmentEntity);
	}

	public DepartmentDTO findDepartmentDTOById(int deptId) {
		DepartmentDTO departmentDTO = DepartmentConverter.getInstance().toDTO(em.find(DepartmentEntity.class, deptId));
		Optional<DepartmentDTO> departmentOptional = Optional.ofNullable(departmentDTO);
		if (!departmentOptional.isPresent()) {
			throw new EntityNotFoundException(DEPARTMENT_NOT_FOUND);
		}
		return departmentOptional.get();
	}

	public DepartmentEntity findDepartmentEntityById(int deptId) {
		DepartmentEntity department = em.find(DepartmentEntity.class, deptId);
		if (department == null) {
			throw new EntityNotFoundException(DEPARTMENT_NOT_FOUND);
		}
		return department;
	}

	public int addDepartment(DepartmentDTO departmentDto) {
		validateDepartmentDTO(departmentDto);
		DepartmentEntity newDepartmentEntity = DepartmentConverter.getInstance().toEntity(departmentDto);
		this.save(newDepartmentEntity);
		return newDepartmentEntity.getDepartmentId();
	}

	public void updateDepartment(DepartmentDTO departmentDto) {
		DepartmentEntity newDepartmentEntity = DepartmentConverter.getInstance().toEntity(departmentDto);
		this.update(newDepartmentEntity);
	}

	public void deleteDepartment(Integer id) {
		DepartmentEntity departmentEntity = findDepartmentEntityById(id);
		this.remove(departmentEntity);
	}

	private boolean checkNameDuplication(DepartmentDTO department) {
		List<DepartmentEntity> existedEntity = em
				.createNamedQuery(DepartmentEntity.FIND_BY_NAME, DepartmentEntity.class)
				.setParameter("name", department.getName()).getResultList();
		return existedEntity.isEmpty();
	}

	private void validateDepartmentDTO(DepartmentDTO departmentDto) {
		List<String> errorList = new ArrayList<>();
		if (StringUtils.isBlank(departmentDto.getName()))
			errorList.add("Name is missing");

		if (!checkNameDuplication(departmentDto))
			errorList.add("Duplicate Depatment");

		if (!errorList.isEmpty()) {
			throw new ValidationException(StringUtils.join(errorList, ", ") + " .Please check again.");
		}
	}
	

}
