package com.axonactive.employeecore.arquillian.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.axonactive.employeecore.department.DepartmentDTO;
import com.axonactive.employeecore.department.DepartmentService;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;

@RunWith(Arquillian.class)
@CleanupUsingScript("scripts/cleanup.sql") 
@Cleanup(phase = TestExecutionPhase.BEFORE)
@UsingDataSet("departments.yml")
public class DepartmentServiceArquillianTest {
	
	
	@EJB
	DepartmentService departmentService;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Deployment
    public static WebArchive createDeployment() {
		// Import Maven runtime dependencies
		
        File[] files = Maven.resolver()
                            .loadPomFromFile("pom.xml")
                            .importCompileAndRuntimeDependencies()
                            .resolve()
                            .withTransitivity()
                            .asFile(); 
		WebArchive archive = ShrinkWrap.create(WebArchive.class) ;
        archive	.addPackages(true, Filters.exclude("com.axonactive.employeecore.restconfiguration*"), "com.axonactive.employeecore")
        	.addAsLibraries(files)
        	.addPackages(true, "org.apache.log4j")
        	.addAsResource("test-persistence.xml", "META-INF/persistence.xml") 
        	.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        	
        //System.out.println(archive.toString(true));
        return archive;
    }
	@Test 
	public void getAllDepartmentTest() {
		List<DepartmentDTO> expectedList = new ArrayList<DepartmentDTO>();
		expectedList.add(new DepartmentDTO(1, "Zürich"));
		expectedList.add(new DepartmentDTO(2, "Geneve"));
		expectedList.add(new DepartmentDTO(3, "Uri"));
		List<DepartmentDTO> actualDeptList = departmentService.getAllDepartmentList();
		System.out.println("getAllDepartmentTest() actualDeptList "+actualDeptList.size());
		Assertions.assertThat(actualDeptList)
        .usingElementComparatorOnFields("name")
        .containsExactlyInAnyOrderElementsOf(expectedList);
		//assertEquals(expectedList, actualDeptList);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test 
	@ShouldMatchDataSet(value = "datasets/expected-departments-after-add.yml", excludeColumns = "departmentId")
	public void addDepartmentTest_validData() {
		
		DepartmentDTO newDept = new DepartmentDTO();
		newDept.setName("Bern");
		int newId = departmentService.addDepartment(newDept);
		System.out.println("new Id "+newId);
		
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test 
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void addDepartmentTest_hasID_IDNotExisted() {
		thrown.expect(ValidationException.class);
		DepartmentDTO newDept = new DepartmentDTO(10,"Bern");
		
		int newId = departmentService.addDepartment(newDept);
		System.out.println("new Id "+newId);
	}
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void addDepartmentTest_hasID_IDExisted() {
		
		DepartmentDTO newDept = new DepartmentDTO(1,"Bern");
		
		int newId = departmentService.addDepartment(newDept);
		System.out.println("new Id "+newId);
	}	
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void addDepartmentTest_emptyName() {
		
		DepartmentDTO newDept = new DepartmentDTO();
		
		int newId = departmentService.addDepartment(newDept);
		System.out.println("new Id "+newId);
		
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void addDepartmentTest_duplicateName() {
		
		DepartmentDTO newDept = new DepartmentDTO();
		newDept.setName("Geneve");
		int newId = departmentService.addDepartment(newDept);
		System.out.println("new Id "+newId);
		
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void addDepartmentTest_longName() {
		
		DepartmentDTO newDept = new DepartmentDTO();
		newDept.setName("This is a really looooooooooooooong department name");
		int newId = departmentService.addDepartment(newDept);
		System.out.println("new Id "+newId);
		
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test 
	@ShouldMatchDataSet(value = "datasets/expected-departments-after-delete.yml", excludeColumns = "departmentId")
	public void deleteDepartmentTest_validID() {
		
		departmentService.deleteDepartment(2);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = EntityNotFoundException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void deleteDepartmentTest_invalidID() {
		
		departmentService.deleteDepartment(4);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test 
	@ShouldMatchDataSet(value = "datasets/expected-departments-after-update.yml", excludeColumns = "departmentId")
	public void updateDepartmentTest_validData() {
		DepartmentDTO newDept = new DepartmentDTO(3,"Marketing & Design");
		departmentService.updateDepartment(newDept);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = EntityNotFoundException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void updateDepartmentTest_notExistedID() {
		DepartmentDTO newDept = new DepartmentDTO(10,"Vallais");
		
		
		departmentService.updateDepartment(newDept);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = EntityNotFoundException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void updateDepartmentTest_emptyID() {
		DepartmentDTO newDept = new DepartmentDTO();
		newDept.setName("Vallais");
		
		departmentService.updateDepartment(newDept);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void updateDepartmentTest_duplicateName() {
		DepartmentDTO newDept = new DepartmentDTO(3,"Geneve");
		
		
		departmentService.updateDepartment(newDept);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void updateDepartmentTest_emptyName() {
		DepartmentDTO newDept = new DepartmentDTO(3,"");
		
		
		departmentService.updateDepartment(newDept);
		// state is verified using @ShouldMatchDataSet feature
	}
	@Test  (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/departments.yml", excludeColumns = "departmentId")
	public void updateDepartmentTest_longName() {
		DepartmentDTO newDept = new DepartmentDTO(3,"This is a really looooooooooooooong department name");
		
		
		departmentService.updateDepartment(newDept);
		// state is verified using @ShouldMatchDataSet feature
	}
}
