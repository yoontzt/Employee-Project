package com.axonactive.employeecore.arquillian.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Calendar;
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
import org.junit.Test;
import org.junit.runner.RunWith;

import com.axonactive.employeecore.employee.BasicEmployeeDTO;
import com.axonactive.employeecore.employee.EmployeeService;
import com.axonactive.employeecore.exception.EntityNotFoundException;

@RunWith(Arquillian.class)
@CleanupUsingScript("scripts/cleanup.sql") 
@Cleanup(phase = TestExecutionPhase.BEFORE)
@UsingDataSet("employees.yml")
public class EmployeeServiceArquillianTest {
	@EJB
	EmployeeService employeeService;
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
	public void getAllEmployeeList() {
		List<BasicEmployeeDTO> employeeList = employeeService.getAllEmployeeList();
		assertTrue(employeeList.size() == 3);
		
	}
	@Test
	public void findEmployeeById_valid() {
		BasicEmployeeDTO actual = employeeService.findEmployeeById(1);
		Calendar c = Calendar.getInstance();
		c.set(1970, Calendar.FEBRUARY, 5,0,0,0); //Calendar.FEBRUARY = 1
		c.set(Calendar.MILLISECOND, 0);
		BasicEmployeeDTO expected = new BasicEmployeeDTO(1, "Vladimir", "Putin", c.getTime(), "Rusian", "putin.v@red-square.ru", "41228090002", "putin.v.red.square", null, null, null);
		Assertions.assertThat(actual).isEqualToIgnoringNullFields(expected);
	}
	@Test (expected = EntityNotFoundException.class)
	public void findEmployeeById_invalid()
	{
		employeeService.findEmployeeById(10);
	}
	@Test
	@ShouldMatchDataSet(value = "datasets/expected-employees-after-delete.yml", excludeColumns = "employeeId")
	public void deleteEmployeeByID_valid() {
		employeeService.deleteEmployeeById(1);
		
	}
}
